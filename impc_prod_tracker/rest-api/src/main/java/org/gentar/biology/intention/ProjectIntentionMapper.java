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
import org.gentar.audit.diff.ChangeEntry;
import org.gentar.biology.gene.ProjectIntentionGeneDTO;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.sequence.ProjectIntentionSequenceDTO;
import org.gentar.EntityMapper;
import org.gentar.biology.mutation.MutationCategorizationMapper;
import org.gentar.biology.mutation.MolecularMutationTypeMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.intention.project_intention_sequence.ProjectIntentionSequence;

import java.util.*;

@Component
public class ProjectIntentionMapper implements Mapper<ProjectIntention, ProjectIntentionDTO>
{
    private final EntityMapper entityMapper;
    private final MutationCategorizationMapper mutationCategorizationMapper;
    private final MolecularMutationTypeMapper molecularMutationTypeMapper;
    private final ProjectIntentionGeneMapper projectIntentionGeneMapper;
    private final ProjectIntentionSequenceMapper projectIntentionSequenceMapper;
    private final OrthologMapper orthologMapper;

    public ProjectIntentionMapper(
            EntityMapper entityMapper,
            MutationCategorizationMapper mutationCategorizationMapper,
            MolecularMutationTypeMapper molecularMutationTypeMapper,
            ProjectIntentionGeneMapper projectIntentionGeneMapper,
            ProjectIntentionSequenceMapper projectIntentionSequenceMapper,
            OrthologMapper orthologMapper)
    {
        this.entityMapper = entityMapper;
        this.mutationCategorizationMapper = mutationCategorizationMapper;
        this.molecularMutationTypeMapper = molecularMutationTypeMapper;
        this.projectIntentionGeneMapper = projectIntentionGeneMapper;
        this.projectIntentionSequenceMapper = projectIntentionSequenceMapper;
        this.orthologMapper = orthologMapper;
    }

    @Override
    public ProjectIntentionDTO toDto(ProjectIntention projectIntention)
    {
        ProjectIntentionDTO projectIntentionDTO = new ProjectIntentionDTO();
        if (projectIntention.getMolecularMutationType() != null)
        {
            projectIntentionDTO.setMolecularMutationTypeName(
                projectIntention.getMolecularMutationType().getName());
        }
        projectIntentionDTO.setMutationCategorizationDTOS(
            mutationCategorizationMapper.toDtos(projectIntention.getMutationCategorizations()));
        setProjectIntentionGeneDTO(projectIntention, projectIntentionDTO);
        setProjectIntentionSequenceDTO(projectIntention, projectIntentionDTO);
        return projectIntentionDTO;
    }

    private void setProjectIntentionGeneDTO(
        ProjectIntention projectIntention, ProjectIntentionDTO projectIntentionDTO)
    {
        ProjectIntentionGene projectIntentionGene = projectIntention.getProjectIntentionGene();
        if (projectIntentionGene != null)
        {
            ProjectIntentionGeneDTO projectIntentionGeneDTO =
                projectIntentionGeneMapper.toDto(projectIntentionGene);
            projectIntentionGeneDTO.setAllOrthologs(
                orthologMapper.toDtos(projectIntentionGene.getAllOrthologs()));
            projectIntentionGeneDTO.setBestOrthologs(
                orthologMapper.toDtos(projectIntentionGene.getBestOrthologs()));
            projectIntentionDTO.setProjectIntentionGeneDTO(projectIntentionGeneDTO);
        }
    }

    private void setProjectIntentionSequenceDTO(
        ProjectIntention projectIntention, ProjectIntentionDTO projectIntentionDTO)
    {
        Set<ProjectIntentionSequence> projectIntentionSequences =
            projectIntention.getProjectIntentionSequences();
        if (projectIntentionSequences != null)
        {
            List<ProjectIntentionSequenceDTO> projectIntentionSequenceDTOS =
                projectIntentionSequenceMapper.toDtos(projectIntentionSequences);
            projectIntentionSequenceDTOS.sort(
                Comparator.comparing(ProjectIntentionSequenceDTO::getIndex));
            projectIntentionDTO.setProjectIntentionSequenceDTOS(projectIntentionSequenceDTOS);
        }
    }

    @Override
    public ProjectIntention toEntity(ProjectIntentionDTO projectIntentionDTO)
    {
        ProjectIntention projectIntention =
            entityMapper.toTarget(projectIntentionDTO, ProjectIntention.class);
        projectIntention.setMolecularMutationType(
            molecularMutationTypeMapper.toEntity(projectIntentionDTO.getMolecularMutationTypeName()));
        Set<MutationCategorization> mutationCategorizations =
                new HashSet<>(mutationCategorizationMapper.toEntities(
                    projectIntentionDTO.getMutationCategorizationDTOS()));
        projectIntention.setMutationCategorizations(mutationCategorizations);
        setProjectIntentionAttributes(projectIntention, projectIntentionDTO);
        return projectIntention;
    }

    private void setProjectIntentionAttributes(
        ProjectIntention projectIntention, ProjectIntentionDTO projectIntentionDTO)
    {
        if (projectIntentionDTO.getProjectIntentionGeneDTO() != null)
        {
            ProjectIntentionGene projectIntentionGene =
                projectIntentionGeneMapper.toEntity(projectIntentionDTO.getProjectIntentionGeneDTO());
            projectIntentionGene.setProjectIntention(projectIntention);
            projectIntention.setProjectIntentionGene(projectIntentionGene);
        }

        if (projectIntentionDTO.getProjectIntentionSequenceDTOS() != null)
        {
            Set<ProjectIntentionSequence> projectIntentionSequences =
                projectIntentionSequenceMapper.toEntities(
                    projectIntentionDTO.getProjectIntentionSequenceDTOS());
            projectIntentionSequences.forEach(x -> x.setProjectIntention(projectIntention));
            projectIntention.setProjectIntentionSequences(projectIntentionSequences);
        }
    }
}
