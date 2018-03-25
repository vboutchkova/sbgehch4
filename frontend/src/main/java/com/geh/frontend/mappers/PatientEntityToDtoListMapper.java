/**
 * 
 */
package com.geh.frontend.mappers;

import org.springframework.stereotype.Controller;

import com.geh.frontend.dtos.PatientListDto;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * Patients mapper
 *
 * @author Vera Boutchkova
 */
@Controller
public class PatientEntityToDtoListMapper extends BaseEntityToBaseDtoMapper<Patient, PatientListDto> {
	

	@Override
	protected PatientListDto newDestinationObject() {
		return new PatientListDto();
	}

	@Override
	protected void mapInternal(Patient sourceObject, PatientListDto result) {
		super.mapInternal(sourceObject, result);
		result.setCity(sourceObject.getAddress() != null ? sourceObject.getAddress().getTown() : "NA");
		result.setFirstName(sourceObject.getFirstName());
		result.setMiddleName(sourceObject.getMiddleName());
		result.setName(sourceObject.getName());
		result.setPersonalNumber(sourceObject.getPersonalNumber());

		if (sourceObject.getLastHospitalization() != null) {
			result.setLastHospitalization(sourceObject.getLastHospitalization().getStartDate());
		}
	}	
}
