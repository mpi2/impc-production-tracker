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
package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.common.types.PlanTypes;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;
import uk.ac.ebi.impc_prod_tracker.web.controller.common.PlanLinkBuilder;
import uk.ac.ebi.impc_prod_tracker.web.controller.util.LinkUtil;
import uk.ac.ebi.impc_prod_tracker.web.dto.common.history.HistoryDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.common.history.HistoryMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectMapper;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
class ProjectController
{
    private ProjectService projectService;
    private ProjectMapper projectMapper;
    private HistoryMapper historyMapper;
    private ProjectSpecs projectSpecs;

    ProjectController(
        ProjectService projectService,
        ProjectMapper projectMapper,
        HistoryMapper historyMapper,
        ProjectSpecs projectSpecs)
    {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.historyMapper = historyMapper;
        this.projectSpecs = projectSpecs;
    }

    /**
     * Get all the projects in the system.
     * @return A collection of {@link ProjectDTO} objects.
     */
    @GetMapping
    public ResponseEntity findAll(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam(value = "consortium", required = false) List<String> consortia,
        @RequestParam(value = "status", required = false) List<String> statuses,
        @RequestParam(value = "privacy", required = false) List<String> privacies)
    {
        Specification<Project> specifications =
            buildSpecificationsWithCriteria(consortia, statuses, privacies);
        Page<Project> projects = projectService.getProjects(specifications, pageable);
        Page<ProjectDTO> projectDtos =
            projects.map(this::getDTO);
        PagedModel pr =
            assembler.toModel(
                projectDtos,
                linkTo(ProjectSummaryController.class).slash("projects").withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    private Specification<Project> buildSpecificationsWithCriteria(
        List<String> consortia, List<String> statuses, List<String> privacies)
    {
        Specification<Project> specifications =
            Specification.where(projectSpecs.withPlansInUserWorkUnits())
                .and(projectSpecs.withConsortia(consortia))
                .and(projectSpecs.withStatuses(statuses))
                .and(projectSpecs.withPrivacies(privacies));
        return specifications;
    }

    private ProjectDTO getDTO(Project project)
    {
        ProjectDTO projectDTO = projectMapper.toDto(project);
        projectDTO.add(
            PlanLinkBuilder.buildPlanLinks(project, PlanTypes.PRODUCTION, "production_plans"));
        projectDTO.add(
            PlanLinkBuilder.buildPlanLinks(project, PlanTypes.PHENOTYPING, "phenotyping_plans"));
        return projectDTO;
    }

    /**
     * Get a specific project.
     * @param tpn tpn Project identifier.
     * @return Entity with the project information.
     */
    @GetMapping(value = {"/{tpn}"})
    public EntityModel<ProjectDTO> findOne(@PathVariable String tpn)
    {
        Project project = ProjectUtilities.getNotNullProjectByTpn(tpn);
        ProjectDTO projectDTO = getDTO(project);

        return new EntityModel<>(projectDTO);
    }
    /**
     *      * @api {post} / create a new project.
     */
    @PostMapping
    private ResponseEntity createProject(@RequestBody NewProjectRequestDTO newProjectRequestDTO)
    {
        Project newProject = projectService.createProject(newProjectRequestDTO);
        System.out.println("Project created => "+ newProject);
        return ok("Project created!");
    }

    @GetMapping(value = {"/{tpn}/history"})
    public List<HistoryDTO> getProjectHistory(@PathVariable String tpn)
    {
        Project project = ProjectUtilities.getNotNullProjectByTpn(tpn);

        return historyMapper.toDtos(projectService.getProjectHistory(project));
    }
}
