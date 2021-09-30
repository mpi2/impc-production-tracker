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
package org.gentar.biology.plan.attempt.crispr;

import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_class.NucleaseClass;
import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.crispr.nuclease.Nuclease;
import org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_type.NucleaseType;

import java.util.*;

@Component
public class NucleaseMapper implements Mapper<Nuclease, NucleaseDTO>
{
    private EntityMapper entityMapper;
    private CrisprAttemptService crisprAttemptService;

    public NucleaseMapper(EntityMapper entityMapper, CrisprAttemptService crisprAttemptService)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptService = crisprAttemptService;
    }

    public NucleaseDTO toDto(Nuclease nuclease)
    {
        NucleaseDTO nucleaseDTO = entityMapper.toTarget(nuclease, NucleaseDTO.class);
        return nucleaseDTO;
    }

    @Override
    public List<NucleaseDTO> toDtos(Collection<Nuclease> nucleases) {
        List<NucleaseDTO> NucleaseDTOS = new ArrayList<>();
        if (nucleases != null)
        {
            nucleases.forEach(nuclease -> NucleaseDTOS.add(toDto(nuclease)));
        }
        return NucleaseDTOS;
    }

    public Nuclease toEntity(NucleaseDTO nucleaseDTO)
    {
        Nuclease nuclease = entityMapper.toTarget(nucleaseDTO, Nuclease.class);
        if (nucleaseDTO.getId() != null)
        {
            nuclease.setId(nucleaseDTO.getId());
        }
        String typeName = nucleaseDTO.getTypeName();
        String className = nucleaseDTO.getClassName();
        NucleaseType nucleaseType =
                crisprAttemptService.getNucleaseTypeByName(typeName);
        NucleaseClass nucleaseClass =
                crisprAttemptService.getNucleaseClassByName(className);
       nuclease.setNucleaseType(nucleaseType);
       nuclease.setNucleaseClass(nucleaseClass);

        return nuclease;
    }

    @Override
    public Set<Nuclease> toEntities(Collection<NucleaseDTO> nucleaseDTOS) {
        Set<Nuclease> nucleases = new HashSet<>();
        if (nucleaseDTOS != null)
        {
            nucleaseDTOS.forEach(nucleaseDTO -> nucleases.add(toEntity(nucleaseDTO)));
        }
        return nucleases;
    }
}
