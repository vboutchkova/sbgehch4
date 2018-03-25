/**
 * 
 */
package com.geh.export.jasperreports.mappers;

import org.springframework.stereotype.Controller;

import com.geh.export.jasperreports.dtos.ClinicalResultJRDto;
import com.geh.mongodb.morphia.entities.ClinicalResult;


/**
 * Mapper of Clinical Results into Jasper Reports DTO
 *
 * @author Vera Boutchkova
 */
@Controller
public class ClinicalResultEntityToJRDtoMapper extends AbstractJRMapper<ClinicalResult, ClinicalResultJRDto> {

	@Override
	public ClinicalResultJRDto map(ClinicalResult sourceObject) {
		ClinicalResultJRDto dto = new ClinicalResultJRDto();
		dto.setComment(sourceObject.getComment());
		dto.setDate(sourceObject.getDate());
		dto.setName(sourceObject.getName());
		return dto;
	}

}