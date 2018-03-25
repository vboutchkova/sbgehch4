package com.geh;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.geh.frontend.controllers.SearchPatientMvcController;
import com.geh.mongodb.morphia.dao.PatientDAO;

/**
 * Tests that one of the controllers is injected
 *
 * @author Vera Boutchkova
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= {com.geh.ApplicationConfiguration.class})
public class SmokeTest {
	
	@Autowired
	private SearchPatientMvcController controller;
	@Autowired
	private PatientDAO pdao;

	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
		assertThat(pdao).isNotNull();
	}
}
