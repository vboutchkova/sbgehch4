/**
 * 
 */
package com.geh.bc;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.geh.frontend.dtos.PatientListDto;
import com.geh.frontend.mappers.PatientEntityToDtoListMapper;
import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.dao.PatientDAO;
import com.geh.mongodb.morphia.entities.Address;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Tests PatientBusinessController
 *
 * @author Vera Boutchkova
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= {com.geh.ApplicationConfiguration.class})
public class SearchPatientsBusinessControllerTest {
	private static final String PERSONAL_NUMBER = "4455567890";

	@Autowired
	private PatientDAO pdao;
	@Autowired
	private HospitalizationDAO hdao;
	@Autowired
	private SearchPatientsBusinessController bc;
	@Autowired
	private PatientEntityToDtoListMapper mapper;
	
	private Set<Hospitalization> hospitalizations = new HashSet<>();
	private Set<Patient> patients = new HashSet<>();
	
	private String objectId;

	@Before
	public void setUp() {
		addOnePatient(7);
		addOnePatient(8);
		addOnePatient(9);
		addOnePatient(10);
		addOnePatient(11);
	}

	private void addOnePatient(int num) {

		Patient patient = new Patient();
		patient.setEmail("ali@abv.bg");
		patient.setPersonalNumber(PERSONAL_NUMBER);
		patient.setFirstName("XXX" + num);
		patient.setName("Иванов");
		patient.setCreatedBy("RLddkd");
		
		Key<Patient> result = pdao.save(patient);
		objectId = result.getId().toString();

		Hospitalization hospitalization = new Hospitalization();
		hospitalization.setName("PatientDAOTest hospitalization " + num);
		hospitalization.setStartDate(new Date());
		hdao.save(hospitalization);
		
		patient.setLastHospitalization(hospitalization);
		
		Address address = new Address();
		address.setCountry("XXX");
		address.setTown("София");
		address.setNumber("67A");
		address.setPostcode("4518");
		address.setStreet("xxX");
		
		patient.setAddress(address);
		
		pdao.save(patient);
		
		patients.add(patient);
		hospitalizations.add(hospitalization);
	}

	@After
	public void cleanUp() {
		patients.forEach(pdao::delete);
		patients.clear();
		hospitalizations.forEach(hdao::delete);
		hospitalizations.clear();
	}

	@Test
	public void testGetById() throws Exception {
		Patient patient = bc.getById(new ObjectId(objectId));
		Assert.assertNotNull("The result should not be null", patient);
	}
	
	/**
	 * Tests 5 patients with same personal number paging with size 2 should result in 3 pages.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFindByPersonalNumberAndPage() throws Exception {
	
		Page<Patient> resultPage = bc.findByPersonalNumberAndPage(PERSONAL_NUMBER, 0, 2);
		Assert.assertNotNull(resultPage);
		Assert.assertTrue(resultPage.hasContent());
		Assert.assertTrue(resultPage.hasNext());
		Assert.assertFalse(resultPage.hasPrevious());
		Assert.assertTrue(resultPage.isFirst());
		Assert.assertFalse(resultPage.isLast());
		Assert.assertEquals(0, resultPage.getNumber());
		Assert.assertEquals(2, resultPage.getNumberOfElements());
		Assert.assertEquals(2, resultPage.getSize());
		Assert.assertEquals(3, resultPage.getTotalPages());
		Assert.assertEquals(5, resultPage.getTotalElements());
		
		List<PatientListDto> displayList = mapper.mapList(resultPage.getContent());
		Assert.assertNotNull(displayList);
		Assert.assertFalse(displayList.isEmpty());
		
		Set<Patient> checkResults = new HashSet<>();
		checkResults.addAll(resultPage.getContent());
		
		resultPage = bc.findByPersonalNumberAndPage(PERSONAL_NUMBER, 1, 2);
		Assert.assertNotNull(resultPage);
		Assert.assertTrue(resultPage.hasContent());
		Assert.assertTrue(resultPage.hasNext());
		Assert.assertTrue(resultPage.hasPrevious());
		Assert.assertFalse(resultPage.isFirst());
		Assert.assertFalse(resultPage.isLast());
		Assert.assertEquals(1, resultPage.getNumber());
		Assert.assertEquals(2, resultPage.getNumberOfElements());
		Assert.assertEquals(2, resultPage.getSize());
		Assert.assertEquals(3, resultPage.getTotalPages());
		Assert.assertEquals(5, resultPage.getTotalElements());
		
		checkResults.addAll(resultPage.getContent());
		
		resultPage = bc.findByPersonalNumberAndPage(PERSONAL_NUMBER, 2, 2);
		Assert.assertNotNull(resultPage);
		Assert.assertTrue(resultPage.hasContent());
		Assert.assertFalse(resultPage.hasNext());
		Assert.assertTrue(resultPage.hasPrevious());
		Assert.assertFalse(resultPage.isFirst());
		Assert.assertTrue(resultPage.isLast());
		Assert.assertEquals(2, resultPage.getNumber());
		Assert.assertEquals(1, resultPage.getNumberOfElements());
		Assert.assertEquals(2, resultPage.getSize());
		Assert.assertEquals(3, resultPage.getTotalPages());
		Assert.assertEquals(5, resultPage.getTotalElements());
		
		checkResults.addAll(resultPage.getContent());

		Assert.assertEquals(patients, checkResults);
	}
}
