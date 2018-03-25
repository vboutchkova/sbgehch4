/**
 * 
 */
package com.geh.frontend.mappers;

import org.springframework.stereotype.Controller;

import com.geh.frontend.dtos.ExaminationDto;
import com.geh.mongodb.morphia.entities.Examination;

/**
 * Examination mapper
 *
 * @author Vera Boutchkova
 */
@Controller
public class ExaminationEntityToDtoMapper extends EmbeddedEntityToDtoMapper<Examination, ExaminationDto> {

	/* (non-Javadoc)
	 * @see com.geh.frontend.mappers.EmbeddedEntityToDtoMapper#newDestinationObject()
	 */
	@Override
	protected ExaminationDto newDestinationObject() {
		return new ExaminationDto();
	}

	@Override
	protected ExaminationDto mapInternal(Examination sourceObject, ExaminationDto destinationObject) {
		destinationObject = super.mapInternal(sourceObject, destinationObject);
		destinationObject.setDoctorNames(sourceObject.getDoctorNames());
		return destinationObject;
	}

	
}
