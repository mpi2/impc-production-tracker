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
import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.service.project.search.Search;
import uk.ac.ebi.impc_prod_tracker.service.project.search.SearchReport;
import uk.ac.ebi.impc_prod_tracker.web.controller.project.helper.ProjectFilter;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import java.util.List;
import java.util.Map;

public interface ProjectService
{
    Project getProjectByTpn(String tpn);

    @Deprecated
    Page<Project> getProjects(Pageable pageable, ProjectFilter projectFilter);
    List<Project>  getProjects(Map<FilterTypes, List<String>> filters);

    Project createProject(NewProjectRequestDTO newProjectRequestDTO);

    /**
     * Gets the history for a project
     * @param project The project.
     * @return List of {@link History} with the trace of the changes for a project.
     */
    List<History> getProjectHistory(Project project);
}
