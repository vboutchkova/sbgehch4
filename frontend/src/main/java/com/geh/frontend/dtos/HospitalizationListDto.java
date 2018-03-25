/**
 * 
 */
package com.geh.frontend.dtos;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * DTO for displaying hospitalization in a list in the web frontend
 *
 * @author Vera Boutchkova
 */
public class HospitalizationListDto extends BaseDto {
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	private Boolean epicrisisIssued;
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Boolean getEpicrisisIssued() {
		return epicrisisIssued;
	}
	public void setEpicrisisIssued(Boolean epicrisisIssued) {
		this.epicrisisIssued = epicrisisIssued;
	}
	
	
}
