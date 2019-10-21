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
package uk.ac.ebi.impc_prod_tracker.web.mapping.project;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_location.ProjectIntentionLocation;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_sequence.ProjectIntentionSequence;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectIntentionGeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectIntentionLocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.sequence.ProjectIntentionSequenceDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.consortium.ConsortiumMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.gene.ProjectIntentionGeneMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.location.ProjectIntentionLocationMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.privacy.PrivacyMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.intention.ProjectIntentionMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.sequence.ProjectIntentionSequenceMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class in charge of converting a {@link ProjectDTO} into a {@link Project}  entity.
 */
@Component
public class ProjectDtoToEntityMapper
{
    private EntityMapper entityMapper;
    private PrivacyMapper privacyMapper;
    private ConsortiumMapper consortiumMapper;
    private ProjectIntentionMapper projectIntentionMapper;
    private ProjectIntentionGeneMapper projectIntentionGeneMapper;
    private ProjectIntentionLocationMapper projectIntentionLocationMapper;
    private ProjectIntentionSequenceMapper projectIntentionSequenceMapper;

    public ProjectDtoToEntityMapper(
        EntityMapper entityMapper,
        PrivacyMapper privacyMapper,
        ConsortiumMapper consortiumMapper,
        ProjectIntentionMapper projectIntentionMapper, ProjectIntentionGeneMapper projectIntentionGeneMapper, ProjectIntentionLocationMapper projectIntentionLocationMapper, ProjectIntentionSequenceMapper projectIntentionSequenceMapper)
    {
        this.entityMapper = entityMapper;
        this.privacyMapper = privacyMapper;
        this.consortiumMapper = consortiumMapper;
        this.projectIntentionMapper = projectIntentionMapper;
        this.projectIntentionGeneMapper = projectIntentionGeneMapper;
        this.projectIntentionLocationMapper = projectIntentionLocationMapper;
        this.projectIntentionSequenceMapper = projectIntentionSequenceMapper;
    }

    public Project toEntity(ProjectDTO projectDTO)
    {
        Project project = entityMapper.toTarget(projectDTO, Project.class);
        setProjectIntention(project, projectDTO);
        setPrivacy(project, projectDTO);
        setConsortia(project, projectDTO);
        return project;
    }

    private void setProjectIntention(Project project, ProjectDTO projectDTO)
    {
        List<ProjectIntention> projectIntentions = getProjectIntentionsByGene(projectDTO);
        if (projectIntentions.isEmpty())
        {
            projectIntentions = getProjectIntentionsByLocation(projectDTO);
            if (projectIntentions.isEmpty())
            {
                projectIntentions = getProjectIntentionsBySequence(projectDTO);
            }
        }
        projectIntentions.forEach(x -> x.setProject(project));
        project.setProjectIntentions(new HashSet<>(projectIntentions));
    }

    private List<ProjectIntention> getProjectIntentionsByGene(ProjectDTO projectDTO)
    {
        List<ProjectIntention> projectIntentions = new ArrayList<>();
        List<ProjectIntentionGeneDTO> projectIntentionGeneDTOS =
            projectDTO.getProjectIntentionGeneDTOS();
        if (projectIntentionGeneDTOS != null)
        {
            projectIntentionGeneDTOS.forEach(x -> {
                ProjectIntentionGene projectIntentionGene = projectIntentionGeneMapper.toEntity(x);
                ProjectIntention projectIntention = projectIntentionGene.getProjectIntention();
                projectIntention.setProjectIntentionGene(projectIntentionGene);
                projectIntentions.add(projectIntention);
            });
        }
        return projectIntentions;
    }

    private List<ProjectIntention> getProjectIntentionsByLocation(ProjectDTO projectDTO)
    {
        List<ProjectIntention> projectIntentions = new ArrayList<>();
        List<ProjectIntentionLocationDTO> projectIntentionLocationDTOS =
            projectDTO.getProjectIntentionLocationDTOS();
        if (projectIntentionLocationDTOS != null)
        {
            projectIntentionLocationDTOS.forEach(x -> {
                ProjectIntentionLocation projectIntentionLocation = projectIntentionLocationMapper.toEntity(x);
                ProjectIntention projectIntention = projectIntentionLocation.getProjectIntention();
                projectIntentions.add(projectIntention);
            });
        }
        return projectIntentions;
    }

    private List<ProjectIntention> getProjectIntentionsBySequence(ProjectDTO projectDTO)
    {
        List<ProjectIntention> projectIntentions = new ArrayList<>();
        List<ProjectIntentionSequenceDTO> projectIntentionSequenceDTOS =
            projectDTO.getProjectIntentionSequenceDTOS();
        if (projectIntentionSequenceDTOS != null)
        {
            projectIntentionSequenceDTOS.forEach(x -> {
                ProjectIntentionSequence projectIntentionSequence = projectIntentionSequenceMapper.toEntity(x);
                ProjectIntention projectIntention = projectIntentionSequence.getProjectIntention();
                projectIntentions.add(projectIntention);
            });
        }
        return projectIntentions;
    }

    private void setPrivacy(Project project, ProjectDTO projectDTO)
    {
        Privacy privacy =  privacyMapper.toEntity(projectDTO.getPrivacyName());
        project.setPrivacy(privacy);
    }

    private void setConsortia(Project project, ProjectDTO projectDTO)
    {
        Set<Consortium> consortia = consortiumMapper.toEntities(projectDTO.getConsortiaNames());
        if (!consortia.isEmpty())
        {
            project.setConsortia(consortia);
        }
    }
}
