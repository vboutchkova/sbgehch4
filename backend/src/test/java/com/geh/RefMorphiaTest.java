package com.geh;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.geh.datasource.MongoDataSource;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Tests the Mongo datasource
 *
 * @author Vera Boutchkova
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={com.geh.ApplicationConfigurationBackend.class})
public class RefMorphiaTest {

	@Autowired
	private MongoDataSource dataSouce;

	private Hospitalization hospitalization;
	private Patient patient;

	@Test
	public void test01() throws Exception {
		hospitalization = new Hospitalization();
		hospitalization.setStartDate(new Date());
		hospitalization.setName("test hospitalization 1");
		dataSouce.getDatastore().save(hospitalization);

		patient = new Patient();
		patient.setEmail("eho@abv.bg");
		patient.setPersonalNumber("2912076224");
		patient.setFirstName("Ivan");
		patient.setName("Ivanov");
		dataSouce.getDatastore().save(patient);

		patient.setLastHospitalization(hospitalization);
		dataSouce.getDatastore().save(patient);

		List<Patient> patients = dataSouce.getDatastore().find(Patient.class).asList();
		Assert.assertTrue(patients.size() >= 1);
	}

	@Test
	public void test02() throws Exception {
		List<Patient> parentList = dataSouce.getDatastore().find(Patient.class).asList();
		Assert.assertNotNull(parentList);
	}

	@After
	public void cleanUp() {
		if (patient != null) {
			dataSouce.getDatastore().delete(patient);
			patient = null;
		}
		if (hospitalization != null) {
			dataSouce.getDatastore().delete(hospitalization);
			hospitalization = null;
		}
	}

}
