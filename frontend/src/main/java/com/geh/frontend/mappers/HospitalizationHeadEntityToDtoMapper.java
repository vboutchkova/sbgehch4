/**
 * 
 */
package com.geh.frontend.mappers;

import org.springframework.stereotype.Controller;

import com.geh.frontend.dtos.HospitalizationListDto;
import com.geh.mongodb.morphia.entities.Hospitalization;

/**
 * Hospitalization mapper
 *
 * @author Vera Boutchkova
 */
@Controller
public class HospitalizationHeadEntityToDtoMapper
		extends BaseEntityToBaseDtoMapper<Hospitalization, HospitalizationListDto> {

	@Override
	protected HospitalizationListDto newDestinationObject() {
		return new HospitalizationListDto();
	}


	@Override
	protected void mapInternal(Hospitalization sourceObject, HospitalizationListDto result) {
		super.mapInternal(sourceObject, result);
		result.setEndDate(sourceObject.getEndDate());
		result.setEpicrisisIssued(sourceObject.getEpicrisisIssued());
		result.setStartDate(sourceObject.getStartDate());
	}	
}
