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
package org.gentar.web.controller.project;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.helpers.PaginationHelper;
import org.gentar.service.biology.project.ProjectService;
import org.gentar.web.controller.common.PlanLinkBuilder;
import org.gentar.web.controller.util.LinkUtil;
import org.gentar.web.dto.common.history.HistoryDTO;
import org.gentar.web.dto.project.ProjectDTO;
import org.gentar.web.mapping.common.history.HistoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.gentar.biology.project.Project;
import org.gentar.service.biology.project.search.ProjectFilter;
import org.gentar.service.biology.project.search.ProjectFilterBuilder;
import org.gentar.web.controller.project.helper.ProjectUtilities;
import org.gentar.web.mapping.project.ProjectDtoToEntityMapper;
import org.gentar.web.mapping.project.ProjectEntityToDtoMapper;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
class ProjectController
{
    private ProjectService projectService;
    private ProjectEntityToDtoMapper projectEntityToDtoMapper;
    private HistoryMapper historyMapper;
    private ProjectDtoToEntityMapper projectDtoToEntityMapper;

    private static final String PROJECT_NOT_FOUND_ERROR =
        "Project %s does not exist or you don't have access to it.";

    ProjectController(
        ProjectService projectService,
        ProjectEntityToDtoMapper projectEntityToDtoMapper,
        HistoryMapper historyMapper,
        ProjectDtoToEntityMapper projectDtoToEntityMapper)
    {
        this.projectService = projectService;
        this.projectEntityToDtoMapper = projectEntityToDtoMapper;
        this.historyMapper = historyMapper;
        this.projectDtoToEntityMapper = projectDtoToEntityMapper;
    }

    /**
     * Get all the projects in the system.
     * @return A collection of {@link ProjectDTO} objects.
     */
    @GetMapping
    public ResponseEntity findAll(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam(value = "tpn", required = false) List<String> tpns,
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols,
        @RequestParam(value = "intention", required = false) List<String> intentions,
        @RequestParam(value = "workUnitName", required = false) List<String> workUnitNames,
        @RequestParam(value = "consortium", required = false) List<String> consortia,
        @RequestParam(value = "status", required = false) List<String> statuses,
        @RequestParam(value = "privacyName", required = false) List<String> privaciesNames)
    {
        ProjectFilter projectFilter = ProjectFilterBuilder.getInstance()
            .withTpns(tpns)
            .withMarkerSymbols(markerSymbols)
            .withIntentions(intentions)
            .withStatuses(statuses)
            .withPrivacies(privaciesNames)
            .withWorkUnitNames(workUnitNames)
            .build();
        List<Project> projects = projectService.getProjects(projectFilter);
        Page<Project> paginatedContent =
            PaginationHelper.createPage(projects, pageable);
        Page<ProjectDTO> projectDtos = paginatedContent.map(this::getDTO);
        PagedModel pr =
            assembler.toModel(
                projectDtos,
                linkTo(ProjectController.class).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    private ProjectDTO getDTO(Project project)
    {
        ProjectDTO projectDTO = null;
        if (project != null)
        {
            projectDTO = projectEntityToDtoMapper.toDto(project);
            projectDTO.add(
                PlanLinkBuilder.buildPlanLinks(project, PlanTypes.PRODUCTION, "productionPlans"));
            projectDTO.add(
                PlanLinkBuilder.buildPlanLinks(project, PlanTypes.PHENOTYPING, "phenotypingPlans"));
        }
        return projectDTO;
    }

    /**
     * Get a specific project.
     * @param tpn tpn Project identifier.
     * @return Entity with the project information.
     */
    @GetMapping(value = {"/{tpn}"})
    public EntityModel<?> findOne(@PathVariable String tpn)
    {
        EntityModel<ProjectDTO> entityModel;
        Project project = projectService.getProjectByTpn(tpn);
        ProjectDTO projectDTO = getDTO(project);

        if (projectDTO != null)
        {
            entityModel = new EntityModel<>(projectDTO);
        }
        else
        {
            //TODO: Exception not found
            throw new UserOperationFailedException(
                String.format(PROJECT_NOT_FOUND_ERROR, tpn));
        }

        return entityModel;
    }
    /**
     *      * @api {post} / create a new project.
     */
    @PostMapping
    private ProjectDTO createProject(@RequestBody ProjectDTO projectDTO)
    {

        Project projectToBeCreated = projectDtoToEntityMapper.toEntity(projectDTO);
        Project createdProject = projectService.createProject(projectToBeCreated);
        System.out.println("Project created => "+ createdProject);
        return projectEntityToDtoMapper.toDto(createdProject);
    }

    @GetMapping(value = {"/{tpn}/history"})
    public List<HistoryDTO> getProjectHistory(@PathVariable String tpn)
    {
        Project project = ProjectUtilities.getNotNullProjectByTpn(tpn);

        return historyMapper.toDtos(projectService.getProjectHistory(project));
    }
}
