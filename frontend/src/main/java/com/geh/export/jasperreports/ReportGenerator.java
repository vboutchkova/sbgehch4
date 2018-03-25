/**
 * 
 */
package com.geh.export.jasperreports;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geh.export.jasperreports.dtos.HospitalizationJRDto;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;

/**
 * Generator of PDF file from Compiled JasperReports
 *
 * @author Vera Boutchkova
 */
public class ReportGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(ReportGenerator.class);
	
	public static final String DEFAULT_UTF_8 = "UTF-8";
	
	private String jsonCharset;
	
	@Value("classpath:/reports/EpicrisisA4.jasper")
	private Resource compiledJasperReport;
	@Value("classpath:/reports/images/")
	private Resource reportImagesDir;
	@Value("classpath:/reports/")
	private Resource subreportsDir;
	
	public byte[] fill(HospitalizationJRDto dto) {
		long start = System.currentTimeMillis();

		Map<String, Object> params = new HashMap<String, Object>();		
		
		ObjectMapper mapper = new ObjectMapper();
		byte[] result = new byte[0];
		try {
			String jsonInString = mapper.writeValueAsString(dto);
			logger.debug("Default Locale : " + java.util.Locale.getDefault());
			logger.debug("Default Charset : " + java.nio.charset.Charset.defaultCharset());
			logger.debug("jsonCharset : " + jsonCharset);
			
			JRDataSource jrDataSource;
			try {
				jrDataSource = new JsonDataSource(new ByteArrayInputStream(jsonInString.getBytes(jsonCharset)));
			} catch (Exception e) {
				jsonCharset = DEFAULT_UTF_8;
				jrDataSource = new JsonDataSource(new ByteArrayInputStream(jsonInString.getBytes(jsonCharset)));
			}
			
			logger.debug(String.format("FileName : %s exists : %s", compiledJasperReport.getFilename(), compiledJasperReport.exists()));
			logger.debug(String.format("ImageDir : %s exists : %s", reportImagesDir.getFilename(), reportImagesDir.exists()));
			
			params.put("IMAGES_DIR", reportImagesDir.getURL().toString());
			params.put("SUBREPORT_DIR", subreportsDir.getURL().toString());

			logger.debug(String.format("Jasper params : %s", params));

			
//			JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.default.pdf.encoding",
//                    UTF_8.toString());
			
			InputStream jrFileInputStream = compiledJasperReport.getInputStream();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jrFileInputStream, params, jrDataSource);
			
			logger.debug(String.format("Filling time : %s", (System.currentTimeMillis() - start)));

			result = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException | IOException e) {
			logger.error("Error occurred while exporting to PDF file. See details", e);
		}
		
		logger.debug("Result : DONE size in bytes:" + result.length);
		return result;
	}


	public void setJasperReportForEpicrisis(Resource jasperReportForEpicrisis) {
		this.compiledJasperReport = jasperReportForEpicrisis;
	}


	public void setJsonCharset(String jsonCharset) {
		this.jsonCharset = jsonCharset;
	}
	
	
}
