package com.geh.mongodb.morphia.entities;

import org.hibernate.validator.constraints.Email;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

/**
 * Patient entity
 * @author Vera Boutchkova
 */
@Entity(value = "patient")
public class Patient extends BaseEntity {

	@Embedded
	private Address address;

	@Email
	private String email;

	private String phone;
	private String personalNumber;
	private String firstName;
	private String middleName;
	private String name;
	private String occupation;
	
	@Reference(lazy = true)
	private Hospitalization lastHospitalization;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Hospitalization getLastHospitalization() {
		return lastHospitalization;
	}

	public void setLastHospitalization(Hospitalization lastHospitalization) {
		this.lastHospitalization = lastHospitalization;
	}

	@Override
	public String toString() {
		return "Patient [address=" + address + ", email=" + email + ", phone=" + phone + ", personalNumber="
				+ personalNumber + ", firstName=" + firstName + ", middleName=" + middleName + ", name=" + name
				+ ", occupation=" + occupation + ", id=" + id + "]";
	}

	
}
