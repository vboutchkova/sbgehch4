/**
 * 
 */
package com.geh.frontend.dtos;

/**
 * Clinical Results DTO for web frontend
 *
 * @author Vera Boutchkova
 */
public class ClinicalResultDto extends EmbeddedListElementDto {

	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
