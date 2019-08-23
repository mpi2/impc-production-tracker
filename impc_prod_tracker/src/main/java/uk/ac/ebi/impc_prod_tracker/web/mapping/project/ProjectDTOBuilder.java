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

import lombok.Data;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.GeneService;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectGeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectLocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.status_stamps.StatusStampsDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.gene.ProjectGeneMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.location.ProjectLocationMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.statusStamp.StatusStampMapper;

import java.util.List;
import java.util.Objects;

@Component
@Data
public class ProjectDTOBuilder
{
    private PlanService planService;
    private GeneService geneService;
    private StatusStampMapper statusStampMapper;
    private ProjectGeneMapper projectGeneMapper;
    private ProjectLocationMapper projectLocationMapper;
    private static final String MGI_URL = "http://www.mousephenotype.org/data/genes/";


    public ProjectDTOBuilder(
        PlanService planService,
        GeneService geneService,
        StatusStampMapper statusStampMapper,
        ProjectGeneMapper projectGeneMapper,
        ProjectLocationMapper projectLocationMapper)
    {
        this.planService = planService;
        this.geneService = geneService;
        this.statusStampMapper = statusStampMapper;
        this.projectGeneMapper = projectGeneMapper;
        this.projectLocationMapper = projectLocationMapper;
    }

    public ProjectDTO buildProjectDTOFromProject(Project project)
    {
        Objects.requireNonNull(project, "The project is null");
        ProjectDTO projectDTO = new ProjectDTO();
        if (project.getAssignmentStatus() != null)
        {
            projectDTO.setAssigmentStatusName(project.getAssignmentStatus().getName());
        }
        if (project.getAssignmentStatusStamps() != null)
        {
            List<StatusStampsDTO> statusStampsDTOS = statusStampMapper.toDtos(project.getAssignmentStatusStamps());
            projectDTO.setStatusStampsDTOS(statusStampsDTOS);
        }

        projectDTO.setImitsMiPlanId(project.getImitsMiPlanId());
        projectDTO.setRecovery(project.getRecovery());
        projectDTO.setWithdrawn(project.getWithdrawn());
        projectDTO.setTpn(project.getTpn());
        projectDTO.setIsActive(project.getIsActive());
        projectDTO.setComment(project.getComment());

        if (!project.getGenes().isEmpty())
        {
            List<ProjectGeneDTO> projectGeneDTOS = projectGeneMapper.toDtos(project.getGenes());
            projectDTO.setProjectGeneDTOS(projectGeneDTOS);
        }
        if (!project.getLocations().isEmpty())
        {
            List<ProjectLocationDTO> projectLocationDTOS = projectLocationMapper.toDtos(project.getLocations());
            projectDTO.setProjectLocationDTOS(projectLocationDTOS);
        }

        return projectDTO;
    }
}

