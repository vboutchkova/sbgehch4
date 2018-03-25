/**
 * 
 */
package com.geh.bc;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import com.geh.frontend.controllers.FrontendException;
import com.geh.mongodb.morphia.dao.PatientDAO;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Business Controller for searching patients
 *
 * @author Vera Boutchkova
 */
@Controller
public class SearchPatientsBusinessController {

	@Autowired
	private PatientDAO dao;

	/**
	 * Fetches patient by ID
	 * 
	 * @param patientId
	 * @return
	 */
	public Patient getById(ObjectId patientId) {
		return dao.get(patientId);
	}

	/**
	 * Searches patients by personalNumber, page and page size
	 * 
	 * @param personalNumber
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Patient> findByPersonalNumberAndPage(String personalNumber, int page, int size) {
		return dao.findByPersonalNumberAndPage(personalNumber, new PageRequest(page, size));
	}

	/**
	 * Searches patients by given personalNumber or subpart of it, page and page size
	 * 
	 * @param keyword
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Patient> findByPersonalNumberAndPageSubstring(String keyword, int page, int size) {
		Page<Patient> patientsIds;
		if (keyword.length() == 10) {
			patientsIds = dao.findByPersonalNumberAndPage(keyword, new PageRequest(page, size));
		} else {
			// TODO: Escape special characters
			try {
				patientsIds = dao.findByPersonalNumberAndPageMatch(keyword, new PageRequest(page, size));
			} catch (Exception e) {
				throw new FrontendException(
						String.format("Search by personal number keyword [%s] failed", keyword), e);
			}
		}
		return patientsIds;
	}
}
