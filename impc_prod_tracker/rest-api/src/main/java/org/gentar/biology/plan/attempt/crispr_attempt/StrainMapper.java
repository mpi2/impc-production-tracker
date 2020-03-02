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
import org.gentar.biology.strain.StrainDTO;
import org.gentar.EntityMapper;
import org.gentar.biology.strain.StrainService;
import org.springframework.stereotype.Component;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainRepository;

@Component
public class StrainMapper implements Mapper<Strain, StrainDTO>
{
    private EntityMapper entityMapper;
    private StrainRepository strainRepository;
    private StrainService strainService;

    public StrainMapper(EntityMapper entityMapper, StrainRepository strainRepository, StrainService strainService)
    {
        this.entityMapper = entityMapper;
        this.strainRepository = strainRepository;
        this.strainService = strainService;
    }

    @Override
    public StrainDTO toDto(Strain entity)
    {
        return entityMapper.toTarget(entity, StrainDTO.class);
    }

    public Strain toEntity(StrainDTO strainDTO)
    {
        Strain strain = strainService.getStrainByName(strainDTO.getStrainName());

        if (strain != null && strain.getStrainTypes() == null && strain.getId() != null)
        {
            Strain persisted = strainService.getStrainById(strain.getId());

            if (persisted != null)
            {
                strain.setStrainTypes(persisted.getStrainTypes());
            }

        }
        return strain;
    }
}
