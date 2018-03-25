/**
 * 
 */
package com.geh.frontend.dtos;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Common DTO for List Element for web frontend
 *
 * @author Vera Boutchkova
 */
public class EmbeddedListElementDto {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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
}
