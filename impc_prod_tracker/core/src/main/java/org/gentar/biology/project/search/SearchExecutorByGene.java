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
package org.gentar.biology.project.search;

import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.project.Project;
import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.project.ProjectService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
class SearchExecutorByGene implements SearchExecutor
{
    private ProjectService projectService;
    private GeneExternalService geneExternalService;

    SearchExecutorByGene(ProjectService projectService, GeneExternalService geneExternalService)
    {
        this.projectService = projectService;
        this.geneExternalService = geneExternalService;
    }

    @Override
    public List<SearchResult> findProjects(String searchTerm)
    {
        List<SearchResult> searchResults = findProjectsByGene(searchTerm);
        if (noProjectsFoundInInternalDatabase(searchResults) && !isAccessionId(searchTerm))
        {
            searchResults = searchGeneInExternalService(searchTerm);
        }
        return searchResults;
    }

    private List<SearchResult> findProjectsByGene(String searchTerm)
    {
        List<SearchResult> searchResults = new ArrayList<>();
        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withGenes(Arrays.asList(searchTerm)).build();
        List<Project> projects = projectService.getProjects(projectFilter);
        projects.forEach(p -> searchResults.add(new SearchResult(searchTerm, p, null)));
        return searchResults;
    }

    private boolean noProjectsFoundInInternalDatabase(List<SearchResult> searchResults)
    {
        return searchResults == null || searchResults.isEmpty();
    }

    private boolean isAccessionId(String searchTerm)
    {
        return searchTerm.contains(":");
    }

    private List<SearchResult> searchGeneInExternalService(String searchTerm)
    {
        List<SearchResult> searchResults = new ArrayList<>();
        if (!isSymbol(searchTerm))
        {
            searchResults = findProjectsBySynonym(searchTerm);
        }
        return searchResults;
    }

    // Checks if the search term is a "current symbol", meaning it exists in the mouse_gene
    // external table and therefore it is not a synonym.
    private boolean isSymbol(String searchTerm)
    {
        return geneExternalService.getGeneFromExternalDataBySymbolOrAccId(searchTerm) != null;
    }

    private List<SearchResult> findProjectsBySynonym(String searchTerm)
    {
        List<SearchResult> searchResults = new ArrayList<>();
        Gene synonym = geneExternalService.getSynonymFromExternalGenes(searchTerm);
        if (synonym == null)
        {
            SearchResult searchResult = getNoValidInputResult(searchTerm);
            searchResults.add(searchResult);
        }
        else
        {
            searchResults = findProjectsByGene(synonym.getAccId());
            if (searchResults.isEmpty())
            {
                addEmptyResult(searchResults);
            }
            searchResults.forEach(s ->
                {
                    s.setInput(searchTerm);
                    s.setComment("Synonym of " + synonym.getSymbol());
                });
        }
        return searchResults;
    }

    private void addEmptyResult(List<SearchResult> searchResults)
    {
        SearchResult searchResult = new SearchResult();
        searchResults.add(searchResult);
    }

    private SearchResult getNoValidInputResult(String searchTerm)
    {
        SearchResult searchResult = new SearchResult();
        searchResult.setInput(searchTerm);
        searchResult.setComment("Not a gene symbol or synonym");
        return searchResult;
    }
}
