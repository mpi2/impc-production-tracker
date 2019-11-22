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
package org.gentar.web.mapping.project;

import org.gentar.web.dto.intention.ProjectIntentionDTO;
import org.gentar.web.dto.project.ProjectConsortiumDTO;
import org.gentar.web.dto.project.ProjectDTO;
import org.gentar.web.dto.species.SpeciesDTO;
import org.gentar.web.dto.status_stamps.StatusStampsDTO;
import org.gentar.web.mapping.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.Project;
import org.gentar.web.mapping.project.consortium.ProjectConsortiumMapper;
import org.gentar.web.mapping.project.intention.ProjectIntentionMapper;
import org.gentar.web.mapping.project.intention.SortByProjectIntentionIndex;
import org.gentar.web.mapping.statusStamp.StatusStampMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ProjectEntityToDtoMapper
{
    private EntityMapper entityMapper;
    private StatusStampMapper statusStampMapper;
    private ProjectIntentionMapper projectIntentionMapper;
    private ProjectConsortiumMapper projectConsortiumMapper;

    public ProjectEntityToDtoMapper(
        EntityMapper entityMapper,
        StatusStampMapper statusStampMapper,
        ProjectIntentionMapper projectIntentionMapper,
        ProjectConsortiumMapper projectConsortiumMapper)
    {
        this.entityMapper = entityMapper;
        this.statusStampMapper = statusStampMapper;
        this.projectIntentionMapper = projectIntentionMapper;
        this.projectConsortiumMapper = projectConsortiumMapper;
    }

    public ProjectDTO toDto(Project project)
    {
        ProjectDTO projectDTO = null;
        if (project != null)
        {
            projectDTO = entityMapper.toTarget(project, ProjectDTO.class);
            addStatusStampsDTO(project, projectDTO);
            addProjectIntentions(project, projectDTO);
            addSpeciesDTO(project, projectDTO);
            addProjectConsortia(project, projectDTO);
        }
        return projectDTO;
    }

    private void addProjectIntentions(Project project, ProjectDTO projectDTO)
    {
        List<ProjectIntentionDTO> projectIntentionDTOs =
            projectIntentionMapper.toDtos(project.getProjectIntentions());
        sort(projectIntentionDTOs);
        projectDTO.setProjectIntentionDTOS(projectIntentionDTOs);
    }

    private void sort(List<ProjectIntentionDTO> projectIntentionDTOs)
    {
        Collections.sort(projectIntentionDTOs, new SortByProjectIntentionIndex());
    }

    private void addProjectConsortia(Project project, ProjectDTO projectDTO)
    {
        List<ProjectConsortiumDTO> projectConsortiumDTOS =
                projectConsortiumMapper.toDtos(project.getProjectConsortia());
        projectDTO.setProjectConsortiumDTOS(projectConsortiumDTOS);
    }

    private void addSpeciesDTO(Project project, ProjectDTO projectDTO)
    {
        List<SpeciesDTO> projectSpeciesDTOs = new ArrayList<>();
        if (project.getSpecies() != null)
        {
            project.getSpecies().forEach(x ->
            {
                SpeciesDTO speciesDTO = new SpeciesDTO();
                speciesDTO.setName(x.getName());
                speciesDTO.setTaxonId(x.getTaxonId());
                projectSpeciesDTOs.add(speciesDTO);
            });
        }
        projectDTO.setProjectSpeciesDTOs(projectSpeciesDTOs);
    }

    private void addStatusStampsDTO(Project project, ProjectDTO projectDTO)
    {
        if (project.getAssignmentStatusStamps() != null)
        {
            List<StatusStampsDTO> statusStampsDTOS =
                statusStampMapper.toDtos(project.getAssignmentStatusStamps());
            projectDTO.setStatusStampsDTOS(statusStampsDTOS);
        }
    }

    public List<ProjectDTO> toDtos(List<Project> project)
    {
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        project.forEach(p -> projectDTOList.add(toDto(p)));
        return projectDTOList;
    }
}
