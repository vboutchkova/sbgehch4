/**
 * 
 */
package com.geh.export.jasperreports.mappers;

import java.util.function.Supplier;

import org.springframework.stereotype.Controller;

import com.geh.export.jasperreports.dtos.AddressJRDto;
import com.geh.export.jasperreports.dtos.PatientJRDto;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Mapper of Patient Entity into Jasper Reports DTO
 *
 * @author Vera Boutchkova
 */
@Controller
public class PatientEntityToJRDtoMapper extends AbstractJRMapper<Patient, PatientJRDto> {

	/* (non-Javadoc)
	 * @see com.geh.frontend.mappers.BaseEntityToDtoMapper#configure(com.geh.mongodb.morphia.entities.BaseEntity)
	 */
	@Override
	public PatientJRDto map(Patient patient) {
		PatientJRDto result = new PatientJRDto();
		
		AddressJRDto addressDto = new AddressJRDto();
		addressDto.setCountry(getAddressFieldForEpicrisis(patient, patient.getAddress()::getCountry));
		addressDto.setNumber(getAddressFieldForEpicrisis(patient, patient.getAddress()::getNumber));
		addressDto.setPostcode(getAddressFieldForEpicrisis(patient, patient.getAddress()::getPostcode));
		addressDto.setStreet(getAddressFieldForEpicrisis(patient, patient.getAddress()::getStreet));
		addressDto.setTown(getAddressFieldForEpicrisis(patient, patient.getAddress()::getTown));
		result.setAddress(addressDto);

		result.setFirstName(getFieldValueNotNull(patient::getFirstName));
		result.setMiddleName(getFieldValueNotNull(patient::getMiddleName));
		result.setName(getFieldValueNotNull(patient::getName));
		result.setPersonalNumber(getFieldValueNotNull(patient::getPersonalNumber));

		return result;
	}

	private String getAddressFieldForEpicrisis(Patient sourceObject, Supplier<String> supplier) {
		return sourceObject.getAddress() != null && supplier != null ? supplier.get() : "";
	}

}
