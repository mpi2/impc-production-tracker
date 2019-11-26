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

import org.gentar.biology.strain.StrainDTO;
import org.gentar.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainRepository;

@Component
public class StrainMapper
{
    private EntityMapper entityMapper;
    private StrainRepository strainRepository;

    public StrainMapper(EntityMapper entityMapper, StrainRepository strainRepository)
    {
        this.entityMapper = entityMapper;
        this.strainRepository = strainRepository;
    }

    public Strain toEntity(StrainDTO strainDTO)
    {
        Strain strain = entityMapper.toTarget(strainDTO, Strain.class);
        if (strain != null && strain.getStrainTypes() == null && strain.getId() != null)
        {
            Strain persisted = strainRepository.findById(strain.getId()).orElse(null);
            if (persisted != null)
            {
                strain.setStrainTypes(persisted.getStrainTypes());
            }

        }
        return strain;
    }
}
