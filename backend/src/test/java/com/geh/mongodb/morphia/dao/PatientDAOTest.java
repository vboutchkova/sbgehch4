/**
 * pier
 */
package com.geh.mongodb.morphia.dao;

import java.util.HashSet;
import java.util.Set;

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
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Tests PatientDAO
 *
 * @author Vera Boutchkova
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={com.geh.ApplicationConfigurationBackend.class})
public class PatientDAOTest {
	
	private static final String START_OF_PERSONAL_NUMBER = "1112111";
	
	@Autowired
	private PatientDAO pdao;
	private Set<Patient> patients = new HashSet<>();
	
	@Before
	public void setUp() {
		addOnePatient("4XX");
		addOnePatient("5YY");
		addOnePatient("6xx");
	}
	
	private void addOnePatient(String postfix) {

		Patient patient = new Patient();
		patient.setEmail("ali@abv.bg");
		patient.setPersonalNumber(START_OF_PERSONAL_NUMBER + postfix);
		patient.setFirstName("XXX " + postfix);
		patient.setName("YYYYYYY");
		patient.setCreatedBy("asdfghjkl");
		
		Address address = new Address();
		address.setTown("София");
		patient.setAddress(address);
		
		Key<Patient> result = pdao.save(patient);
		patients.add(patient);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getId());
		Assert.assertEquals("patient", result.getCollection());
	}
	

	@After
	public void cleanUp() {
		patients.forEach(pdao::delete);
		patients.clear();
	}
	
	@Test
	public void testFindExactMatch() {
		Page<Patient> resultPage = pdao.findByPersonalNumberAndPage(START_OF_PERSONAL_NUMBER + "4XX", new PageRequest(0, 10));
		
		Assert.assertNotNull(resultPage);
		Assert.assertTrue(resultPage.hasContent());
		Assert.assertFalse(resultPage.hasNext());
		Assert.assertFalse(resultPage.hasPrevious());
		Assert.assertTrue(resultPage.isFirst());
		Assert.assertTrue(resultPage.isLast());
		Assert.assertEquals(0, resultPage.getNumber());
		Assert.assertEquals(1, resultPage.getNumberOfElements());
		Assert.assertEquals(10, resultPage.getSize());
		Assert.assertEquals(1, resultPage.getTotalPages());
		Assert.assertEquals(1, resultPage.getTotalElements());
		
		Assert.assertEquals(START_OF_PERSONAL_NUMBER + "4XX",resultPage.getContent().get(0).getPersonalNumber());
	}
	
	@Test
	public void testFindExactMatchNegative1() {
		Page<Patient> resultPage = pdao.findByPersonalNumberAndPage("Z", new PageRequest(0, 10));
		Assert.assertNotNull(resultPage);
		Assert.assertFalse(resultPage.hasContent());
	}
	
	@Test
	public void testFindSubstringMatch() {
		Page<Patient> resultPage = pdao.findByPersonalNumberAndPageMatch("11121114", new PageRequest(0, 10));
		Assert.assertNotNull(resultPage);
		Assert.assertTrue(resultPage.hasContent());
		Assert.assertFalse(resultPage.hasNext());
		Assert.assertFalse(resultPage.hasPrevious());
		Assert.assertTrue(resultPage.isFirst());
		Assert.assertTrue(resultPage.isLast());
		Assert.assertEquals(0, resultPage.getNumber());
		Assert.assertEquals(1, resultPage.getNumberOfElements());
		Assert.assertEquals(10, resultPage.getSize());
		Assert.assertEquals(1, resultPage.getTotalPages());
		Assert.assertEquals(1, resultPage.getTotalElements());
	}
}
