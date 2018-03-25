/**
 * 
 */
package com.geh.mappers;

import java.util.List;

/**
 * Mapper interface for creation of DTOs from entities and vice versa
 *
 * @param <S>	source object type
 * @param <D>	destination object type
 * 
 * @author Vera Boutchkova
 */
public interface IMapper<S, D> {
	
    D map(S sourceObject);

    List<D> mapList(List<S> sourceList);
}
