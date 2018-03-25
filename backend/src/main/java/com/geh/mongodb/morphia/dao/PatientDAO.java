package com.geh.mongodb.morphia.dao;

import java.util.Collections;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.geh.datasource.MongoDataSource;
import com.geh.mongodb.morphia.entities.Patient;

/**
 * DAO for Patient entity
 * 
 * @author Vera Boutchkova
 */
public class PatientDAO extends BaseEntityDAO<Patient> {

	private static final String PERSONAL_NUMBER = "personalNumber";
	
	private Datastore datastore;

	public PatientDAO(MongoDataSource ds) {
		super(ds.getDatastore());
		this.datastore = ds.getDatastore();
	}

	/**
	 * Finds by personal Number
	 * 
	 * @param keyword
	 * @param pageRequest
	 * @return
	 */
	public Page<Patient> findByPersonalNumberAndPage(String keyword, PageRequest pageRequest) {
		int pagesize = pageRequest.getPageSize();
		int pagenumber = pageRequest.getPageNumber();
		long totalCount = this.count(PERSONAL_NUMBER, keyword);

		FindOptions fop = new FindOptions();
		if (pagenumber > 0) {
			fop = fop.skip(pagesize * pagenumber);
		}
		fop = fop.limit(pagesize);
		List<Patient> content = (totalCount <= pagesize * pagenumber) ? Collections.emptyList()
				: (datastore.createQuery(Patient.class)).field(PERSONAL_NUMBER).equal(keyword).order("-createdDate").asList(fop);

		Page<Patient> result = PageableExecutionUtils.getPage(content, pageRequest, () -> totalCount);
		return result;
	}

	//TODO: more
}