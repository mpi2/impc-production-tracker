/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.organization.consortium;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class ConsortiumMapper
{
    private final ConsortiumService consortiumService;
    private static final String CONSORTIUM_NOT_FOUND_ERROR = "Consortium [%s] does not exist.";

    public ConsortiumMapper(ConsortiumService consortiumService)
    {
        this.consortiumService = consortiumService;
    }

    public Consortium toEntity(String consortiumName)
    {
        Consortium consortium = consortiumService.findConsortiumByName(consortiumName);
        if (consortium == null)
        {
            throw new UserOperationFailedException(
                String.format(CONSORTIUM_NOT_FOUND_ERROR, consortiumName));
        }
        return consortium;
    }

    public Set<Consortium> toEntities(Collection<String> consortiumNames)
    {
        Set<Consortium> consortia = new HashSet<>();
        if (consortiumNames != null)
        {
            consortiumNames.forEach(x -> consortia.add(toEntity(x)));
        }
        return consortia;
    }
}
