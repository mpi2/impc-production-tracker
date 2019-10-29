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
package uk.ac.ebi.impc_prod_tracker.service.organization.consortium;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.ConsortiumRepository;

import java.util.List;

@Component
public class ConsortiumService
{
    private ConsortiumRepository consortiumRepository;

    private static final String CONSORTIUM_NOT_EXISTS_ERROR = "Consortium %s does not exist.";

    public ConsortiumService(ConsortiumRepository consortiumRepository)
    {
        this.consortiumRepository = consortiumRepository;
    }

    @Cacheable("consortiumNames")
    public Consortium findConsortiumByName(String name)
    {
        return consortiumRepository.findByNameIgnoreCase(name);
    }

    public Consortium getConsortiumByNameOrThrowException(String consortiumName)
    {
        Consortium consortium = findConsortiumByName(consortiumName);
        if (consortium == null)
        {
            throw new UserOperationFailedException(
                String.format(CONSORTIUM_NOT_EXISTS_ERROR, consortiumName));
        }
        return consortium;
    }

    public List<Consortium> findAllConsortia()
    {
        return consortiumRepository.findAll();
    }
}
