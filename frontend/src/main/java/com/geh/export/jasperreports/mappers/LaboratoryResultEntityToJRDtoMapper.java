/**
 * 
 */
package com.geh.export.jasperreports.mappers;

import org.springframework.stereotype.Controller;

import com.geh.export.jasperreports.dtos.LaboratoryResultJRDto;
import com.geh.mongodb.morphia.entities.LaboratoryResult;

/**
 * Mapper of Laboratory Results into Jasper Reports DTO
 *
 * @author Vera Boutchkova
 */
@Controller
public class LaboratoryResultEntityToJRDtoMapper
		extends AbstractJRMapper<LaboratoryResult, LaboratoryResultJRDto> {
	
	@Override
	public LaboratoryResultJRDto map(LaboratoryResult sourceObject) {
		LaboratoryResultJRDto dto = new LaboratoryResultJRDto();
		dto.setComment(sourceObject.getComment());
		dto.setDate(sourceObject.getDate());
		return dto;
	}

}
