/**
 * 
 */
package com.geh.frontend.controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.geh.frontend.dtos.HospitalizationEditDto;
import com.geh.frontend.mappers.HospitalizationDtoNewToEntityMapper;
import com.geh.frontend.mappers.HospitalizationEntityToEditDtoMapper;
import com.geh.mongodb.morphia.dao.DoctorDAO;
import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.dao.PatientDAO;
import com.geh.mongodb.morphia.entities.Doctor;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Frontend Controller handling the adding new hospitalization
 *
 * @author Vera Boutchkova
 */
@Controller
public class AddNewHospitalizationMvcController {
	
	private static final String HOSPITALIZATION_PARAM_NAME = "hospitalization";

	private static Logger logger = LoggerFactory.getLogger(AddNewHospitalizationMvcController.class);
	
	@Autowired
	private HospitalizationDAO hospDao;
	@Autowired
	private PatientDAO patientDao;
	@Autowired
	private DoctorDAO doctorDao;
	@Autowired
	private HospitalizationDtoNewToEntityMapper mapper;
	@Autowired
	private HospitalizationEntityToEditDtoMapper mapperEntityToDto;
	
	
	/**
	 * Returns a Hospitalization for given ID
	 * 
	 * @param hospitalizationId
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/hospitalization/{hospitalizationId}")
	public String getHospitalization(@PathVariable ObjectId hospitalizationId, Model model) {
		
		Hospitalization hospitalization = hospDao.get(hospitalizationId);
		HospitalizationEditDto hospitalizationDto = mapperEntityToDto.map(hospitalization);
		model.addAttribute(HOSPITALIZATION_PARAM_NAME, hospitalizationDto);
		
		MvcControllerUtils.initSelects(model, doctorDao);
		return HOSPITALIZATION_PARAM_NAME;
	}
	
	
	/**
	 * Saves a new hospitalization
	 * 
	 * @param hospitalizationDto	containing the patient id
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveNewHospitalization")
	public String saveNewHospitalization(@Valid HospitalizationEditDto hospitalizationDto, BindingResult bindingResult, Model model) {
		
		Hospitalization hospitalization = mapper.map(hospitalizationDto);
		
		if (hospitalizationDto.getPatientId() != null) {
			Patient patient = patientDao.get(new ObjectId(hospitalizationDto.getPatientId()));
			hospitalization.setPatient(patient);
			if (hospitalizationDto.getStartDate() == null) {
				hospitalization.setStartDate(new Date());
			}
			Key<Hospitalization> saved = hospDao.save(hospitalization);
			Object id = saved.getId();
			
			if (id != null) {
				patient.setLastHospitalization(hospitalization);
				patientDao.save(patient);
				
				hospitalizationDto = mapperEntityToDto.map(hospitalization);
				model.addAttribute(HOSPITALIZATION_PARAM_NAME, hospitalizationDto);
				
				MvcControllerUtils.initSelects(model, doctorDao);
				return HOSPITALIZATION_PARAM_NAME;
			} else {
				logger.error("Failed to save new hospitalization for patient with personal number " + patient.getPersonalNumber());
				model.addAttribute("errorMessage", "Failed to save new hospitalization: internal error");
				return "error";
			}
		}
		
		logger.error("Failed to save new hospitalization, because patientId is null");
		model.addAttribute("errorMessage", "Failed to save new hospitalization: internal error");
		return "error";

	}
	
	
	/**
	 * Returns a page for entering a new hospitalization for the given patient
	 * 
	 * @param patientId
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/newHospitalization/{patientId}")
	public String newHospitalization(@PathVariable ObjectId patientId, Model model) {
		
		HospitalizationEditDto hospitalization = new HospitalizationEditDto();
		hospitalization.setPatientId(patientId.toString());
		model.addAttribute("hospitalizationDto", hospitalization);

		List<Doctor> doctors = doctorDao.find().asList();
		model.addAttribute("doctors", doctors);
		List<Doctor> heads = doctorDao.findHeadsOfWards();
		model.addAttribute("warHeads", heads);
		
		Patient patient = patientDao.get(patientId);
		model.addAttribute("patient", patient);
		
		return "newhospitalization";

	}

}
