/**
 * 
 */
package com.geh.mongodb.morphia.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.geh.mongodb.morphia.entities.Address;
import com.geh.mongodb.morphia.entities.ClinicalResult;
import com.geh.mongodb.morphia.entities.Examination;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.LaboratoryResult;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Tests HospitalizationDAO
 *
 * @author Vera Boutchkova
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { com.geh.ApplicationConfigurationBackend.class })
public class HospitalizationDAOTest {

	private static final String PERSONAL_NUMBER = "1111111111";
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private PatientDAO pdao;
	@Autowired
	private HospitalizationDAO hdao;

	private Set<Hospitalization> hospitalizations = new HashSet<>();
	private Set<Hospitalization> hospitalizationsCheck = new HashSet<>();
	private Set<Patient> patients = new HashSet<>();

	private String objectId;

	@Before
	public void setUp() throws Exception {
		objectId = addOnePatient(5, hospitalizationsCheck);
		addOnePatient(6, null);
	}

	private String addOnePatient(int num, Set<Hospitalization> hospitalizationsCheck) throws ParseException {

		Patient patient = new Patient();
		patient.setEmail("ali@abv.bg");
		patient.setPersonalNumber(PERSONAL_NUMBER);
		//TODO set more fields....

		Key<Patient> result = pdao.save(patient);

		Address address = new Address();
		//TODO set more fields....
		patient.setAddress(address);

		pdao.save(patient);

		Hospitalization hospitalization = new Hospitalization();
		hospitalization.setName("Completed Test hospitalization 1111_" + (num * 2 + 1));
		hospitalization.setStartDate(FORMATTER.parse("31-12-2018")); // uncompleted
		//TODO set more fields....
		hospitalization.setPatient(patient);

		hdao.save(hospitalization);
		hospitalizations.add(hospitalization);
		if (hospitalizationsCheck != null) {
			hospitalizationsCheck.add(hospitalization);
		}

		//TODO set more hospitalizations....

		hdao.save(hospitalization);
		hospitalizations.add(hospitalization);
		if (hospitalizationsCheck != null) {
			hospitalizationsCheck.add(hospitalization);
		}

		hospitalization = new Hospitalization();
		hospitalization.setName("Completed Test hospitalization 3333_" + (num * 3 + 1));
		hospitalization.setStartDate(FORMATTER.parse("31-01-2018")); // uncompleted
		//TODO set more fields....
		ClinicalResult clinicalResult = new ClinicalResult();
		clinicalResult.setName("CR XX NAME");
		//TODO set more fields....
		hospitalization.addClinicalResult(clinicalResult);

		LaboratoryResult lr = new LaboratoryResult();
		//TODO set more fields....
		hospitalization.addLaboratoryResults(lr);
		
		Examination ex = new Examination();
		//TODO set more fields....
		hospitalization.addExaminations(ex);
		hospitalization.setPatient(patient);

		hdao.save(hospitalization);
		hospitalizations.add(hospitalization);
		if (hospitalizationsCheck != null) {
			hospitalizationsCheck.add(hospitalization);
		}
		patient.setLastHospitalization(hospitalization);
		pdao.save(patient);
		patients.add(patient);

		return result.getId().toString();
	}

	@After
	public void cleanUp() {
		patients.forEach(pdao::delete);
		patients.clear();
		hospitalizations.forEach(hdao::delete);
		hospitalizations.clear();
	}

	@Test
	public void testCountUncompletedHospitalizationsForPatient() throws Exception {
		Patient patient = pdao.get(new ObjectId(objectId));

		long result = hdao.count("patient", patient);
		Assert.assertEquals("Expected total 4 hospitalizations", 4, result);

		result = hdao.countUncompletedHospitalizationsForPatientAndYear(patient.getId().toString(), 2018);
		Assert.assertEquals("Expected 2 uncompleted hospitalizations for this year", 2, result);
	}

	@Test
	public void testFindByPatientAndYear() throws Exception {
		Patient patient = pdao.get(new ObjectId(objectId));
		Page<Hospitalization> resultPage = hdao.findByPatientAndYear(patient.getId().toString(), 2018,
				new PageRequest(0, 10));

		Assert.assertNotNull(resultPage);
		Assert.assertTrue(resultPage.hasContent());
		Assert.assertFalse(resultPage.hasNext());
		Assert.assertFalse(resultPage.hasPrevious());
		Assert.assertTrue(resultPage.isFirst());
		Assert.assertTrue(resultPage.isLast());
		Assert.assertEquals(0, resultPage.getNumber());
		Assert.assertEquals(3, resultPage.getNumberOfElements());
		Assert.assertEquals(10, resultPage.getSize());
		Assert.assertEquals(1, resultPage.getTotalPages());
		Assert.assertEquals(3, resultPage.getTotalElements());

		Set<Hospitalization> checkResults = new HashSet<>();
		checkResults.addAll(resultPage.getContent());

		Assert.assertEquals(hospitalizationsCheck, checkResults);
		
		resultPage = hdao.findByPatientAndYear(patient.getId().toString(), 2018, new PageRequest(1, 2)); //1st page of pages with size 2
		Assert.assertNotNull(resultPage);
		Assert.assertTrue(resultPage.hasContent());
		Assert.assertFalse(resultPage.hasNext());
		Assert.assertTrue(resultPage.hasPrevious());
		Assert.assertFalse(resultPage.isFirst());
		Assert.assertTrue(resultPage.isLast());
		Assert.assertEquals(1, resultPage.getNumber());
		Assert.assertEquals(1, resultPage.getNumberOfElements());
		Assert.assertEquals(2, resultPage.getSize());
		Assert.assertEquals(2, resultPage.getTotalPages());
		Assert.assertEquals(3, resultPage.getTotalElements());
	}

	/**
	 * For all years
	 * @throws Exception
	 */
	@Test
	public void testFindByPatient() throws Exception {
		Patient patient = pdao.get(new ObjectId(objectId));
		Page<Hospitalization> resultPage = hdao.findByPatient(patient, new PageRequest(1, 2)); //1st page of pages with size 2
		Assert.assertNotNull(resultPage);
		Assert.assertTrue(resultPage.hasContent());
		Assert.assertFalse(resultPage.hasNext());
		Assert.assertTrue(resultPage.hasPrevious());
		Assert.assertFalse(resultPage.isFirst());
		Assert.assertTrue(resultPage.isLast());
		Assert.assertEquals(1, resultPage.getNumber());
		Assert.assertEquals(2, resultPage.getNumberOfElements());
		Assert.assertEquals(2, resultPage.getSize());
		Assert.assertEquals(2, resultPage.getTotalPages());
		Assert.assertEquals(4, resultPage.getTotalElements());
	}
}
