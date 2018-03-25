/**
 * 
 */
package com.geh.frontend.dtos;

import javax.validation.constraints.Size;

/**
 * Patient's Address DTO for Web Frontend
 *
 * @author Vera Boutchkova
 */
public class AddressDto {
	
	@Size(max=10)
	private String number;
	@Size(max=30)
	private String street;
	@Size(max=30)
	private String town;
	@Size(max=20)
	private String postcode;
	@Size(max=30)
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
