package com.geh.frontend.controllers;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.geh.bc.SearchPatientsBusinessController;
import com.geh.frontend.dtos.PatientDetailsEditDto;
import com.geh.frontend.dtos.PatientListDto;
import com.geh.frontend.mappers.PatientEntityToDetailsDtoMapper;
import com.geh.frontend.mappers.PatientEntityToDtoListMapper;
import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Frontend Controller handling the search of patients
 *
 * @author Vera Boutchkova
 */
@Controller
public class SearchPatientMvcController {

	private static Logger logger = LoggerFactory.getLogger(SearchPatientMvcController.class);

	@Value("${spring.application.name}")
	String appName;

	@Autowired
	private HospitalizationDAO hospDao;
	@Autowired
	private SearchPatientsBusinessController searchBC;
	@Autowired
	private PatientEntityToDtoListMapper mapper;
	@Autowired
	private PatientEntityToDetailsDtoMapper mapperEntityToDto;

	/**
	 * Returns a patient for a given ID
	 * 
	 * @param patientId
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/patient/{patientId}")
	public String getPatient(@PathVariable ObjectId patientId, Model model) {
		Patient patient = searchBC.getById(patientId);
		PatientDetailsEditDto patientDto = mapperEntityToDto.map(patient);
		model.addAttribute("patient", patientDto);
		MvcControllerUtils.setModelForHospitalizationsList(model, patient, hospDao);
		return "hospitalizationsList";
	}

	/**
	 * Returns all the Patients with given personal number passed with the "keyword"
	 * with paging. The patients are returned as a list to be displayed in html.
	 * 
	 * @param keyword
	 * @param page
	 * @param size
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/patients")
	public String searchAndListGet(@RequestParam String personalNumber, @RequestParam int page, @RequestParam int size,
			Model model) {

		return searchAndListPatients(personalNumber, page, size, model);
	}

	/**
	 * Executes a search of patient by personal number and returns a paged result
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/patients")
	public String searchAndListPost(@RequestParam String personalNumber, @RequestParam int page, @RequestParam int size,
			Model model) {

		return searchAndListPatients(personalNumber, page, size, model);
	}

	private String searchAndListPatients(String personalNumber, int page, int size, Model model) {

		Page<Patient> patientsPage;
		String showView = "patientsList";

		personalNumber = personalNumber.trim();
		patientsPage = searchBC.findByPersonalNumberAndPageSubstring(personalNumber, page, size);

		List<PatientListDto> displayList = mapper.mapList(patientsPage.getContent());
		model.addAttribute("patientsIds", displayList);

		if (!patientsPage.getContent().isEmpty()) {
			PatientDetailsEditDto patientDto = mapperEntityToDto.map(patientsPage.getContent().get(0));
			model.addAttribute("patient", patientDto);
		}

		if (patientsPage.getTotalElements() == 1) {
			// Redirect to hospitalizations list
			MvcControllerUtils.setModelForHospitalizationsList(model, patientsPage.getContent().get(0), hospDao);
			showView = "hospitalizationsList";
		} else {
			addPageAttributes(personalNumber, page, size, model, patientsPage);
		}
		return showView;
	}

	private void addPageAttributes(String personalNumber, int page, int size, Model model, Page<?> entitiesPage) {
		model.addAttribute("totalElements", entitiesPage.getTotalElements());
		model.addAttribute("totalPages", entitiesPage.getTotalPages());
		model.addAttribute("personalNumber", personalNumber);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("patientsPage", entitiesPage);
	}
}
