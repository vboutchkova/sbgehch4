/**
 * 
 */
package com.geh.mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract mapper
 *
 * @author Vera Boutchkova
 */
public abstract class AbstractMapper<S, D> implements IMapper<S, D> {
	

	/* (non-Javadoc)
	 * @see com.geh.frontend.mappers.IAbstractMapper#map(java.lang.Object)
	 */
	@Override
	public abstract D map(S sourceObject);


	/* (non-Javadoc)
	 * @see com.geh.frontend.mappers.IAbstractMapper#mapList(java.util.List)
	 */
	@Override
	public final List<D> mapList(List<S> sourceList) {
		List<D> result = sourceList.stream().map(s -> map(s)).collect(Collectors.toList());
		return result;
	}

}
