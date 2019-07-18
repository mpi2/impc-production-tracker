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
package uk.ac.ebi.impc_prod_tracker.controller.project;

import lombok.Data;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.PlanDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.data.biology.ortholog.Ortholog;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project_mouse_gene.ProjectMouseGene;
import uk.ac.ebi.impc_prod_tracker.service.MouseGeneService;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;

import java.util.*;

@Component
@Data
public class ProjectDTOBuilder
{
    private PlanService planService;
    private PlanDTOBuilder planDTOBuilder;
    private MouseGeneService mouseGeneService;
    private static final String MGI_URL = "http://www.mousephenotype.org/data/genes/";

    public ProjectDTOBuilder(PlanService planService, PlanDTOBuilder planDTOBuilder, MouseGeneService mouseGeneService)
    {
        this.planService = planService;
        this.planDTOBuilder = planDTOBuilder;
        this.mouseGeneService = mouseGeneService;
    }

    public ProjectDTO buildProjectDTOFromProject(Project project)
    {
        ProjectDTO projectDTO = convertToDto(project);
        List<Plan> plans = planService.getPlansByProject(project);
        List<PlanDTO> planDTOS = new ArrayList<>();

        for (Plan p : plans)
        {
            PlanDTO planDTO = planDTOBuilder.buildPlanDTOFromPlan(p);
            planDTOS.add(planDTO);

        }
        projectDTO.setPlans(planDTOS);

        return projectDTO;
    }

    private ProjectDTO convertToDto(Project project)
    {
        Objects.requireNonNull(project, "The project is null");
        ProjectDTO projectDTO = new ProjectDTO();
        ProjectDetailsDTO projectDetailsDTO = buildProjectDetailsDTOFromProject(project);
        projectDetailsDTO.setAssigmentStatusName(project.getAssignmentStatus().getName());
        projectDetailsDTO.setTpn(project.getTpn());
        projectDTO.setProjectDetailsDTO(projectDetailsDTO);

        return projectDTO;
    }

    public ProjectDetailsDTO buildProjectDetailsDTOFromProject(Project project)
    {
        Objects.requireNonNull(project, "The project is null");
        ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO();
        if (project.getAssignmentStatus() != null)
        {
            projectDetailsDTO.setAssigmentStatusName(project.getAssignmentStatus().getName());
        }

        projectDetailsDTO.setTpn(project.getTpn());

         addMarkerSymbols(projectDetailsDTO, project);
         addIntentions(projectDetailsDTO, project);

         addHumanGenes(projectDetailsDTO, project);

        return projectDetailsDTO;
    }

    private void addMarkerSymbols(ProjectDetailsDTO projectDetailsDTO, final Project project)
    {
        Set<ProjectMouseGene> projectMouseGeneSet = project.getProjectMouseGenes();

        List<ProjectDetailsDTO.MarkerSymbolDTO> markerSymbolDTOS = new ArrayList<>();

        projectMouseGeneSet.forEach(x -> {
            ProjectDetailsDTO.MarkerSymbolDTO markerSymbolDTO = new ProjectDetailsDTO.MarkerSymbolDTO();
            markerSymbolDTO.setMarkerSymbol(x.getGene().getSymbol());
            markerSymbolDTO.setMgiLink(MGI_URL + x.getGene().getMgiId());
            markerSymbolDTOS.add(markerSymbolDTO);
        });

        projectDetailsDTO.setMarkerSymbols(markerSymbolDTOS);
    }

    private void addIntentions(ProjectDetailsDTO projectDetailsDTO, final Project project)
    {
        Set<ProjectMouseGene> projectMouseGeneSet = project.getProjectMouseGenes();

        List<String> intentions = new ArrayList<>();

        projectMouseGeneSet.forEach(x -> intentions.add(x.getAlleleType().getName()) );

        projectDetailsDTO.setAlleleIntentions(intentions);
    }

    private void addHumanGenes(ProjectDetailsDTO projectDetailsDTO, final Project project)
    {
        Set<ProjectMouseGene> projectMouseGeneSet = project.getProjectMouseGenes();

        List<String> mouseMgiIds = new ArrayList<>();

        projectMouseGeneSet.forEach(x -> mouseMgiIds.add(x.getGene().getMgiId()) );

        List<ProjectDetailsDTO.HumanGeneSymbolDTO> humanGeneSymbolDTOS = new ArrayList<>();

        mouseMgiIds.forEach(x -> {
            Set<Ortholog> orthologs = mouseGeneService.getMouseGenesByMgiId(x).getOrthologs();
            orthologs.forEach(o -> {
                ProjectDetailsDTO.HumanGeneSymbolDTO humanGeneSymbolDTO = new ProjectDetailsDTO.HumanGeneSymbolDTO();
                humanGeneSymbolDTO.setSymbol(o.getHumanGene().getSymbol());
                humanGeneSymbolDTO.setSupport(o.getSupport());
                humanGeneSymbolDTO.setSupport_count(o.getSupportCount());
                humanGeneSymbolDTOS.add(humanGeneSymbolDTO);
            });

        });

        projectDetailsDTO.setHumanGenes(humanGeneSymbolDTOS);
    }
}

