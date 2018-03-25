/**
 * 
 */
package com.geh.mongodb.morphia.dao;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.geh.mongodb.morphia.entities.BaseEntity;
import com.mongodb.WriteConcern;

/**
 * DAO for all morphia entities
 * 
 * @author Vera Boutchkova
 */
public class BaseEntityDAO<T extends BaseEntity> extends BasicDAO<T, ObjectId> {

	/**
	 * @param ds
	 */
	protected BaseEntityDAO(Datastore ds) {
		super(ds);
	}

    @Override
    public Key<T> save(final T entity) {
    	setInfo(entity);
        return getDatastore().save(entity);
    }

    @Override
    public Key<T> save(final T entity, final WriteConcern wc) {
    	setInfo(entity);
        return getDatastore().save(entity, new InsertOptions().writeConcern(wc));
    }
    
	private void setInfo(final T entity) {
		Date currentDate = new Date();
		entity.setLastModifiedDate(currentDate);
		
		Authentication auth;
		String currentUserName = null;
		try {
			auth = SecurityContextHolder.getContext().getAuthentication();
			currentUserName = auth.getName();
		} catch (Exception e) {
			currentUserName = "anonymous";
		}
		entity.setLastModifiedBy(currentUserName);
		
		if (entity.getCreatedDate() == null) {
			entity.setCreatedDate(currentDate);
			entity.setCreatedBy(currentUserName);
		}
	}

}
