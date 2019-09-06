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
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;
import uk.ac.ebi.impc_prod_tracker.web.dto.common.history.HistoryDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.common.history.HistoryMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectMapper;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ProjectController
{
    private ProjectService projectService;
    private ProjectMapper projectMapper;
    private HistoryMapper historyMapper;

    ProjectController(
        ProjectService projectService,
        ProjectMapper projectMapper,
        HistoryMapper historyMapper)
    {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.historyMapper = historyMapper;
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
        // List<Plan> plans = planService.getPlansByProject(project);
        ProjectDTO projectDTO = projectMapper.projectToDTO(project);

        return new EntityModel<>(projectDTO);
    }

    /**
     *      * @api {post} / create a new project.
     */
    @PostMapping(value = {"/projects"})
    private ResponseEntity createProject(@RequestBody NewProjectRequestDTO newProjectRequestDTO)
    {
        Project newProject = projectService.createProject(newProjectRequestDTO);
        System.out.println("Project created => "+ newProject);
        return ok("Project created!");
    }

    @GetMapping(value = {"/projects/{tpn}/history"})
    public List<HistoryDTO> getProjectHistory(@PathVariable String tpn)
    {
        Project project = ProjectUtilities.getNotNullProjectByTpn(tpn);

        return historyMapper.toDtos(projectService.getProjectHistory(project));
    }
}
