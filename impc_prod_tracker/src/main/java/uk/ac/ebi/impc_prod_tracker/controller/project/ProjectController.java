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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.*;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200"})
public class ProjectController
{
    private ProjectService projectService;
    private PlanService planService;
    private PlanDTOBuilder planDTOBuilder;
    private ProjectDTOBuilder projectDTOBuilder;
    private ProjectDTOLinkManager projectDTOLinkManager;
    private PlanDTOLinkManager planDTOLinkManager;

    ProjectController(
        ProjectService projectService,
        ProjectDTOBuilder projectDTOBuilder,
        PlanDTOBuilder planDTOBuilder,
        PlanService planService,
        ProjectDTOLinkManager projectDTOLinkManager,
        PlanDTOLinkManager planDTOLinkManager)
    {
        this.projectService = projectService;
        this.projectDTOBuilder = projectDTOBuilder;
        this.planDTOBuilder = planDTOBuilder;
        this.planService = planService;
        this.projectDTOLinkManager = projectDTOLinkManager;
        this.planDTOLinkManager = planDTOLinkManager;
    }

    @GetMapping(value = {"/projects"})
    public CollectionModel<ProjectDTO> getPlansMapNew()
    {
        List<Project> projects = projectService.getProjects();

        List<ProjectDTO> projectDTOList = projects.stream()
            .map(
                project -> projectDTOLinkManager.addLinks(
                    projectDTOBuilder.buildProjectDTOFromProject(project)))
            .collect(Collectors.toList());

        return new CollectionModel<>(projectDTOList);
    }

    @GetMapping(value = {"/projects/{tpn}"})
    public EntityModel<ProjectDTO> getProjectsY(@PathVariable String tpn)
    {
        Project project = projectService.getProjectByTpn(tpn);
        if (project == null)
        {
            throw new OperationFailedException(
                String.format("The project %s does not exist", tpn));
        }
        ProjectDTO projectDTO = projectDTOBuilder.buildProjectDTOFromProject(project);
        projectDTOLinkManager.addLinks(projectDTO);

        return new EntityModel<>(projectDTO);
    }

    @GetMapping(value = {"/projects/{tpn}/plans/{pin}"})
    public EntityModel<ProjectPlanDTO> getProjectPlan(@PathVariable String tpn, @PathVariable String pin)
    {
        Project project = projectService.getProjectByTpn(tpn);
        if (project == null)
        {
            throw new OperationFailedException(
                String.format("The project %s does not exist", tpn));
        }
        List<Plan> plans = planService.getPlansByProject(project);
        PlanDTO planDTO = null;
        for (Plan plan : plans)
        {
            if (plan.getPin().equals(pin))
            {
                planDTO = planDTOBuilder.buildPlanDTOFromPlan(plan);
                break;
            }
        }
        if (planDTO == null)
        {
            throw new OperationFailedException(
                String.format("Project %s does not have any plan %s associated", tpn, pin),
                HttpStatus.NOT_FOUND);
        }

        planDTOLinkManager.addLinks(planDTO);

        ProjectPlanDTO projectPlanDTO = new ProjectPlanDTO();
        projectPlanDTO.setProjectDetailsDTO(projectDTOBuilder.buildProjectDetailsDTOFromProject(project));
        projectPlanDTO.setPlanDTO(planDTO);
        return new EntityModel<>(projectPlanDTO);
    }

    @GetMapping(value = {"/projectSummaries"})
    public ResponseEntity<PagedModel<ProjectSummaryDTO>> getPlanSummariesPaginated(
            Pageable pageable, PagedResourcesAssembler assembler)
    {
        Page<Project> projects = projectService.getPaginatedProjects(pageable);
        Page<ProjectSummaryDTO> planSummaryDTOPage = projects.map(this::convertToProjectSummaryDTO);

        PagedModel pr =
                assembler.toModel(
                        planSummaryDTOPage,
                        linkTo(PlanController.class).slash("/planSummaries").withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link",createLinkHeader(pr));

        return new ResponseEntity<>(pr,responseHeaders,HttpStatus.OK);

    }

//    @GetMapping(value = {"/projectSummaries/{pin}"})
//    public ProjectSummaryDTO getProjectSummary(@PathVariable String pin)
//    {
//        Plan plan = getNotNullPlanByPin(pin);
//        ProjectSummaryDTO projectSummaryDTO = convertToProjectSummaryDTO(plan);
//
//        return projectSummaryDTO;
//    }

    private Plan getNotNullPlanByPin(String pin)
    {
        Plan plan = projectDTOBuilder.getPlanService().getPlanByPin(pin);
        if (plan == null)
        {
            throw new OperationFailedException(
                    String.format("Plan %s does not exist.", pin), HttpStatus.NOT_FOUND);
        }
        return plan;
    }

    private ProjectSummaryDTO convertToProjectSummaryDTO(final Project project)
    {
        ProjectSummaryDTO projectSummaryDTO = new ProjectSummaryDTO();
        addPlans(projectSummaryDTO, project);

        projectSummaryDTO.setProjectDetailsDTO(
                projectDTOBuilder.buildProjectDetailsDTOFromProject(project));
        return projectSummaryDTO;
    }

    private String createLinkHeader(PagedModel<ProjectSummaryDTO> pr){
        final StringBuilder linkHeader = new StringBuilder();
        if (!pr.getLinks("first").isEmpty())
        {
            linkHeader.append(buildLinkHeader( pr.getLinks("first").get(0).getHref(),"first"));
            linkHeader.append(", ");
        }
        if (!pr.getLinks("next").isEmpty())
        {
            linkHeader.append(buildLinkHeader(pr.getLinks("next").get(0).getHref(),"next"));
        }
        return linkHeader.toString();
    }

    public static String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    private void addPlans(ProjectSummaryDTO projectSummaryDTO, final Project project)
    {
        List<Plan> plans = planService.getPlansByProject(project);

        List<PlanDetailsDTO> plansDTO = new ArrayList<PlanDetailsDTO>();

        for (Plan p: plans) {
            PlanDetailsDTO planDTO = planDTOBuilder.buildPlanDetailsDTOFromPlan(p);
            plansDTO.add(planDTO);
        }
        projectSummaryDTO.setPlanDetailsDTO(plansDTO);
    }
}
