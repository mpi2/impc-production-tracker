/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.organization.institute;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class InstituteService
{
    private InstituteRepository instituteRepository;

    private static final String INSTITUTE_NOT_EXISTS_ERROR = "Institute %s does not exist.";

    public InstituteService(InstituteRepository instituteRepository)
    {
        this.instituteRepository = instituteRepository;
    }

    @Cacheable("instituteNames")
    public Institute findInstituteByName(String name)
    {
        return instituteRepository.findByNameIgnoreCase(name);
    }

    public Institute getInstituteByNameOrThrowException(String instituteName)
    {
        Institute institute = findInstituteByName(instituteName);
        if (institute == null)
        {
            throw new UserOperationFailedException(
                    String.format(INSTITUTE_NOT_EXISTS_ERROR, instituteName));
        }
        return institute;
    }

    public List<Institute> getAllInstitutes()
    {
        return instituteRepository.findAll();
    }
}
