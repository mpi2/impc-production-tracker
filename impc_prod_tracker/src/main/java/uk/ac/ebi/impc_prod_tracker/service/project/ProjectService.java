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
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import java.util.List;

public interface ProjectService
{
    /**
     * Get all the projects that a user has access to. The visibility of a project is given by the
     * existence of at least one plan in the project that has a work unit that matches with the
     * user's work unit.
     * @param pageable Pageable information.
     * @param consortiaNames Optional list of consortia names to filter the results.
     *                       If null no filters are applied.
     * @param statusesNames Optional list of statuses names to filter the results.
     *                      If null no filters are applied.
     * @param privaciesNames Optional list of privacies names to filter the results.
     *                       If null no filters are applied.
     * @return Paginated projects.
     */
    Page<Project> getCurrentUserProjects(
        Pageable pageable,
        List<String> consortiaNames,
        List<String> statusesNames,
        List<String> privaciesNames);

    Project getCurrentUserProjectByTpn(String tpn);

    Page<Project> getProjects(
        Pageable pageable,
        List<String> consortiaNames,
        List<String> statusesNames,
        List<String> privaciesNames);

    Project createProject(NewProjectRequestDTO newProjectRequestDTO);

    /**
     * Gets the history for a project
     * @param project The project.
     * @return List of {@link History} with the trace of the changes for a project.
     */
    List<History> getProjectHistory(Project project);
}
