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
package org.gentar.biology.plan.attempt.crispr_attempt;

import org.gentar.Mapper;
import org.gentar.biology.strain.StrainService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import org.gentar.biology.strain.Strain;

@Component
public class StrainMapper implements Mapper<Strain, String>
{
    private StrainService strainService;

    private static final String STRAIN_NOT_FOUND_ERROR = "Strain '%s' does not exist.";

    public StrainMapper(StrainService strainService)
    {
        this.strainService = strainService;
    }

    @Override
    public String toDto(Strain entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    public Strain toEntity(String strainName)
    {
        Strain strain = strainService.getStrainByName(strainName);

        if (strain == null)
        {
            throw new UserOperationFailedException(String.format(STRAIN_NOT_FOUND_ERROR, strainName));
        }

        return strain;
    }
}
