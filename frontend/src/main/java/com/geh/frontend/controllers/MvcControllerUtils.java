/**
 * 
 */
package com.geh.frontend.controllers;

import java.util.Calendar;
import java.util.List;

import org.springframework.ui.Model;

import com.geh.mongodb.morphia.dao.DoctorDAO;
import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.entities.Doctor;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Utility methods for the Frontend controllers
 *
 * @author Vera Boutchkova
 */
public class MvcControllerUtils {

	private MvcControllerUtils() {
	}

	static void initSelects(Model model, DoctorDAO doctorDao) {
		List<Doctor> doctors = doctorDao.find().asList();
		model.addAttribute("doctors", doctors);
		List<Doctor> heads = doctorDao.findHeadsOfWards();
		model.addAttribute("warHeads", heads);
	}

	static void setModelForHospitalizationsList(Model model, Patient patient, HospitalizationDAO hospDao) {
		if (patient.getLastHospitalization() == null) {
			long uncompleted = hospDao.countUncompletedHospitalizationsForPatientAndYear(patient.getId().toString(),
					Calendar.getInstance().get(Calendar.YEAR));
			model.addAttribute("uncompletedHospitalizations", uncompleted);
		}
	}
}
