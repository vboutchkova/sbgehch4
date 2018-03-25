/**
 * 
 */
package com.geh.frontend.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.geh.frontend.dtos.HospitalizationEditDto;
import com.geh.frontend.dtos.PatientDetailsEditDto;
import com.geh.mappers.AbstractMapper;
import com.geh.mongodb.morphia.entities.Hospitalization;

/**
 * Converter of Hospitalization entity into DTO for New/Edit hospitalization page
 * 
 * @author Vera Boutchkova
 */
@Controller
public class HospitalizationEntityToEditDtoMapper extends AbstractMapper<Hospitalization, HospitalizationEditDto> {
	
	@Autowired
	private PatientEntityToDetailsDtoMapper patientMapper;
	@Autowired
	private ExaminationEntityToDtoMapper examinationsMapper;
	@Autowired
	private LaboratoryResultEnityToDtoMapper labResultsMapper;
	@Autowired
	private ClinicalResultEntityToDtoMapper clinResultMapper;

	/* (non-Javadoc)
	 * @see com.geh.mappers.IMapper#map(java.lang.Object)
	 */
	@Override
	public HospitalizationEditDto map(Hospitalization src) {
		HospitalizationEditDto dto = new HospitalizationEditDto();
		
		if (src.getId() != null) {
			dto.setId(src.getId().toString());
		}
		
		dto.setAnamnesis(src.getAnamnesis());
		dto.setDiagnoses(src.getDiagnoses());
		dto.setDoctorNames(src.getDoctorNames());
		dto.setDiscussion(src.getDiscussion());
		dto.setEndDate(src.getEndDate());
		dto.setGeneralComment(src.getGeneralComment());
		dto.setName(src.getName());
		dto.setPatientId(src.getPatient().getId().toString());
		dto.setPostTreatment(src.getPostTreatment());
		dto.setSentDate(src.getSentDate());
		dto.setSentDocumentNumber(src.getSentDocumentNumber());
		dto.setSentMedicalCenter(src.getSentMedicalCenter());
		dto.setStartDate(src.getStartDate());
		dto.setStatus(src.getStatus());
		dto.setTreatment(src.getTreatment());
		dto.setWardHeadNames(src.getWardHeadNames());
		
		dto.setExaminations(examinationsMapper.mapList(src.getExaminations()));
		dto.setClinicalResults(clinResultMapper.mapList(src.getClinicalResults()));		
		dto.setLaboratoryResults(labResultsMapper.mapList(src.getLaboratoryResults()));
		
		PatientDetailsEditDto patientDto = patientMapper.map(src.getPatient());
		dto.setPatient(patientDto);
		
		return dto;
	}


}
