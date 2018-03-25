package com.geh;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import com.geh.datasource.MongoDataSource;
import com.geh.mongodb.morphia.dao.DoctorDAO;
import com.geh.mongodb.morphia.dao.HospitalizationDAO;
import com.geh.mongodb.morphia.dao.PatientDAO;

/**
 * Source of bean definitions for the application context in the Backend module
 * 
 * @author Vera Boutchkova
 */
@SpringBootApplication
@PropertySource("classpath:backend-application.properties")
public class ApplicationConfigurationBackend {
	
	public static final int DEFAULT_PAGESIZE = 10;

	@Bean
	@ConfigurationProperties(prefix = "app.datasource")
	public MongoDataSource mongoDataSource() {
		return new MongoDataSource();
	}
	
	@Bean
	public HospitalizationDAO hospitalizationDAO(MongoDataSource ds) {
		return new HospitalizationDAO(ds);
	}

	//... more
}