/**
 * 
 */
package com.geh.frontend.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.geh.export.jasperreports.ReportGenerator;
import com.geh.export.jasperreports.dtos.HospitalizationJRDto;
import com.geh.export.jasperreports.mappers.HospitalizationEntityToJRDtoMapper;
import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.ReportFile;

/**
 * Frontend Controller handling the export of PDF document for a hospitalization
 *
 * @author Vera Boutchkova
 */
@Controller
public class ExportHospitalizationMvcController {

	private static Logger logger = LoggerFactory.getLogger(ExportHospitalizationMvcController.class);

	@Autowired
	private HospitalizationDAO hospDao;
	@Autowired
	private HospitalizationEntityToJRDtoMapper hospMapper;
	@Autowired
	private ReportGenerator reportGenerator;

	/**
	 * Returns a newly generated PDF file for the given hospitalization data
	 * 
	 * @param hospitalizationId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/pdf/{hospitalizationId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> generateHospitalizationPdf(@PathVariable ObjectId hospitalizationId) {

		Hospitalization hospitalization = hospDao.get(hospitalizationId);
		HospitalizationJRDto hospDto = hospMapper.map(hospitalization);

		byte[] filledEpicrisis = reportGenerator.fill(hospDto);
		try (InputStream insPdf = new ByteArrayInputStream(filledEpicrisis)) {

			// TODO: Up to 16MB - add check and error handling
			InputStreamResource bodyContent = new InputStreamResource(insPdf);

			ReportFile file = new ReportFile();
			file.setFileName(getFileName(hospitalization));
			file.setFileContent(filledEpicrisis);
			file.setGeneratedDate(new Date());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			file.setGeneratedBy(auth.getName());

			hospitalization.setEpicrisisFile(file);
			hospitalization.setEpicrisisIssued(true);
			hospDao.save(hospitalization);

			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"")
					.contentLength(filledEpicrisis.length).body(bodyContent);
		} catch (IOException e) {
			logger.warn("Failed to generate PDF document for hospitalization " + hospitalization.getId(), e);
			throw new FrontendException(
					"Failed to generate PDF document for hospitalization " + hospitalization.getId(), e);
		}
	}

	/**
	 * Returns the last generated PDF file for the given hospitalization data
	 * 
	 * @param hospitalizationId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/pdf/{hospitalizationId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> getHospitalizationPdfPreview(@PathVariable ObjectId hospitalizationId) {

		Hospitalization hospitalization = hospDao.get(hospitalizationId);
		byte[] filledEpicrisis = null;

		if (hospitalization.getEpicrisisFile() != null) {
			filledEpicrisis = hospitalization.getEpicrisisFile().getFileContent();
		}

		if (filledEpicrisis != null && filledEpicrisis.length > 0) {
			try (InputStream insPdf = new ByteArrayInputStream(filledEpicrisis)) {

				InputStreamResource bodyContent = new InputStreamResource(insPdf);
				return ResponseEntity.ok().contentLength(filledEpicrisis.length).body(bodyContent);
			} catch (IOException e) {
				String message = getPdfPreviewErrorMessage(hospitalization);
				logger.warn(message, e);
				throw new FrontendException(message, e);
			}
		} else {
			throw new FrontendException(
					"Failed to display " + (hospitalization.getEpicrisisFile() == null ? "nonexisting  " : "")
							+ "PDF document for hospitalization " + hospitalization.getId());
		}
	}

	/**
	 * @return
	 */
	private static String getPdfPreviewErrorMessage(Hospitalization hospitalization) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(hospitalization.getEpicrisisFile().getGeneratedDate().getTime());
		return "Failed to display PDF document from "
				+ new SimpleDateFormat("dd-MM-yyyy HH:mm").format(calendar.getTime()) + " for hospitalization "
				+ hospitalization.getId();
	}

	private static String getFileName(Hospitalization hospitalization) {
		Calendar calendar = Calendar.getInstance();
		if (hospitalization.getEndDate() == null) {
			calendar.setTimeInMillis(System.currentTimeMillis());
		} else {
			calendar.setTimeInMillis(hospitalization.getEndDate().getTime());
		}
		String datePostfix = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());

		return "Epicrisis_" + datePostfix + ".pdf";
	}
}
