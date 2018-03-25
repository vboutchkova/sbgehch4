/**
 * 
 */
package com.geh.frontend.mappers;

import org.springframework.stereotype.Controller;

import com.geh.frontend.dtos.PatientDetailsEditDto;
import com.geh.mongodb.morphia.entities.Address;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Patient Details mapper
 *
 * @author Vera Boutchkova
 */
@Controller
public class PatientDetailsDtoToEntityMapper extends AbstractDtoToEntityMapper<PatientDetailsEditDto, Patient> {


	/* (non-Javadoc)
	 * @see com.geh.frontend.mappers.AbstractDtoToEntityMapper#map(java.lang.Object, com.geh.mongodb.morphia.entities.BaseEntity)
	 */
	@Override
	public Patient map(PatientDetailsEditDto sourceObject, Patient destinationObject) {
		if (destinationObject == null) {
			destinationObject = new Patient();
		}
		if (sourceObject.getAddress() != null) {
			Address address = new Address();
			address.setCountry(sourceObject.getAddress().getCountry());
			address.setNumber(sourceObject.getAddress().getNumber());
			address.setPostcode(sourceObject.getAddress().getPostcode());
			address.setStreet(sourceObject.getAddress().getStreet());
			address.setTown(sourceObject.getAddress().getTown());
			destinationObject.setAddress(address);
		}
		destinationObject.setEmail(sourceObject.getEmail());
		destinationObject.setFirstName(sourceObject.getFirstName());
		destinationObject.setMiddleName(sourceObject.getMiddleName());
		destinationObject.setName(sourceObject.getName());
		destinationObject.setOccupation(sourceObject.getOccupation());
		destinationObject.setPersonalNumber(sourceObject.getPersonalNumber());
		destinationObject.setPhone(sourceObject.getPhone());
		
		return destinationObject;
	}


}
