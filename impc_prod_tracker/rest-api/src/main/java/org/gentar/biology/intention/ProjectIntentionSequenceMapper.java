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
package org.gentar.biology.intention;

import org.gentar.Mapper;
import org.gentar.biology.sequence.ProjectIntentionSequenceDTO;
import org.gentar.biology.sequence.SequenceMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.intention.project_intention_sequence.ProjectIntentionSequence;

import java.util.*;

@Component
public class ProjectIntentionSequenceMapper implements Mapper<ProjectIntentionSequence, ProjectIntentionSequenceDTO>
{
    private SequenceMapper sequenceMapper;

    public ProjectIntentionSequenceMapper(SequenceMapper sequenceMapper)
    {
        this.sequenceMapper = sequenceMapper;
    }

    public ProjectIntentionSequenceDTO toDto(ProjectIntentionSequence projectIntentionSequence)
    {
        ProjectIntentionSequenceDTO projectIntentionSequenceDTO = new ProjectIntentionSequenceDTO();
        projectIntentionSequenceDTO.setSequenceDTO(
            sequenceMapper.toDto(projectIntentionSequence.getSequence()));
        projectIntentionSequenceDTO.setIndex(projectIntentionSequence.getIndex());
        return projectIntentionSequenceDTO;
    }

    public List<ProjectIntentionSequenceDTO> toDtos(
        Collection<ProjectIntentionSequence> projectIntentionSequences)
    {
        List<ProjectIntentionSequenceDTO> projectIntentionSequenceDTOS = new ArrayList<>();
        if (projectIntentionSequences != null)
        {
            projectIntentionSequences.forEach(x -> projectIntentionSequenceDTOS.add(toDto(x)));
        }
        return projectIntentionSequenceDTOS;
    }

    public ProjectIntentionSequence toEntity(ProjectIntentionSequenceDTO projectIntentionSequenceDTO)
    {
        ProjectIntentionSequence projectIntentionGene = new ProjectIntentionSequence();
        projectIntentionGene.setSequence(
            sequenceMapper.toEntity(projectIntentionSequenceDTO.getSequenceDTO()));
        projectIntentionGene.setIndex(projectIntentionSequenceDTO.getIndex());

        return projectIntentionGene;
    }

    public Set<ProjectIntentionSequence> toEntities(Collection<ProjectIntentionSequenceDTO> projectIntentionSequenceDTOS)
    {
        Set<ProjectIntentionSequence> projectIntentionSequences = new HashSet<>();
        if (projectIntentionSequenceDTOS != null)
        {
            projectIntentionSequenceDTOS.forEach(x -> projectIntentionSequences.add(toEntity(x)));
        }
        return projectIntentionSequences;
    }
}
