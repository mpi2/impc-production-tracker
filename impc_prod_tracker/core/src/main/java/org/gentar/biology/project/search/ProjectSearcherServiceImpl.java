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

import org.gentar.biology.project.search.filter.FluentSearchResultFilter;
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

    public ProjectSearcherServiceImpl(
        Searcher searcher,
        ProjectService projectService)
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
        var filteredResults = applyFiltersToResults(results, search.getFilters());
        searchReport.setResults(filteredResults);
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
            var allResults = searcher.execute(search);
            var filteredResults = applyFiltersToResults(allResults, search.getFilters());
            Page<SearchResult> paginatedContent =
                PaginationHelper.createPage(filteredResults, pageable);
            PagedModel.PageMetadata pageMetadata = buildPageMetadata(paginatedContent);
            searchReport.setPageMetadata(pageMetadata);
            results = paginatedContent.getContent();
        }
        searchReport.setResults(results);
        return searchReport;
    }

    private List<SearchResult> applyFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        List<SearchResult> allFilteredResults= applyTpnsFiltersToResults(allResults ,filters);
        allFilteredResults= applyWorkUnitNamesFiltersToResults(allFilteredResults ,filters);
        allFilteredResults= applyWorkGroupNamesFiltersToResults(allFilteredResults ,filters);
        allFilteredResults= applyMolecularMutationTypeNamesFiltersToResults(allFilteredResults ,filters);
        allFilteredResults= applyConsortiaNamesFiltersToResults(allFilteredResults ,filters);
        allFilteredResults= applyPrivaciesNamesFiltersToResults(allFilteredResults ,filters);
        allFilteredResults= applyImitsMiPlansFiltersToResults(allFilteredResults ,filters);
        allFilteredResults= applyColonyNamesFiltersToResults(allFilteredResults ,filters);
        allFilteredResults= applyPhenotypingExternalRefsFiltersToResults(allFilteredResults ,filters);

        return allFilteredResults;


    }

    private List<SearchResult> applyTpnsFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)
            .withTpns(filters.getTpns())
            .getFilteredData();
    }

    private List<SearchResult> applyWorkUnitNamesFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)
            .withWorkUnitNames(filters.getWorkUnitNames())
            .getFilteredData();
    }

    private List<SearchResult> applyWorkGroupNamesFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)

            .withWorkGroupNames(filters.getWorGroupNames())

            .getFilteredData();


    }

    private List<SearchResult> applyMolecularMutationTypeNamesFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)

            .withMolecularMutationTypeNames(filters.getIntentions())

            .getFilteredData();


    }

    private List<SearchResult> applyConsortiaNamesFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)
            .withConsortiaNames(filters.getConsortiaNames())
            .getFilteredData();
    }

    private List<SearchResult> applyPrivaciesNamesFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)
            .withPrivaciesNames(filters.getPrivaciesNames())
            .getFilteredData();
    }

    private List<SearchResult> applyImitsMiPlansFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)
            .withImitsMiPlans(filters.getImitsMiPlans())
            .getFilteredData();
    }

    private List<SearchResult> applyColonyNamesFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)
            .withColonyNames(filters.getProductionColonyNames())
            .getFilteredData();
    }

    private List<SearchResult> applyPhenotypingExternalRefsFiltersToResults(List<SearchResult> allResults, ProjectFilter filters)
    {
        return new FluentSearchResultFilter(allResults)
            .withPhenotypingExternalRefs(filters.getPhenotypingExternalRefs())
            .getFilteredData();
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
        return new PagedModel.PageMetadata(
            paginatedContent.getSize(),
            paginatedContent.getNumber(),
            paginatedContent.getTotalElements(),
            paginatedContent.getTotalPages());
    }
}
