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

import org.gentar.biology.intention.ProjectIntentionDTO;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.EntityMapper;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.consortium.ProjectConsortiumMapper;
import org.gentar.biology.intention.ProjectIntentionMapper;
import org.gentar.biology.status.StatusStampMapper;
import java.util.ArrayList;
import java.util.HashSet;
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
            addRelatedWorkUnits(project, projectDTO);
            addRelatedWorkGroups(project, projectDTO);
        }
        return projectDTO;
    }

    private void addProjectIntentions(Project project, ProjectDTO projectDTO)
    {
        List<ProjectIntentionDTO> projectIntentionDTOs =
            projectIntentionMapper.toDtos(project.getProjectIntentions());
        projectDTO.setProjectIntentionDTOS(projectIntentionDTOs);
    }

    private void addProjectConsortia(Project project, ProjectDTO projectDTO)
    {
        List<ProjectConsortiumDTO> projectConsortiumDTOS =
                projectConsortiumMapper.toDtos(project.getProjectConsortia());
        projectDTO.setProjectConsortiumDTOS(projectConsortiumDTOS);
    }

    private void addRelatedWorkUnits(Project project, ProjectDTO projectDTO)
    {
        List<String> relatedWorkUnits = new ArrayList<>();
        List<WorkUnit> workUnits = project.getRelatedWorkUnits();
        if (workUnits != null)
        {
            workUnits.forEach(x -> relatedWorkUnits.add(x.getName()));
        }
        projectDTO.setRelatedWorkUnitNames(new HashSet<>(relatedWorkUnits));
    }

    private void addRelatedWorkGroups(Project project, ProjectDTO projectDTO)
    {
        List<String> relatedWorkGroups = new ArrayList<>();
        List<WorkGroup> workGroups = project.getRelatedWorkGroups();
        if (workGroups != null)
        {
            workGroups.forEach(x -> relatedWorkGroups.add(x.getName()));
        }
        projectDTO.setRelatedWorkGroupNames(new HashSet<>(relatedWorkGroups));
    }

    private void addSpeciesDTO(Project project, ProjectDTO projectDTO)
    {
        List<String> speciesNames = new ArrayList<>();
        if (project.getSpecies() != null)
        {
            project.getSpecies().forEach(x ->
            {
                speciesNames.add(x.getName());
            });
        }
        projectDTO.setSpeciesNames(speciesNames);
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
