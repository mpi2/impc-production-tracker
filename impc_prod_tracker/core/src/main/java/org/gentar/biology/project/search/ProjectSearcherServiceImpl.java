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
package org.gentar.biology.project.search;

import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.util.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        SearchReport searchReport = buildReportWithInitialData(search);
        List<SearchResult> results;

        if (search.getSearchType() == null || search.getInputs().isEmpty())
        {
            results = retrieveAllAvailableProjects(search.getFilters());
        }
        else
        {
            results = searcher.execute(search);
        }
        searchReport.setResults(results);
        return searchReport;
    }

    @Override
    public SearchReport executeSearch(Search search, Pageable pageable)
    {
        SearchReport searchReport = buildReportWithInitialData(search);
        List<SearchResult> results = new ArrayList<>();

        if (search.getSearchType() == null || search.getInputs().isEmpty())
        {
            Page<Project> paginatedProjects = getAllPaginatedProjects(search.getFilters(), pageable);
            PagedModel.PageMetadata pageMetadata = buildPageMetadata(paginatedProjects);
            searchReport.setPageMetadata(pageMetadata);
            addProjectsToResult(paginatedProjects.getContent(), results);
        }
        else
        {
            results = searcher.execute(search);
            Page<SearchResult> paginatedContent =
                PaginationHelper.createPage(results, pageable);
            PagedModel.PageMetadata pageMetadata = buildPageMetadata(paginatedContent);
            searchReport.setPageMetadata(pageMetadata);
        }
        searchReport.setResults(results);
        return searchReport;
    }

    private void addProjectsToResult(Collection<Project> projects, List<SearchResult> searchResults)
    {
        projects.forEach(p -> searchResults.add(new SearchResult(null, p, null)));
    }

    private SearchReport buildReportWithInitialData(Search search)
    {
        SearchReport searchReport = new SearchReport();
        searchReport.setDate(LocalDateTime.now());
        searchReport.setSearchType(search.getSearchType());
        searchReport.setFilters(search.getFilters().getNotNullFilterNames());
        searchReport.setInputs(search.getInputs());
        return searchReport;
    }

    private List<SearchResult> retrieveAllAvailableProjects(ProjectFilter filters)
    {
        List<Project> projects = projectService.getProjects(filters);
        List<SearchResult> searchResults = new ArrayList<>();
        projects.forEach(p -> searchResults.add(new SearchResult(null, p, null)));

        return searchResults;
    }

    private Page<Project> getAllPaginatedProjects(ProjectFilter filters, Pageable pageable)
    {
        return projectService.getProjects(filters, pageable);
    }

    private PagedModel.PageMetadata buildPageMetadata(Page<?> paginatedContent)
    {
        int numNulls = (int) paginatedContent.stream().filter(Objects::isNull).count();
        return new PagedModel.PageMetadata(
            paginatedContent.getSize(),
            paginatedContent.getNumber(),
            paginatedContent.getTotalElements(),
            paginatedContent.getTotalPages());
    }
}
