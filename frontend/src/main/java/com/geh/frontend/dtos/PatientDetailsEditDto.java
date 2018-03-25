/**
 * 
 */
package com.geh.frontend.dtos;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * DTO for displaying patient's details in the web frontend
 *
 * @author Vera Boutchkova
 */
public class PatientDetailsEditDto {

	private String id;
	
	@Valid
	private HospitalizationListDto lastHospitalization;
	
	private AddressDto address;

	@Email
	private String email;
	@Size(max=20)
	private String phone;
	
	@NotBlank
	@Size(min=10, max=10)
	private String personalNumber;
	@NotBlank
	@Size(max=30)
	private String firstName;
	@Size(max=30)
	private String middleName;
	@NotBlank
	@Size(max=40)
	private String name;
	@Size(max=30)
	private String occupation;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AddressDto getAddress() {
		return address;
	}
	public void setAddress(AddressDto address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
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
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public HospitalizationListDto getLastHospitalization() {
		return lastHospitalization;
	}
	public void setLastHospitalization(HospitalizationListDto lastHospitalization) {
		this.lastHospitalization = lastHospitalization;
	}
	@Override
	public String toString() {
		return "PatientDetailsDto [id=" + id + ", lastHospitalization=" + lastHospitalization + ", address=" + address
				+ ", email=" + email + ", phone=" + phone + ", personalNumber=" + personalNumber + ", firstName="
				+ firstName + ", middleName=" + middleName + ", name=" + name + ", occupation=" + occupation + "]";
	}
	
	
}
