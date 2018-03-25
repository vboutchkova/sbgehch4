/**
 * 
 */
package com.geh.export.jasperreports.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geh.export.jasperreports.mappers.HospitalizationEntityToJRDtoMapper;
import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.dao.PatientDAO;
import com.geh.mongodb.morphia.entities.Address;
import com.geh.mongodb.morphia.entities.ClinicalResult;
import com.geh.mongodb.morphia.entities.Examination;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.LaboratoryResult;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Tests JSON serialization of HospitalizationJRDto used for generating the PDF file
 * 
 * @author Vera Boutchkova
 */
@JsonTest
@RunWith(SpringRunner.class)
@SpringBootTest(classes= {com.geh.ApplicationConfiguration.class})
public class HospitalizationJRDtoTest {
	@Autowired
	JacksonTester<HospitalizationJRDto> json;

	private long fixedDate1 = 1521659805487l;
	private long fixedDate2 = 1521658532893l;
	private long fixedDate3 = 1521665805111l;
	
	@Autowired
	private PatientDAO pdao;
	@Autowired
	private HospitalizationDAO hdao;
	@Autowired
	private HospitalizationEntityToJRDtoMapper mapper;

	private Set<Hospitalization> hospitalizations = new HashSet<>();
	private Set<Patient> patients = new HashSet<>();

	@Test
	public void someTest() {
		HospitalizationJRDto hospDto = mapper.map(hospitalizations.iterator().next());
		try {
			JsonContent<HospitalizationJRDto> result = json.write(hospDto);
			//System.out.println("HospitalizationJRDto in JSON format:\r\n\t" + result);
			
			assertThat(result).isEqualToJson("HospitalizationJRDto.json");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() {
		ObjectMapper objectMapper = new ObjectMapper();
		JacksonTester.initFields(this, objectMapper);

		addOnePatient(4, null);
	}

	private String addOnePatient(int num, Set<Hospitalization> hospitalizationsCheck) {

		Patient patient = new Patient();
		patient.setEmail("ali@abv.bg");
		patient.setPersonalNumber("1234567890");
		patient.setFirstName("Юри " + num);
		patient.setName("Панайотов");
		patient.setCreatedBy("Пешо");

		Key<Patient> result = pdao.save(patient);

		Address address = new Address();
		address.setCountry("България");
		address.setTown("София");
		address.setNumber("8");
		address.setPostcode("1000");
		address.setStreet("Бул. България");

		patient.setAddress(address);

		pdao.save(patient);

		Hospitalization hospitalization = new Hospitalization();
		
		hospitalization.setName("Completed Test hospitalization 3333_" + (num * 3 + 1));
		hospitalization.setStartDate(new Date(fixedDate1));
		hospitalization.setEndDate(new Date(fixedDate2));
		hospitalization.setEpicrisisIssued(Boolean.TRUE);
		hospitalization.setAnamnesis("Някаква анамнеза...");
		hospitalization.setSentDocumentNumber("548954+6454/10.11.2015");
		hospitalization.setSentDate(new Date(fixedDate3));
		hospitalization.setSentMedicalCenter("16 MC Даме Груев 24");
		ClinicalResult clinicalResult = new ClinicalResult();
		clinicalResult.setName("Ехография");
		clinicalResult.setDate(new Date(fixedDate1));
		clinicalResult.setComment(
				"JasperReports est un outil open source puissant pour la génération des états avec du contenu riche, soit pour présentation à l'écran, impression papier ou stockage dans des fichiers en format PDF, HTML, XLS, CSV ou XML.");
		hospitalization.addClinicalResult(clinicalResult);
		clinicalResult = new ClinicalResult();
		clinicalResult.setName("Колоноскопия");
		clinicalResult.setDate(new Date(fixedDate2));
		clinicalResult.setComment(
				"JasperReports é uma poderosa ferramenta Open Source de criação de relatórios que tem a capacidade de gerar conteúdos complexos para o ecran, para a impressora ou em ficheiros PDF, HTML, XLS, CSV e XML.");
		hospitalization.addClinicalResult(clinicalResult);

		LaboratoryResult lr = new LaboratoryResult();
		lr.setComment(
				"Лабораторни резултати 1л................................ Keifdkjkf fkdkdk 309l,l kdljkd fd \r\nllkdkdjflkdsjk\r\nkkdsfs.....");
		lr.setDate(new Date(fixedDate3));
		hospitalization.addLaboratoryResults(lr);
		lr = new LaboratoryResult();
		lr.setComment("Лабораторни резултати номер 2 kdjflkdsjk\r\nkkdsfs.....");
		lr.setDate(new Date(fixedDate1));
		hospitalization.addLaboratoryResults(lr);
		lr = new LaboratoryResult();
		lr.setComment(
				"Лабораторни резултати номер 3 Keifdkjkf fkdkdk 309l,l kdljkd fd \r\nllkdkdjflkdsjk\r\nkkdsfs.....");
		lr.setDate(new Date(fixedDate2));
		hospitalization.addLaboratoryResults(lr);

		Examination ex = new Examination();
		ex.setComment("Прелед 1 ван тонятонятж вжотвя 4634 жтоавжт ятжоа нжотвожт жоа нжотжоав жоа\r\n"
				+ "Year: If the formatter's Calendar is the Gregorian calendar, the following rules are applied.\r\n"
				+ "For formatting, if the number of pattern letters is 2, the year is truncated to 2 digits; otherwise it is interpreted as a number.\r\n"
				+ "For parsing, if the number of pattern letters is more than 2, the year is interpreted literally, regardless of the number of digits. So using the pattern \"MM/dd/yyyy\", \"01/11/12\" parses to Jan 11, 12 A.D.\r\n"
				+ "For parsing with the abbreviated year pattern (\"y\" or \"yy\"), SimpleDateFormat must interpret the abbreviated year relative to some century. It does this by adjusting dates to be within 80 years before and 20 years after the time the SimpleDateFormat instance is created. For example, using a pattern of \"MM/dd/yy\" and a SimpleDateFormat instance created on Jan 1, 1997, the string \"01/11/12\" would be interpreted as Jan 11, 2012 while the string \"05/04/64\" would be interpreted as May 4, 1964. During parsing, only strings consisting of exactly two digits, as defined by Character.isDigit(char), will be parsed into the default century. Any other numeric string, such as a one digit string, a three or more digit string, or a two digit string that isn't all digits (for example, \"-1\"), is interpreted literally. So \"01/02/3\" or \"01/02/003\" are parsed, using the same pattern, as Jan 2, 3 AD. Likewise, \"01/02/-3\" is parsed as Jan 2, 4 BC.");
		ex.setDate(new Date(fixedDate3));
		hospitalization.addExaminations(ex);
		ex = new Examination();
		ex.setComment("VAT	VAT - Service Fee - Ref ID 167528683	($3.20) $60.80\r\n"
				+ "Fixed Price	Invoice for Consultation for YYY: Milestone 1 - XXX	$80.00 $80.00"
				+ "Allocates a Date object and initializes it so that it represents the time at which it was allocated, measured to the nearest millisecond."
				+ "Allocates a Date object and initializes it to represent the specified number of milliseconds since the standard base time known as \"the epoch\", namely January 1, 1970, 00:00:00 GMT.");
		ex.setDate(new Date(fixedDate1));
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
		for (Patient p : patients) {
			pdao.delete(p);
		}
		patients.clear();
		for (Hospitalization h : hospitalizations) {
			hdao.delete(h);
		}
		hospitalizations.clear();
	}
	
}
