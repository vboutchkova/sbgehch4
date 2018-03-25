/**
 * 
 */
package com.geh.frontend.controllers;

import javax.servlet.http.HttpServletRequest;
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

import com.geh.bc.SearchPatientsBusinessController;
import com.geh.frontend.dtos.PatientDetailsEditDto;
import com.geh.frontend.mappers.PatientDetailsDtoToEntityMapper;
import com.geh.frontend.mappers.PatientEntityToDetailsDtoMapper;
import com.geh.mongodb.morphia.dao.PatientDAO;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Frontend Controller handling the update of existing patient
 *
 * @author Vera Boutchkova
 */
@Controller
public class EditPatientMvcController {

	/**
	 * Note this value should correspond to the name of the dto class
	 * PatientDetailsEditDto see
	 * http://forum.thymeleaf.org/Fields-object-functions-Spring-td3302513.html#a3304174
	 */
	private static final String PATIENT_DETAILS_EDIT_DTO = "patientDetailsEditDto";

	private static Logger logger = LoggerFactory.getLogger(EditPatientMvcController.class);

	@Autowired
	private PatientDAO patientDao;
	@Autowired
	private SearchPatientsBusinessController searchBC;
	@Autowired
	private PatientEntityToDetailsDtoMapper mapperEntityToDto;
	@Autowired
	private PatientDetailsDtoToEntityMapper mapperToEntity;

	/**
	 * Returns a view for editing a patient for the given id
	 * 
	 * @param patientId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/editPatient/{patientId}")
	public String getPatient(@PathVariable ObjectId patientId, Model model, HttpServletRequest request) {

		Patient patient = searchBC.getById(patientId);
		PatientDetailsEditDto patientDto = mapperEntityToDto.map(patient);
		model.addAttribute(PATIENT_DETAILS_EDIT_DTO, patientDto);

		return "editpatient";
	}

	/**
	 * Updates the existing patient with the edied data
	 * 
	 * @param patientDto
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updatePatient")
	public String updatePatient(@Valid PatientDetailsEditDto patientDto, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "editpatient";
		}

		Patient patient = patientDao.get(new ObjectId(patientDto.getId()));
		if (!patient.getPersonalNumber().equals(patientDto.getPersonalNumber())) {
			boolean exists = patientDao.exists("personalNumber", patientDto.getPersonalNumber());
			if (exists) {
				String errorMsg = String.format(
						"Failed to update the patient, because a patient with such personal number %s already exists.",
						patientDto.getPersonalNumber());
				model.addAttribute("errorMessage", errorMsg);
				model.addAttribute(PATIENT_DETAILS_EDIT_DTO, patientDto);
				logger.warn(errorMsg);
				return "editpatient";
			}
		}

		patient = mapperToEntity.map(patientDto, patient);
		Key<Patient> key = patientDao.save(patient);
		String patientId = key.getId().toString();
		patientDto.setId(patientId);

		return "redirect:/patient/" + patientId;
	}

	/**
	 * Returns a view for entering new patient
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/newPatient")
	public String newPatient(Model model) {

		model.addAttribute(PATIENT_DETAILS_EDIT_DTO, new PatientDetailsEditDto());
		return "newpatient";
	}

	/**
	 * Saves the new patient into DB
	 * 
	 * @param patientDto
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveNewPatient")
	public String saveNewPatient(@Valid PatientDetailsEditDto patientDto, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "newpatient";
		}

		boolean exists = patientDao.exists("personalNumber", patientDto.getPersonalNumber());
		if (exists) {
			String errorMsg = String.format(
					"Failed to save the patient, because a patient with such personal number %s already exists.",
					patientDto.getPersonalNumber());
			model.addAttribute("errorMessage", errorMsg);
			model.addAttribute(PATIENT_DETAILS_EDIT_DTO, patientDto);
			logger.warn(errorMsg);
			return "newpatient";
		}
		Patient patient = mapperToEntity.map(patientDto);
		Key<Patient> key = patientDao.save(patient);
		String patientId = key.getId().toString();
		patientDto.setId(patientId);

		return "redirect:/patient/" + patientId;
	}
}
