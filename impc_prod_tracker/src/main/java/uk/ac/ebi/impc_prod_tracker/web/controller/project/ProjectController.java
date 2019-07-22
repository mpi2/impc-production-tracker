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

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectPlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.PlanMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectMapper;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ProjectController
{
    private ProjectService projectService;
    private PlanService planService;
    private ProjectMapper projectMapper;
    private PlanMapper planMapper;

    ProjectController(
        ProjectService projectService,
        PlanService planService,
        ProjectMapper projectMapper,
        PlanMapper planMapper)
    {
        this.projectService = projectService;
        this.planService = planService;
        this.projectMapper = projectMapper;
        this.planMapper = planMapper;
    }

    /**
     * Get all the projects in the system.
     * @return A collection of {@link ProjectDTO} objects.
     */
    @GetMapping(value = {"/projects"})
    CollectionModel<ProjectDTO> getAllProjects()
    {
        List<Project> projects = projectService.getProjects();
        List<ProjectDTO> projectDTOList = projectMapper.projectsToDTOs(projects);
        return new CollectionModel<>(projectDTOList);
    }

    /**
     * Get a specific project.
     * @param tpn Project identifier.
     * @return Entity with the project information.
     */
    @GetMapping(value = {"/projects/{tpn}"})
    EntityModel<ProjectDTO> getProject(@PathVariable String tpn)
    {
        Project project = ProjectUtilities.getNotNullProjectByTpn(tpn);
        ProjectDTO projectDTO = projectMapper.projectToDTO(project);

        return new EntityModel<>(projectDTO);
    }

    @GetMapping(value = {"/projects/{tpn}/plans/{pin}"})
    EntityModel<ProjectPlanDTO> getProjectPlan(
        @PathVariable String tpn, @PathVariable String pin)
    {
        Project project = ProjectUtilities.getNotNullProjectByTpn(tpn);
        ProjectPlanDTO projectPlanDTO = new ProjectPlanDTO();
        Optional<Plan> planOpt = findPlanInProject(project, pin);
        if (planOpt.isPresent())
        {
            projectPlanDTO = planMapper.planToProjectPlanDTO(planOpt.get(), project);
        }
        return new EntityModel<>(projectPlanDTO);
    }

    private Optional<Plan> findPlanInProject(Project project, String pin)
    {
        List<Plan> plans = planService.getPlansByProject(project);
        return plans.stream().filter(x -> pin.equals(x.getPin())).findFirst();
    }

    /**
     *      * @api {post} / create a new project.
     */
    @PostMapping(value = {"/createProject"})
    private ResponseEntity createProject(@RequestBody NewProjectRequestDTO newProjectRequestDTO)
    {
        Project newProject = projectService.createProject(newProjectRequestDTO);
        System.out.println("Project created => "+ newProject);
        return ok("Project created!");
    }
}
