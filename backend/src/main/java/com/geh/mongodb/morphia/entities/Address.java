package com.geh.mongodb.morphia.entities;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Address embedded document
 * 
 * @author Vera Boutchkova
 */
@Embedded
public class Address {

	private String number;
	private String street;
	private String town;
	private String postcode;
	private String country;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}