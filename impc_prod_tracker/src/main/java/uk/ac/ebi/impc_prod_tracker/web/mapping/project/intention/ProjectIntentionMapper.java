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
package uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_location.ProjectIntentionLocation;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_sequence.ProjectIntentionSequence;
import uk.ac.ebi.impc_prod_tracker.service.biology.project_intention.ProjectIntentionService;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectIntentionGeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.intention.ProjectIntentionDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectIntentionLocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.sequence.ProjectIntentionSequenceDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.allele.AlleleTypeMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.allele_categorization.AlleleCategorizationMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.gene.ProjectIntentionGeneMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.location.ProjectIntentionLocationMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.molecular_mutation.MolecularMutationTypeMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.sequence.ProjectIntentionSequenceMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ProjectIntentionMapper
{
    private EntityMapper entityMapper;
    private AlleleTypeMapper alleleTypeMapper;
    private AlleleCategorizationMapper alleleCategorizationMapper;
    private MolecularMutationTypeMapper molecularMutationTypeMapper;
    private ProjectIntentionGeneMapper projectIntentionGeneMapper;
    private ProjectIntentionLocationMapper projectIntentionLocationMapper;
    private ProjectIntentionSequenceMapper projectIntentionSequenceMapper;
    private ProjectIntentionService projectIntentionService;

    private static final String INTENTION_TYPE_GENE = "gene";
    private static final String INTENTION_TYPE_LOCATION = "location";
    private static final String INTENTION_TYPE_SEQUENCE = "sequence";

    public ProjectIntentionMapper(
        EntityMapper entityMapper,
        AlleleTypeMapper alleleTypeMapper,
        AlleleCategorizationMapper alleleCategorizationMapper,
        MolecularMutationTypeMapper molecularMutationTypeMapper,
        ProjectIntentionGeneMapper projectIntentionGeneMapper,
        ProjectIntentionLocationMapper projectIntentionLocationMapper,
        ProjectIntentionSequenceMapper projectIntentionSequenceMapper, ProjectIntentionService projectIntentionService)
    {
        this.entityMapper = entityMapper;
        this.alleleTypeMapper = alleleTypeMapper;
        this.alleleCategorizationMapper = alleleCategorizationMapper;
        this.molecularMutationTypeMapper = molecularMutationTypeMapper;
        this.projectIntentionGeneMapper = projectIntentionGeneMapper;
        this.projectIntentionLocationMapper = projectIntentionLocationMapper;
        this.projectIntentionSequenceMapper = projectIntentionSequenceMapper;
        this.projectIntentionService = projectIntentionService;
    }

    public ProjectIntentionDTO toDto(ProjectIntention projectIntention)
    {
        ProjectIntentionDTO projectIntentionDTO =
            entityMapper.toTarget(projectIntention, ProjectIntentionDTO.class);
        projectIntentionDTO.setAlleleCategorizationDTOS(
            alleleCategorizationMapper.toDtos(projectIntention.getAlleleCategorizations()));
        setProjectIntentionGeneDTO(projectIntention, projectIntentionDTO);
        setProjectIntentionLocationDTO(projectIntention, projectIntentionDTO);
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
            projectIntentionDTO.setProjectIntentionGeneDTO(projectIntentionGeneDTO);
        }
    }

    private void setProjectIntentionLocationDTO(
        ProjectIntention projectIntention, ProjectIntentionDTO projectIntentionDTO)
    {
        ProjectIntentionLocation projectIntentionLocation =
            projectIntention.getProjectIntentionLocation();
        if (projectIntentionLocation != null)
        {
            ProjectIntentionLocationDTO projectIntentionLocationDTO =
                projectIntentionLocationMapper.toDto(projectIntentionLocation);
            projectIntentionDTO.setProjectIntentionLocationDTO(projectIntentionLocationDTO);
            projectIntentionDTO.setIndex(projectIntentionLocationDTO.getIndex());
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
        projectIntention.setAlleleType(
            alleleTypeMapper.toEntity(projectIntentionDTO.getAlleleTypeName()));
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
            projectIntention.setIntentionType(
                projectIntentionService.getIntentionTypeByName(INTENTION_TYPE_GENE));
        }
        else if (projectIntention.getProjectIntentionLocation() != null)
        {
            ProjectIntentionLocation projectIntentionLocation =
                projectIntentionLocationMapper.toEntity(
                    projectIntentionDTO.getProjectIntentionLocationDTO());
            projectIntentionLocation.setProjectIntention(projectIntention);
            projectIntention.setProjectIntentionLocation(projectIntentionLocation);
            projectIntention.setIntentionType(
                projectIntentionService.getIntentionTypeByName(INTENTION_TYPE_LOCATION));
        }
        else if (projectIntention.getProjectIntentionSequence() != null)
        {
            ProjectIntentionSequence projectIntentionSequence =
                projectIntentionSequenceMapper.toEntity(
                    projectIntentionDTO.getProjectIntentionSequenceDTO());
            projectIntentionSequence.setProjectIntention(projectIntention);
            projectIntention.setProjectIntentionSequence(projectIntentionSequence);
            projectIntention.setIntentionType(
                projectIntentionService.getIntentionTypeByName(INTENTION_TYPE_SEQUENCE));
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
