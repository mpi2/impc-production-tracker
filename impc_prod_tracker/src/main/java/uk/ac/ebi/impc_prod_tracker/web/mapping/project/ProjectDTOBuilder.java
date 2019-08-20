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
import uk.ac.ebi.impc_prod_tracker.data.biology.project_gene.ProjectGene;
import uk.ac.ebi.impc_prod_tracker.service.GeneService;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.PlanDTOBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@Data
public class ProjectDTOBuilder
{
    private PlanService planService;
    private PlanDTOBuilder planDTOBuilder;
    private GeneService geneService;
    private static final String MGI_URL = "http://www.mousephenotype.org/data/genes/";

    public ProjectDTOBuilder(
        PlanService planService,
        PlanDTOBuilder planDTOBuilder,
        GeneService geneService)
    {
        this.planService = planService;
        this.planDTOBuilder = planDTOBuilder;
        this.geneService = geneService;
    }

    public ProjectDTO buildProjectDTOFromProject(Project project)
    {
        Objects.requireNonNull(project, "The project is null");
        ProjectDTO projectDTO = new ProjectDTO();
        if (project.getAssignmentStatus() != null)
        {
            projectDTO.setAssigmentStatusName(project.getAssignmentStatus().getName());
        }

        projectDTO.setTpn(project.getTpn());

//        addMarkerSymbols(projectDetailsDTO, project);
//        addIntentions(projectDetailsDTO, project);
//
//        addHumanGenes(projectDetailsDTO, project);

        return projectDTO;
    }

    private ProjectDTO convertToDto(Project project)
    {
        Objects.requireNonNull(project, "The project is null");
        ProjectDTO projectDTO = buildProjectDTOFromProject(project);
        projectDTO.setAssigmentStatusName(project.getAssignmentStatus().getName());
        projectDTO.setTpn(project.getTpn());

        return projectDTO;
    }

    private void addIntentions(ProjectDTO projectDTO, final Project project)
    {
        Set<ProjectGene> projectGeneSet = project.getProjectGenes();

        List<String> intentions = new ArrayList<>();

        projectGeneSet.forEach(x -> intentions.add(x.getAlleleType().getName()) );

//        projectDTO.setAlleleIntentions(intentions);
    }

    private void addHumanGenes(ProjectDTO projectDTO, final Project project)
    {
        // TODO mapp with orthologues from external data
//        Set<ProjectGene> projectMouseGeneSet = project.getProjectGenes();
//
//        List<String> mouseMgiIds = new ArrayList<>();
//
//        projectMouseGeneSet.forEach(x -> mouseMgiIds.add(x.getGene().getMgiId()) );
//
//        List<ProjectDTO.HumanGeneSymbolDTO> humanGeneSymbolDTOS = new ArrayList<>();

//        mouseMgiIds.forEach(x -> {
//            Set<Ortholog> orthologs = geneService.getMouseGenesByMgiId(x).getOrthologs();
//            orthologs.forEach(o -> {
//                ProjectDetailsDTO.HumanGeneSymbolDTO humanGeneSymbolDTO = new ProjectDetailsDTO.HumanGeneSymbolDTO();
//                humanGeneSymbolDTO.setSymbol(o.getHumanGene().getSymbol());
//                humanGeneSymbolDTO.setSupport(o.getSupport());
//                humanGeneSymbolDTO.setSupport_count(o.getSupportCount());
//                humanGeneSymbolDTOS.add(humanGeneSymbolDTO);
//            });
//
//        });

//        projectDetailsDTO.setHumanGenes(humanGeneSymbolDTOS);
    }
}

