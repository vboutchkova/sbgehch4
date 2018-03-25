/**
 * 
 */
package com.geh.mongodb.morphia.dao;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.geh.datasource.MongoDataSource;
import com.geh.mongodb.morphia.entities.Doctor;
import com.geh.mongodb.morphia.entities.Position;

/**
 * DAO for Doctor entity
 * 
 * @author Vera Boutchkova
 */
public class DoctorDAO extends BaseEntityDAO<Doctor> {

	private Datastore datastore;

	public DoctorDAO(MongoDataSource ds) {
		super(ds.getDatastore());
		this.datastore = ds.getDatastore();
	}

	/**
	 * Gets all heads of wards ordered by first name first and next by last name.
	 * @return
	 */
	public List<Doctor> findHeadsOfWards() {
		Query<Doctor> query = (datastore.createQuery(Doctor.class)).field("position").equal(Position.HEAD_OF_WARD)
				.order("firstName, surName");
		return query.asList();
	}

}
