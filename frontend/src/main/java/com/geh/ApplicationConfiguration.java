package com.geh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.geh.export.jasperreports.ReportGenerator;

/**
 * Source of bean definitions for the application context in the Frontend
 * module, which launches the SpringBoot application
 * 
 * @author Vera Boutchkova
 */
@SpringBootApplication
@ComponentScan
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration extends SpringBootServletInitializer {

	public static final int DEFAULT_PAGESIZE = 10;

	@Bean
	@ConfigurationProperties(prefix = "app.jasperreports")
	public ReportGenerator reportGenerator() {
		return new ReportGenerator();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApplicationConfiguration.class);
	}

	/**
	 * Launches the SpringBoot application
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfiguration.class, args);
	}
}