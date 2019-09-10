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
package uk.ac.ebi.impc_prod_tracker.service.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;

import java.util.List;

public interface ProjectService
{
    /**
     * Get all projects.
     * @return
     */
    List<Project> getProjects();
    Page<Project> getProjects(Pageable pageable);

    /**
     * Get the project identified by a specific tpn.
     * @param tpn Project identifier.
     * @return A project with the specific identifier (tpn).
     */
    Project getProjectByTpn(String tpn);

    /**
     * Get paginated projects filtering with the criteria defined in specification.
     * @param specification Filters to apply to the projects to return.
     * @param pageable Pagination information.
     * @return Paginated Projects filtered with criteria defined in specification.
     */
    Page<Project> getProjects(Specification<Project> specification, Pageable pageable);

    /**
     * 
     * @param project Project to filter
     * @param workUnits Work unit name list.
     * @param workGroups Work group name list.
     * @param planTypes Plan type name list.
     * @param statuses Statuses name list.
     * @return Filtered project.
     */
    Project getProjectFilteredByPlanAttributes(
        Project project,
        List<String> workUnits,
        List<String> workGroups,
        List<String> planTypes,
        List<String> statuses
    );

    Project createProject(NewProjectRequestDTO newProjectRequestDTO);

    /**
     * Gets the history for a project
     * @param project The project.
     * @return List of {@link History} with the trace of the changes for a project.
     */
    List<History> getProjectHistory(Project project);
}
