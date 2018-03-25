/**
 * 
 */
package com.geh.mongodb.morphia.entities;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Examination extends EmbeddedListElement {

	private String doctorNames;
	
	public String getDoctorNames() {
		return doctorNames;
	}
	public void setDoctorNames(String doctorNames) {
		this.doctorNames = doctorNames;
	}
	
}
