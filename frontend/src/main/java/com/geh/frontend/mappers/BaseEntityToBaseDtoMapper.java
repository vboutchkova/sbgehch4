/**
 * 
 */
package com.geh.frontend.mappers;

import org.springframework.stereotype.Controller;

import com.geh.frontend.dtos.BaseDto;
import com.geh.mappers.AbstractMapper;
import com.geh.mongodb.morphia.entities.BaseEntity;

/**
 * Abstract mapper of Entity to BaseDto
 *
 * @author Vera Boutchkova
 */
@Controller
public abstract class BaseEntityToBaseDtoMapper<S extends BaseEntity, D extends BaseDto> extends AbstractMapper<S,D> {
	

	protected abstract D newDestinationObject();

	@Override
	public final D map(S sourceObject) {
		D destinationObject = newDestinationObject();
		mapInternal(sourceObject, destinationObject);
		return destinationObject;
	}

	/**
	 * Maps all the needed fields from the source object to the destinationObject, both are not null.
	 * 
	 * @param sourceObject
	 * @param destinationObject	not null
	 */
	protected void mapInternal(S sourceObject, D destinationObject) {
		destinationObject.setCreatedBy(sourceObject.getCreatedBy());
		destinationObject.setCreatedDate(sourceObject.getCreatedDate());
		destinationObject.setId(sourceObject.getId().toString());
		destinationObject.setLastModifiedBy(sourceObject.getLastModifiedBy());
		destinationObject.setLastModifiedDate(sourceObject.getLastModifiedDate());
		destinationObject.setVersion(sourceObject.getVersion());
	}
}
