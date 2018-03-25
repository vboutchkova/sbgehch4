/**
 * 
 */
package com.geh.mongodb.morphia.entities;

import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Embedded document for generated PDF file
 *
 * @author Vera Boutchkova
 */
@Embedded
public class ReportFile {
	
	private byte[] fileContent;
	private String fileName;
	private Date generatedDate;
	private String generatedBy;
	
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getGeneratedDate() {
		return generatedDate;
	}
	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}
	public String getGeneratedBy() {
		return generatedBy;
	}
	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}
	
	
}
