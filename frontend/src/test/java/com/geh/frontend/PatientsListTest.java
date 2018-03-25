/**
 * 
 */
package com.geh.frontend;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.geh.mongodb.morphia.dao.PatientDAO;
import com.geh.mongodb.morphia.entities.Address;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Tests authentication with patients list controller
 *
 * @author Vera Boutchkova
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = { com.geh.ApplicationConfiguration.class })
public class PatientsListTest {

	private static final String PASSWORD = "tmpadmin";
	private static final String USERNAME = "admin";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PatientDAO pdao;
	private Set<Patient> patients = new HashSet<>();

	@Test
	public void testLogin() throws Exception {

		ResultActions result = mockMvc.perform(get("/").with(user(USERNAME).password(PASSWORD).roles("USER", "ADMIN")))
				.andDo(print());
		result.andExpect(status().isOk()).andExpect(content().string(containsString("Logged as <span>admin</span>")));
	}

	@Before
	public void setUp() {
		addOnePatient("12345674**", "aaa");
		addOnePatient("12345675?-", "xxx");
		addOnePatient("12345676xx", "yyy");
	}

	@After
	public void cleanUp() {
		patients.forEach(pdao::delete);
		patients.clear();
	}

	/**
	 * Expects to retrieve one patient
	 * @throws Exception
	 */
	@Test
	public void testGetPatient() throws Exception {

		Patient patient = patients.iterator().next();

		ResultActions result = mockMvc.perform(
				get("/patient/" + patient.getId()).with(user(USERNAME).password(PASSWORD).roles("USER", "ADMIN")))
				.andDo(print());
		result.andExpect(status().isOk()).andExpect(content().string(containsString(patient.getFirstName())))
				.andExpect(content().string(containsString(patient.getPersonalNumber())));
	}

	/**
	 * Expects to find all the 3 patients in one page
	 * @throws Exception
	 */
	@Test
	public void testSearchAndListGet() throws Exception {

		String requestUrl = "/patients?personalNumber=1234567&page=0&size=10";
		ResultActions result = mockMvc
				.perform(get(requestUrl).with(user(USERNAME).password(PASSWORD).roles("USER", "ADMIN")))
				.andDo(print());
		result.andExpect(status().isOk()).andExpect(content().string(
				allOf(containsString("12345674**"), containsString("12345675?-"), containsString("12345676xx"))));
	}
	
	/**
	 * Expects to find 1 of the patients in this last page with size 2
	 * @throws Exception
	 */
	@Test
	public void testSearchAndListGet2() throws Exception {
		
		String requestUrl = "/patients?personalNumber=1234567&page=1&size=2";
		ResultActions result = mockMvc
				.perform(get(requestUrl).with(user(USERNAME).password(PASSWORD).roles("USER", "ADMIN")))
				.andDo(print());
		result.andExpect(status().isOk()).andExpect(content().string(
				anyOf(containsString("12345674**"), containsString("12345675?-"), containsString("12345676xx"))));
	}
	
	/**
	 * Expects to find one patient and to redirect to its hospitalizations list
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchAndListGet3() throws Exception {
		
		String requestUrl = "/patients?personalNumber=12345676xx&page=0&size=10";
		ResultActions result = mockMvc
				.perform(get(requestUrl).with(user(USERNAME).password(PASSWORD).roles("USER", "ADMIN")))
				.andDo(print());
		result.andExpect(status().isOk()).andExpect(content().string(
				allOf(containsString("12345676xx"), 
						containsString("<!-- Hospitalizations list -->"), containsString("/newHospitalization/"))));
	}

	/**
	 * Posts the search of patients by personal number
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchAndListPost() throws Exception {
		String requestUrl = "/patients";
		ResultActions result = mockMvc
				.perform(post(requestUrl)
						.with(csrf())
						.with(user(USERNAME).password(PASSWORD).roles("USER", "ADMIN"))
						.param("personalNumber", "1234567")
						.param("page", "1")
						.param("size", "2"))
				.andDo(print());
		result.andExpect(status().isOk()).andExpect(content().string(
				anyOf(containsString("12345674**"), containsString("12345675?-"), containsString("12345676xx"))));
	}

	private void addOnePatient(String personalNumber, String postfix) {
		Patient patient = new Patient();
		patient.setEmail("ali@abv.bg");
		patient.setPersonalNumber(personalNumber);
		patient.setFirstName("XXX " + postfix);

		Address address = new Address();
		address.setTown("София");
		address.setNumber("8");
		patient.setAddress(address);

		pdao.save(patient);
		patients.add(patient);
	}
}
