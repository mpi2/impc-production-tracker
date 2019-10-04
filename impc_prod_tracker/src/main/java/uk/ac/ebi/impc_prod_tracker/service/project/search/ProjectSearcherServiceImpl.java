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
package uk.ac.ebi.impc_prod_tracker.service.project.search;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProjectSearcherServiceImpl implements ProjectSearcherService
{
    private Searcher searcher;
    private ProjectService projectService;

    public ProjectSearcherServiceImpl(Searcher searcher, ProjectService projectService)
    {
        this.searcher = searcher;
        this.projectService = projectService;
    }

    @Override
    public SearchReport executeSearch(Search search)
    {
        SearchReport searchReport;
        if (search.getSearchType() == null || search.getInputs().isEmpty())
        {
            searchReport = retrieveAllAvailableProjects();
        }
        else
        {
            searchReport = searcher.execute(search);
        }
        return searchReport;
    }

    private SearchReport retrieveAllAvailableProjects()
    {
        SearchReport searchReport = new SearchReport();

        Map<FilterTypes, List<String>> filters = new HashMap<>(0);
        List<Project> projects = projectService.getProjects(filters);
        List<SearchResult> searchResults = new ArrayList<>();
        projects.forEach(p -> searchResults.add(new SearchResult(null, p, null)));
        searchReport.setResults(searchResults);

        return searchReport;
    }
}
