/**
 * 
 */
package com.geh.mongodb.morphia.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.geh.mongodb.morphia.entities.Doctor;
import com.geh.mongodb.morphia.entities.Position;

/**
 * Tests Doctor DAO
 * 
 * @author Vera Boutchkova
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={com.geh.ApplicationConfigurationBackend.class})
public class DoctorDAOTest {

	@Autowired
	private DoctorDAO ddao;
	
	private Set<Doctor> doctors = new HashSet<>();
	private Set<Doctor> doctorsCheck = new HashSet<>();

	@Before
	public void setUp() {
		addDoctors();
	}
	
	private void addDoctors() {

		Doctor doc = new Doctor();

		doc.setFirstName("Ааxxx");
		doc.setSurName("YYY");
		doc.setCreatedBy("Админ");
		doc.setPrefix("Д-р");
		doc.setSpecialty("ERTY");
		doc.setWard("XDFRYRFHDHFH");
		
		ddao.save(doc);
		doctors.add(doc);
		
		doc = new Doctor();
		doc.setFirstName("Яяяzzz");
		doc.setSurName("CCC");
		doc.setCreatedBy("Потребител");
		doc.setPrefix("Д-р");
		doc.setSpecialty("FRTRS");
		doc.setPosition(Position.HEAD_OF_WARD);
		doc.setWard("DGSGDSGSDGRE");
		
		ddao.save(doc);
		doctors.add(doc);

		//TODO set more fields and entities...
	}
	
	@After
	public void cleanUp() {
		doctors.forEach(ddao::delete);
		doctors.clear();
	}
	
	@Test
	public void testFindHeadsOfWards() {
		List<Doctor> heads = ddao.findHeadsOfWards();
		Assert.assertNotNull(heads);
		Assert.assertTrue(heads.size() >= doctorsCheck.size());
		Assert.assertTrue(heads.containsAll(doctorsCheck));
		Assert.assertEquals("Ааxxx", heads.get(0).getFirstName());
		Assert.assertEquals("Яяяzzz", heads.get(heads.size()-1).getFirstName());
		//heads.forEach(System.out::println);
	}

}
