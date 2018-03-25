/**
 * 
 */
package com.geh.frontend.mappers;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.stereotype.Controller;

import com.geh.frontend.dtos.HospitalizationEditDto;
import com.geh.mongodb.morphia.entities.ClinicalResult;
import com.geh.mongodb.morphia.entities.Examination;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.LaboratoryResult;

/**
 * Hospitalization mapper
 *
 * @author Vera Boutchkova
 */
@Controller
public class HospitalizationDtoNewToEntityMapper
		extends AbstractDtoToEntityMapper<HospitalizationEditDto, Hospitalization> {

	@Override
	public Hospitalization map(HospitalizationEditDto sourceObject, Hospitalization destinationObject) {
		if (destinationObject == null) {
			destinationObject = new Hospitalization();
		}

		applyNotNull(sourceObject::getAnamnesis, destinationObject::setAnamnesis);

		if (sourceObject.getClinicalResultComment() != null || sourceObject.getClinicalResultDate() != null
				|| sourceObject.getClinicalResultName() != null) {
			ClinicalResult cr = new ClinicalResult();
			cr.setComment(sourceObject.getClinicalResultComment());
			cr.setDate(sourceObject.getClinicalResultDate());
			cr.setName(sourceObject.getClinicalResultName());
			destinationObject.addClinicalResult(cr);
		}

		applyNotNull(sourceObject::getDiagnoses, destinationObject::setDiagnoses);
		applyNotNull(sourceObject::getDiscussion, destinationObject::setDiscussion);
		applyNotNull(sourceObject::getDoctorNames, destinationObject::setDoctorNames);
		applyNotNull(sourceObject::getEndDate, destinationObject::setEndDate);

		if (sourceObject.getExaminationComment() != null || sourceObject.getExaminationDate() != null
				|| sourceObject.getExaminationDoctorNames() != null) {
			Examination cr = new Examination();
			cr.setDate(sourceObject.getExaminationDate());
			applyNotNull(sourceObject::getExaminationComment, cr::setComment);
			applyNotNull(sourceObject::getExaminationDoctorNames, cr::setDoctorNames);
			destinationObject.addExaminations(cr);
		}
		
		applyNotNull(sourceObject::getGeneralComment, destinationObject::setGeneralComment);

		if (sourceObject.getLabResultComment() != null || sourceObject.getLabResultDate() != null
				|| sourceObject.getLabResultName() != null) {
			LaboratoryResult cr = new LaboratoryResult();
			cr.setComment(sourceObject.getLabResultComment());
			cr.setDate(sourceObject.getLabResultDate());
			cr.setName(sourceObject.getLabResultName());
			destinationObject.addLaboratoryResults(cr);
		}

		applyNotNull(sourceObject::getName, destinationObject::setName);

		applyNotNull(sourceObject::getPostTreatment, destinationObject::setPostTreatment);
		applyNotNull(sourceObject::getSentDate, destinationObject::setSentDate);
		applyNotNull(sourceObject::getSentDocumentNumber, destinationObject::setSentDocumentNumber);
		applyNotNull(sourceObject::getSentMedicalCenter, destinationObject::setSentMedicalCenter);
		
		applyNotNull(sourceObject::getStartDate, destinationObject::setStartDate);
		applyNotNull(sourceObject::getStatus, destinationObject::setStatus);
		applyNotNull(sourceObject::getTreatment, destinationObject::setTreatment);
		applyNotNull(sourceObject::getWardHeadNames, destinationObject::setWardHeadNames);
		
		return destinationObject;
	}

	private static <T> void applyNotNull(Supplier<T> supplier, Consumer<T> consumer) {
		
		if ( supplier.get() != null) {
			consumer.accept(supplier.get());
		}
	}

}
