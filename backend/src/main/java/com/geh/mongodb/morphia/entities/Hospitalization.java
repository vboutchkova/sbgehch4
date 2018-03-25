package com.geh.mongodb.morphia.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

/**
 * Hospitalization Entity
 * 
 * @author Vera Boutchkova
 */
@Entity(value = "hospitalization")
public class Hospitalization extends BaseEntity {

	private String doctorNames;
	private String wardHeadNames;
	
	private String name;
	
	private Date startDate;
	private Date endDate;
	
	private String sentDocumentNumber;
	private String sentMedicalCenter;
	private Date sentDate;
	
	private String anamnesis;
	private String status;
	private String diagnoses;
	private String treatment;
	private String postTreatment;
	private String discussion;
	
	private String generalComment;
	
	@Embedded
	private ReportFile epicrisisFile;
	private Boolean epicrisisIssued;
	
	@Reference
	private Patient patient;

	private List<Examination> examinations = new ArrayList<>();
	private List<ClinicalResult> clinicalResults = new ArrayList<>();
	private List<LaboratoryResult> laboratoryResults = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAnamnesis() {
		return anamnesis;
	}

	public void setAnamnesis(String anamnesis) {
		this.anamnesis = anamnesis;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDiagnoses() {
		return diagnoses;
	}

	public void setDiagnoses(String diagnoses) {
		this.diagnoses = diagnoses;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getPostTreatment() {
		return postTreatment;
	}

	public void setPostTreatment(String postTreatment) {
		this.postTreatment = postTreatment;
	}

	public List<Examination> getExaminations() {
		return examinations;
	}

	public void setExaminations(List<Examination> examinations) {
		this.examinations = examinations;
	}
	
	public void addExaminations(Examination examination) {
		this.examinations.add(examination);
	}

	public List<ClinicalResult> getClinicalResults() {
		return clinicalResults;
	}

	public void setClinicalResults(List<ClinicalResult> clinicalResults) {
		this.clinicalResults = clinicalResults;
	}
	
	public void addClinicalResult(ClinicalResult clinicalResults) {
		this.clinicalResults.add(clinicalResults);
	}

	public List<LaboratoryResult> getLaboratoryResults() {
		return laboratoryResults;
	}

	public void setLaboratoryResults(List<LaboratoryResult> laboratoryResults) {
		this.laboratoryResults = laboratoryResults;
	}
	
	public void addLaboratoryResults(LaboratoryResult laboratoryResults) {
		this.laboratoryResults.add(laboratoryResults);
	}

	public Boolean getEpicrisisIssued() {
		return epicrisisIssued;
	}

	public void setEpicrisisIssued(Boolean epicrisisIssued) {
		this.epicrisisIssued = epicrisisIssued;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getSentDocumentNumber() {
		return sentDocumentNumber;
	}

	public void setSentDocumentNumber(String sentDocumentNumber) {
		this.sentDocumentNumber = sentDocumentNumber;
	}

	public String getSentMedicalCenter() {
		return sentMedicalCenter;
	}

	public void setSentMedicalCenter(String sentMedicalCenter) {
		this.sentMedicalCenter = sentMedicalCenter;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public ReportFile getEpicrisisFile() {
		return epicrisisFile;
	}

	public void setEpicrisisFile(ReportFile epicrisisFile) {
		this.epicrisisFile = epicrisisFile;
	}

	public String getGeneralComment() {
		return generalComment;
	}

	public void setGeneralComment(String generalComment) {
		this.generalComment = generalComment;
	}
	

	public String getDiscussion() {
		return discussion;
	}

	public void setDiscussion(String discussion) {
		this.discussion = discussion;
	}

	public String getDoctorNames() {
		return doctorNames;
	}

	public void setDoctorNames(String doctorNames) {
		this.doctorNames = doctorNames;
	}

	public String getWardHeadNames() {
		return wardHeadNames;
	}

	public void setWardHeadNames(String wardHeadNames) {
		this.wardHeadNames = wardHeadNames;
	}


}