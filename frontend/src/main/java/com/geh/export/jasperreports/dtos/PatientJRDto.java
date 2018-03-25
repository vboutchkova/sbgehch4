/**
 * 
 */
package com.geh.export.jasperreports.dtos;

/**
 * DTO for displaying Patient in Jasper Report documents
 *
 * @author Vera Boutchkova
 */
public class PatientJRDto extends BaseDto {

	private String personalNumber;
	private String firstName;
	private String middleName;
	private String name;
	private AddressJRDto address;
	
	public String getPersonalNumber() {
		return personalNumber;
	}
	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AddressJRDto getAddress() {
		return address;
	}
	public void setAddress(AddressJRDto address) {
		this.address = address;
	}


	
}
