/**
 * 
 */
package com.geh.frontend.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.geh.mappers.IMapper;
import com.geh.mongodb.morphia.entities.BaseEntity;

/**
 * Abstract mapper of DTO to BaseEntity
 *
 * @author Vera Boutchkova
 */
public abstract class AbstractDtoToEntityMapper <S, D extends BaseEntity> implements IMapper<S, D> {

	/* (non-Javadoc)
	 * @see com.geh.frontend.mappers.IAbstractMapper#map(java.lang.Object)
	 */
	@Override
	public D map(S sourceObject) {
		return map(sourceObject, null);
	}

	public abstract D map(S sourceObject, D destinationObject);
	
	/* (non-Javadoc)
	 * @see com.geh.frontend.mappers.IAbstractMapper#mapList(java.util.List)
	 */
	@Override
	public final List<D> mapList(List<S> sourceList) {
		List<D> result = sourceList.stream().map(s -> map(s)).collect(Collectors.toList());
		return result;
	}
}
