/**
 * 
 */
package com.geh.mongodb.morphia.entities;

import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Common parts of embedded list element
 * 
 * @author Vera Boutchkova
 */
@Embedded
public class EmbeddedListElement {
	
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
