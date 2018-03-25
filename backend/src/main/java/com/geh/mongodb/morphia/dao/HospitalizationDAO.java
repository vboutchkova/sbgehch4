/**
 * 
 */
package com.geh.mongodb.morphia.dao;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.geh.datasource.MongoDataSource;
import com.geh.mongodb.morphia.entities.Hospitalization;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * DAO for Hospitalization entity
 * 
 * @author Vera Boutchkova
 */
public class HospitalizationDAO extends BaseEntityDAO<Hospitalization> {

	/**
	 * 
	 */
	private static final String PATIENT_REF_FIELD_NAME = "patient";
	private Datastore datastore;

	public HospitalizationDAO(MongoDataSource ds) {
		super(ds.getDatastore());
		this.datastore = ds.getDatastore();
	}
	
	
	/**
	 * Use slow query db.users.find().skip(pagesize*(n-1)).limit(pagesize)
	 * 
	 * @param patient
	 * @param pageRequest
	 * @return
	 */
	public Page<Hospitalization> findByPatient(Patient patient, PageRequest pageRequest) {
		int pagesize = pageRequest.getPageSize();
		int pagenumber = pageRequest.getPageNumber();

		long totalCount = this.count(PATIENT_REF_FIELD_NAME, patient);

		FindOptions fop = new FindOptions();
		if (pagenumber > 0) {
			fop = fop.skip(pagesize * pagenumber);
		}
		fop = fop.limit(pagesize);
		List<Hospitalization> content = (totalCount <= pagesize * pagenumber) ? Collections.emptyList()
				: (datastore.createQuery(Hospitalization.class)).field(PATIENT_REF_FIELD_NAME).equal(patient).order("-startDate, -createdDate").asList(fop);

		Page<Hospitalization> result = PageableExecutionUtils.getPage(content, pageRequest, () -> totalCount);
		return result;
	}
	
	public Page<Hospitalization> findByPatientAndYear(String patientId, int year, PageRequest pageRequest) {
		int pagesize = pageRequest.getPageSize();
		int pagenumber = pageRequest.getPageNumber();

		FindOptions fop = new FindOptions();
		if (pagenumber > 0) {
			fop = fop.skip(pagesize * pagenumber);
		}
		fop = fop.limit(pagesize);
		
		Calendar gte = Calendar.getInstance();
		gte.clear();
		gte.set(Calendar.YEAR, year);
		Calendar lt = Calendar.getInstance();
		lt.clear();
		lt.set(Calendar.YEAR, year + 1);
		
		Patient patient = new Patient();
		patient.setId(new ObjectId(patientId));
		
		Query<Hospitalization> query = (datastore.createQuery(Hospitalization.class)).field(PATIENT_REF_FIELD_NAME).equal(patient)
				.filter("startDate >=", gte.getTime())
				.filter("startDate <", lt.getTime());
		
		long totalCount = this.count(query);
		
		List<Hospitalization> content = (totalCount <= pagesize * pagenumber) ? Collections.emptyList()
				: query.order("-startDate, -createdDate").asList(fop);

		Page<Hospitalization> result = PageableExecutionUtils.getPage(content, pageRequest, () -> totalCount);
		return result;
	}

	
	public long countUncompletedHospitalizationsForPatientAndYear(String patientId, int year) {
		Calendar gte = Calendar.getInstance();
		gte.clear();
		gte.set(Calendar.YEAR, year);
		Calendar lt = Calendar.getInstance();
		lt.clear();
		lt.set(Calendar.YEAR, year + 1);
		
		Patient patient = new Patient();
		patient.setId(new ObjectId(patientId));
		
		Query<Hospitalization> query = (datastore.createQuery(Hospitalization.class)).field(PATIENT_REF_FIELD_NAME).equal(patient)
				.filter("endDate exists", false)
				.filter("startDate >=", gte.getTime())
				.filter("startDate <", lt.getTime());
		
		long result = this.count(query);
		return result;
	}
}
