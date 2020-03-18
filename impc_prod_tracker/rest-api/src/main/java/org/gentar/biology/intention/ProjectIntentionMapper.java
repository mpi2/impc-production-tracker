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
    private EntityMapper entityMapper;
    private MutationCategorizationMapper mutationCategorizationMapper;
    private MolecularMutationTypeMapper molecularMutationTypeMapper;
    private ProjectIntentionGeneMapper projectIntentionGeneMapper;
    private ProjectIntentionSequenceMapper projectIntentionSequenceMapper;
    private OrthologMapper orthologMapper;

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

    public ProjectIntentionDTO toDto(ProjectIntention projectIntention)
    {
        ProjectIntentionDTO projectIntentionDTO =
            entityMapper.toTarget(projectIntention, ProjectIntentionDTO.class);
        projectIntentionDTO.setMutationCategorizationDTOS(
            mutationCategorizationMapper.toDtos(projectIntention.getMutationCategorizations()));
        setProjectIntentionGeneDTO(projectIntention, projectIntentionDTO);
        setProjectIntentionSequenceDTO(projectIntention, projectIntentionDTO);
        return projectIntentionDTO;
    }

    public List<ProjectIntentionDTO> toDtos(Collection<ProjectIntention> projectIntention)
    {
        List<ProjectIntentionDTO> intentionDTOS = new ArrayList<>();
        if (projectIntention != null)
        {
            projectIntention.forEach(x -> intentionDTOS.add(toDto(x)));
        }
        return intentionDTOS;
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
            projectIntentionDTO.setProjectIntentionSequenceDTOS(projectIntentionSequenceDTOS);
        }
    }

    public ProjectIntention toEntity(ProjectIntentionDTO projectIntentionDTO)
    {
        ProjectIntention projectIntention =
            entityMapper.toTarget(projectIntentionDTO, ProjectIntention.class);
        projectIntention.setMolecularMutationType(
            molecularMutationTypeMapper.toEntity(projectIntentionDTO.getMolecularMutationTypeName()));
        Set<MutationCategorization> mutationCategorizations =
                new HashSet<>(mutationCategorizationMapper.toEntities(projectIntentionDTO.getMutationCategorizationDTOS()));
        projectIntention.setMutationCategorizations(mutationCategorizations);
        setProjectIntentionAttributes(projectIntention, projectIntentionDTO);
        return projectIntention;
    }

    public List<ProjectIntention> toEntities(Collection<ProjectIntentionDTO> projectIntentionDTOS)
    {
        List<ProjectIntention> projectIntentions = new ArrayList<>();
        if (projectIntentionDTOS != null)
        {
            projectIntentionDTOS.forEach(x -> projectIntentions.add(toEntity(x)));
        }
        return projectIntentions;
    }

    private void setProjectIntentionAttributes(ProjectIntention projectIntention, ProjectIntentionDTO projectIntentionDTO)
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
                    projectIntentionSequenceMapper.toEntities(projectIntentionDTO.getProjectIntentionSequenceDTOS());
            projectIntentionSequences.forEach(x -> x.setProjectIntention(projectIntention));
            projectIntention.setProjectIntentionSequences(projectIntentionSequences);
        }
    }
}
