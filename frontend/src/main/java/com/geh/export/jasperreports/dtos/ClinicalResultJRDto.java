/**
 * 
 */
package com.geh.export.jasperreports.dtos;

import java.util.Date;

/**
 * DTO for displaying Clinical Results in Jasper Report documents
 *
 * @author Vera Boutchkova
 */
public class ClinicalResultJRDto {
	private Date date;
	private String name;
	private String comment;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
