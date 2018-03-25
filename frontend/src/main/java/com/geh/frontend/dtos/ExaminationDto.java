/**
 * 
 */
package com.geh.frontend.dtos;

/**
 * Examination DTO for web frontend
 *
 * @author Vera Boutchkova
 */
public class ExaminationDto extends EmbeddedListElementDto {
	private String doctorNames;

	public String getDoctorNames() {
		return doctorNames;
	}

	public void setDoctorNames(String doctorNames) {
		this.doctorNames = doctorNames;
	}
	
	
}
