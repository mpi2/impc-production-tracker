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
package org.gentar.biology.project;

import org.gentar.EntityMapper;
import org.gentar.biology.project.consortium.ProjectConsortium;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.species.SpeciesMapper;
import org.gentar.biology.species.Species;
import org.gentar.biology.status.StatusMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.project.consortium.ProjectConsortiumMapper;
import org.gentar.biology.intention.ProjectIntentionMapper;
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
    private ProjectConsortiumMapper projectConsortiumMapper;
    private ProjectIntentionMapper projectIntentionMapper;
    private StatusMapper summaryStatusMapper;
    private SpeciesMapper speciesMapper;

    public ProjectDtoToEntityMapper(
            EntityMapper entityMapper,
            PrivacyMapper privacyMapper,
            ProjectConsortiumMapper projectConsortiumMapper,
            ProjectIntentionMapper projectIntentionMapper,
            StatusMapper summaryStatusMapper,
            SpeciesMapper speciesMapper)
    {
        this.entityMapper = entityMapper;
        this.privacyMapper = privacyMapper;
        this.projectConsortiumMapper = projectConsortiumMapper;
        this.projectIntentionMapper = projectIntentionMapper;
        this.summaryStatusMapper = summaryStatusMapper;
        this.speciesMapper = speciesMapper;
    }

    public Project toEntity(ProjectDTO projectDTO)
    {
        Project project = entityMapper.toTarget(projectDTO, Project.class);
        setSummaryStatus(project, projectDTO);
        setProjectIntention(project, projectDTO);
        setPrivacy(project, projectDTO);
        setConsortia(project, projectDTO);
        setSpecies(project, projectDTO);
        return project;
    }

    private void setSpecies(Project project, ProjectDTO projectDTO)
    {
        Set<Species> species =
                speciesMapper.toEntities(projectDTO.getSpeciesNames());
        project.setSpecies(species);
    }

    private void setSummaryStatus(Project project, ProjectDTO projectDTO)
    {
        if (projectDTO.getSummaryStatusName() == null) {
            project.setSummaryStatus(
                    summaryStatusMapper.toEntity("Plan Created"));
        } else {
            project.setSummaryStatus(
                    summaryStatusMapper.toEntity(projectDTO.getSummaryStatusName()));
        }
    }

    private void setProjectIntention(Project project, ProjectDTO projectDTO)
    {
        List<ProjectIntention> projectIntentions =
            projectIntentionMapper.toEntities(projectDTO.getProjectIntentionDTOS());
        projectIntentions.forEach(x -> x.setProject(project));
        project.setProjectIntentions(projectIntentions);
    }

    private void setPrivacy(Project project, ProjectDTO projectDTO)
    {
        Privacy privacy =  privacyMapper.toEntity(projectDTO.getPrivacyName());
        project.setPrivacy(privacy);
    }

    private void setConsortia(Project project, ProjectDTO projectDTO)
    {
        Set<ProjectConsortium> projectConsortia = projectConsortiumMapper.toEntities(projectDTO.getProjectConsortiumDTOS());
        projectConsortia.forEach(x -> x.setProject(project));
        project.setProjectConsortia(projectConsortia);
    }
}
