/**
 * 
 */
package com.geh.export.jasperreports.dtos;

import java.util.Date;

/**
 * DTO for displaying Examinations in Jasper Report documents
 *
 * @author Vera Boutchkova
 */
public class ExaminationJRDto {

	private String doctorNames;
	
	private Date date;
	private String comment;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDoctorNames() {
		return doctorNames;
	}
	public void setDoctorNames(String doctorNames) {
		this.doctorNames = doctorNames;
	}
}
