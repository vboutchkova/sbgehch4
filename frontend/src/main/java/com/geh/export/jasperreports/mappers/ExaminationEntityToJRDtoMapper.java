/**
 * 
 */
package com.geh.export.jasperreports.mappers;

import org.springframework.stereotype.Controller;

import com.geh.export.jasperreports.dtos.ExaminationJRDto;
import com.geh.mongodb.morphia.entities.Examination;

/**
 * Mapper of Examinations into Jasper Reports DTO
 *
 * @author Vera Boutchkova
 */
@Controller
public class ExaminationEntityToJRDtoMapper extends AbstractJRMapper<Examination, ExaminationJRDto> {

	@Override
	public ExaminationJRDto map(Examination sourceObject) {
		ExaminationJRDto dto = new ExaminationJRDto();
		dto.setComment(sourceObject.getComment());
		dto.setDate(sourceObject.getDate());
		dto.setDoctorNames(sourceObject.getDoctorNames());
		return dto;
	}

}
