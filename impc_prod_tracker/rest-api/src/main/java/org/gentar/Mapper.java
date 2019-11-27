/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines the operations for Mapper classes. Mapping implies converting Entity (S) to DTO (V) or
 * vice versa.
 * @param <S> Entity.
 * @param <V> DTO.
 */
public interface Mapper<S, V>
{
    /**
     * Converts a entity to a DTO.
     * @param entity The entity.
     * @return The DTO.
     */
    V toDto(S entity);

    /**
     * Converts a collection of entities to a list of DTO.
     * @param entities Collection of entities.
     * @return List of DTOs.
     */
    default List<V> toDtos(Collection<S> entities)
    {
        List<V> dtos = new ArrayList<>();
        if (entities != null)
        {
            entities.forEach(x -> dtos.add(toDto(x)));
        }
        return dtos;
    }

    /**
     * Converts a DTO to a Entity.
     * @param dto The DTO.
     * @return The entity.
     */
    default S toEntity(V dto)
    {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    default Collection<S> toEntities(Collection<V> dtos)
    {
        Set<S> entities = new HashSet<>();
        if (dtos != null)
        {
            dtos.forEach(x -> entities.add(toEntity(x)));
        }
        return entities;
    }
}
