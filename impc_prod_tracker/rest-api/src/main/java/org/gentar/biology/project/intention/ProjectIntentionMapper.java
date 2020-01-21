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
package org.gentar.biology.project.intention;

import org.gentar.biology.intention.ProjectIntentionGeneDTO;
import org.gentar.biology.intention.ProjectIntentionDTO;
import org.gentar.biology.sequence.ProjectIntentionSequenceDTO;
import org.gentar.EntityMapper;
import org.gentar.biology.mutation.MutationCategorizationMapper;
import org.gentar.biology.molecular_mutation.MolecularMutationTypeMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.intention.project_intention.ProjectIntention;
import org.gentar.biology.project.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.project.intention.project_intention_sequence.ProjectIntentionSequence;
import org.gentar.biology.sequence.ProjectIntentionSequenceMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ProjectIntentionMapper
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
        ProjectIntentionSequence projectIntentionSequence =
            projectIntention.getProjectIntentionSequence();
        if (projectIntentionSequence != null)
        {
            ProjectIntentionSequenceDTO projectIntentionSequenceDTO =
                projectIntentionSequenceMapper.toDto(projectIntentionSequence);
            projectIntentionDTO.setProjectIntentionSequenceDTO(projectIntentionSequenceDTO);
            projectIntentionDTO.setIndex(projectIntentionDTO.getIndex());
        }
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

    public ProjectIntention toEntity(ProjectIntentionDTO projectIntentionDTO)
    {
        ProjectIntention projectIntention =
            entityMapper.toTarget(projectIntentionDTO, ProjectIntention.class);
        projectIntention.setMolecularMutationType(
            molecularMutationTypeMapper.toEntity(projectIntentionDTO.getMolecularMutationTypeName()));
        setProjectIntention(projectIntention, projectIntentionDTO);
        return projectIntention;
    }

    private void setProjectIntention(
        ProjectIntention projectIntention, ProjectIntentionDTO projectIntentionDTO)
    {
        if (projectIntention.getProjectIntentionGene() != null)
        {
            ProjectIntentionGene projectIntentionGene =
                projectIntentionGeneMapper.toEntity(projectIntentionDTO.getProjectIntentionGeneDTO());
            projectIntentionGene.setProjectIntention(projectIntention);
            projectIntention.setProjectIntentionGene(projectIntentionGene);
        }
        else if (projectIntention.getProjectIntentionSequence() != null)
        {
            ProjectIntentionSequence projectIntentionSequence =
                projectIntentionSequenceMapper.toEntity(
                    projectIntentionDTO.getProjectIntentionSequenceDTO());
            projectIntentionSequence.setProjectIntention(projectIntention);
            projectIntention.setProjectIntentionSequence(projectIntentionSequence);
        }
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
}
