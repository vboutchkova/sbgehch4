package com.geh;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.dao.PatientDAO;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Tests Web Request to the SpringBoot application
 *
 * @author Vera Boutchkova
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes= {com.geh.ApplicationConfiguration.class})
public class ApplicationTest {

	private static final String PERSONAL_NUMBER = "4612076882";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PatientDAO pdao;
	@Autowired
	private HospitalizationDAO hdao;
	
	private Hospitalization hospitalization;
	private Patient patient;

	@Before
	public void setUp() {
		addOnePatient(19);
	}

	private void addOnePatient(int num) {
		hospitalization = new Hospitalization();
		hospitalization.setName("test hospitalization " + num);
		hospitalization.setStartDate(new Date());
		hdao.save(hospitalization);

		patient = new Patient();
		patient.setEmail("eho@abv.bg");
		patient.setPersonalNumber(PERSONAL_NUMBER);
		patient.setFirstName("Chocho " + num);
		patient.setName("Delibaltov");
		patient.setCreatedBy("Admin");
		pdao.save(patient);

		patient.setLastHospitalization(hospitalization);
		pdao.save(patient);
	}

	@After
	public void cleanUp() {
		if (patient != null) {
			pdao.delete(patient);
			patient = null;
		}
		if (hospitalization != null) {
			hdao.delete(hospitalization);
			hospitalization = null;
		}
	}

	@Test
	public void requestByPersonalNumberNegativeTest() throws Exception {
		
		this.mockMvc.perform(get("/search/findall/personalNumber/keyword/" + PERSONAL_NUMBER + "?page=0&size=10"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/login"));
	}
}
