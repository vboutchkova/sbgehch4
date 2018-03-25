/**
 * 
 */
package com.geh.frontend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Tests that a get request is redirected to login page and login error if authentication fails
 *
 * @author Vera Boutchkova
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = { com.geh.ApplicationConfiguration.class })
public class PatientsListNegativeTest {
	private static final String PERSONAL_NUMBER = "8501072948";
	
	@Autowired
	private MockMvc mockMvc;

	/**
	 * POST requests to /patients should be unauthorized 403 because "Could not
	 * verify the provided CSRF token because your session was not found."
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCSRF1() throws Exception {
		ResultActions request = this.mockMvc.perform(
				post("/patients").param("personalNumber", PERSONAL_NUMBER).param("page", "0").param("size", "10"))
				.andDo(print());
		request.andExpect(status().is(403));
	}

	/**
	 * GET request to /patients/{objectId} should be redirected to login page with
	 * status code 302 Found and Location header.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCSRF2() throws Exception {
		ResultActions request = this.mockMvc.perform(get("/patients/5a8c771ce5121622d48333ed")).andDo(print());
		request.andExpect(status().is(302));
		request.andExpect(header().string("Location", CoreMatchers.endsWith("/login")));
	}
	
	/**
	 * Tests the redirect to login error in case of failed authentication
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCSRF3() throws Exception {
		FormLoginRequestBuilder builder = SecurityMockMvcRequestBuilders.formLogin("/patients").user("xxx").password("yyy")
				.loginProcessingUrl("/login");
		ResultActions result = this.mockMvc.perform(builder).andDo(print());
		result.andExpect(status().is(302)).andExpect(redirectedUrl("/login?error"));
	}
}
