/**
 * 
 */
package com.geh.frontend.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.geh.frontend.dtos.PatientDetailsEditDto;
import com.geh.frontend.mappers.PatientEntityToDetailsDtoMapper;
import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.dao.PatientDAO;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Frontend Controller handling the search of hospitalization
 *
 * @author Vera Boutchkova
 */
@Controller
public class SearchHospitalizationMvcController {
	
	@Autowired
	private HospitalizationDAO hospDao;
	@Autowired
	private PatientDAO patientDao;
	@Autowired
	private PatientEntityToDetailsDtoMapper mapperEntityToDto;

	/**
	 * Returns a view wit all the Hospitalizations of a patient with given personal number passed with the "keyword"
	 * with paging. The hospitalizations are returned as a list to be displayed in html.
	 * 
	 * @param personalNumber
	 * @param page
	 * @param size
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/hospitalizationsList")
	public String searchAndListPost(@RequestParam ObjectId patientId, @RequestParam int page,
			@RequestParam int size, @RequestParam int year, Model model, HttpSession session) {

		//TODO: optimize
		Patient patient = patientDao.get(patientId);
		PatientDetailsEditDto patientDto = mapperEntityToDto.map(patient);

		return searchAndListHospitalizations(patientDto, page, size, year, model);
	}
	
	/**
	 * Returns all the Hospitalizations of a patient with given personal number passed with the "keyword"
	 * with paging. The hospitalizations are returned as a list to be displayed in html.
	 * 
	 * @param personalNumber
	 * @param page
	 * @param size
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/hospitalizationsList")
	public String searchAndListGet(@Valid PatientDetailsEditDto patientDto, @RequestParam int page,
			@RequestParam int size, @RequestParam int year, Model model) {
		
		return searchAndListHospitalizations(patientDto, page, size, year, model);
	}
	
	
	private String searchAndListHospitalizations(PatientDetailsEditDto patientDto, int page, int size, int year, Model model) {
		
		Page<Hospitalization> hospPage = hospDao.findByPatientAndYear(patientDto.getId(), year, new PageRequest(page, size));
		setModelForHospitalizationsList(model, patientDto, size, year, hospPage);
		
		List<Hospitalization> displayList = hospPage.getContent();
		model.addAttribute("hospitalizations", displayList);
		model.addAttribute("year", year);
		
		addPageAttributes(patientDto.getId(), page, size, model, hospPage);

		return "hospitalizationsList";
	}
	
	private void setModelForHospitalizationsList(Model model, PatientDetailsEditDto patientDto, int size, int year, Page<Hospitalization> hospPage) {
		model.addAttribute("patient", patientDto);
		if (patientDto.getLastHospitalization() == null) {
			long uncompleted = hospDao.countUncompletedHospitalizationsForPatientAndYear(patientDto.getId(), year);
			model.addAttribute("uncompletedHospitalizations", uncompleted);
			model.addAttribute("hospPage", hospPage);
			addPageAttributes(patientDto.getId(), 0, size, model, hospPage);
		}
	}

	private void addPageAttributes(String patientId, int page, int size, Model model, Page<?> entitiesPage) {
		model.addAttribute("totalElements", entitiesPage.getTotalElements());
		model.addAttribute("totalPages", entitiesPage.getTotalPages());
		model.addAttribute("patientId", patientId);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("hospPage", entitiesPage);
	}
}
