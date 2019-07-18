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
import org.springframework.data.jpa.domain.Specification;
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
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
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
    public CollectionModel<ProjectDTO> getAllProjects()
    {
        List<Project> projects = projectService.getProjects();

        List<ProjectDTO> projectDTOList = projects.stream()
            .map(
                project -> projectDTOLinkManager.addLinks(
                    projectDTOBuilder.buildProjectDTOFromProject(project)))
            .collect(Collectors.toList());

        return new CollectionModel<>(projectDTOList);
    }

    @GetMapping(value = {"/hello"})
    public String test(@RequestParam String mje)
    {
        return "Hello "+ mje;
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
    public EntityModel<ProjectPlanDTO> getProjectPlan(
        @PathVariable String tpn, @PathVariable String pin)
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

    /**
     * Returns Information about the projects with pagination.
     *
     * @param markerSymbols Optional filters with specific marker symbols.
     * @param workUnits Optional filters with specific workUnits names.
     * @param workGroups Optional filters with specific marker names.
     * @param pageable      Pagination options.
     * @param assembler     Assembler to add links to the resources.
     * @return List of ProjectSummaryDTO.
     */
    @GetMapping(value = {"/projectSummaries"})
    public ResponseEntity getProjectSummariesPaginated(
        @RequestParam(value = "markerSymbols", required = false) List<String> markerSymbols,
        @RequestParam(value = "workUnit", required = false) List<String> workUnits,
        @RequestParam(value = "workGroup", required = false) List<String> workGroups,
        @RequestParam(value = "planType", required = false) List<String> planTypes,
        @RequestParam(value = "status", required = false) List<String> statuses,
        @RequestParam(value = "privacy", required = false) List<String> privacies,
//        @RequestParam(value = "priority", required = false) List<String> priorities,
        Pageable pageable,
        PagedResourcesAssembler assembler)
    {
//        Specification<Project> projectSpecification =
//            Specification.where(ProjectSpecs.getProjectsByMarkerSymbol(markerSymbols)
//                .and(ProjectSpecs.getProjectsByWorkUnitNames(workUnits)
//                .and(ProjectSpecs.getProjectsByWorkGroup(workGroups)
//                .and(ProjectSpecs.getProjectsByPlanType(planTypes)
//                .and(ProjectSpecs.getProjectsByStatus(statuses)
//                .and(ProjectSpecs.getProjectsByPrivacy(privacies)
//                .and(ProjectSpecs.getProjectsByPriority(priorities))))))));

        System.out.println("/n/n marker_symbol => " + markerSymbols);

        Specification<Project> projectSpecification =
                Specification.where(ProjectSpecs.getProjectsByMarkerSymbol(markerSymbols)
                        .and(ProjectSpecs.getProjectsByWorkUnitNames(workUnits)
                        .and(ProjectSpecs.getProjectsByWorkGroup(workGroups)
                        .and(ProjectSpecs.getProjectsByPlanType(planTypes)
                        .and(ProjectSpecs.getProjectsByStatus(statuses)
                        .and(ProjectSpecs.getProjectsByPrivacy(privacies)))))));

        Page<Project> projects = projectService.getProjectsBySpecPro(projectSpecification, pageable);
        Page<Project> filteredProjects = projects.map(p ->
            projectService.getProjectFilteredByPlanAttributes(
                p, workUnits, workGroups, planTypes, statuses, privacies));
        Page<ProjectSummaryDTO> planSummaryDTOPage =
            filteredProjects.map(this::convertToProjectSummaryDTO);

        PagedModel pr =
            assembler.toModel(
                planSummaryDTOPage,
                linkTo(PlanController.class).slash("/planSummaries").withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = {"/projectSummaries/{tpn}"})
    public EntityModel<ProjectSummaryDTO> getProjectSummary(@PathVariable String tpn)
    {
        Project project = getNotNullProjectByTpn(tpn);
        ProjectSummaryDTO projectSummaryDTO = convertToProjectSummaryDTO(project);

        return new EntityModel<>(projectSummaryDTO);
    }

    private Project getNotNullProjectByTpn(String tpn)
    {
        Project project = projectService.getProjectByTpn(tpn);
        if (project == null)
        {
            throw new OperationFailedException(
                String.format("Project %s does not exist.", tpn), HttpStatus.NOT_FOUND);
        }
        return project;
    }

    private ProjectSummaryDTO convertToProjectSummaryDTO(final Project project)
    {
        ProjectSummaryDTO projectSummaryDTO = new ProjectSummaryDTO();

        projectSummaryDTO.setProjectDetailsDTO(
            projectDTOBuilder.buildProjectDetailsDTOFromProject(project));
        addPlans(projectSummaryDTO, project);

        return projectSummaryDTO;
    }

    private String createLinkHeader(PagedModel<ProjectSummaryDTO> pr)
    {
        final StringBuilder linkHeader = new StringBuilder();
        if (!pr.getLinks("first").isEmpty())
        {
            linkHeader.append(buildLinkHeader(pr.getLinks("first").get(0).getHref(), "first"));
            linkHeader.append(", ");
        }
        if (!pr.getLinks("next").isEmpty())
        {
            linkHeader.append(buildLinkHeader(pr.getLinks("next").get(0).getHref(), "next"));
        }
        return linkHeader.toString();
    }

    public static String buildLinkHeader(final String uri, final String rel)
    {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    private void addPlans(ProjectSummaryDTO projectSummaryDTO, final Project project)
    {
        List<Plan> plans = planService.getPlansByProject(project);

        List<PlanDetailsDTO> plansDTO = new ArrayList<PlanDetailsDTO>();

        for (Plan p : plans)
        {
            PlanDetailsDTO planDTO = planDTOBuilder.buildPlanDetailsDTOFromPlan(p);
            plansDTO.add(planDTO);
        }
        projectSummaryDTO.setPlanDetailsDTO(plansDTO);
    }

    /**
     *      * @api {post} / create a new project.
     */
    @PostMapping(value = {"/createProject"})
    private ResponseEntity createProject(@RequestBody NewProjectRequestDTO newProjectRequestDTO) {
        Project newProject = projectService.createProject(newProjectRequestDTO);
        System.out.println("Project created => "+ newProject);
        return ok("Project created!");

    }
}
