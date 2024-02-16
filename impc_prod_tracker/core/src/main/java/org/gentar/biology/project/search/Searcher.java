/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/

package org.gentar.biology.project.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

/**
 * Class in charge of taking a {@link Search} and return a {@link SearchReport} with the results of
 * the search.
 */
@Component
public class Searcher {
    private SearchExecutor searchExecutor;
    private final SearchExecutorByGene searchExecutorByGene;

    public Searcher(SearchExecutorByGene searchExecutorByGene) {
        this.searchExecutorByGene = searchExecutorByGene;
    }

    /**
     * Executes a search.
     *
     * @param search Parameters of the search.
     * @return List of {@link SearchResult} with the results of the search.
     */
    public List<SearchResult> execute(Search search) {
        resolveSearchExecutor(search);

        return getResults(search);
    }

    private void resolveSearchExecutor(Search search) {
        switch (search.getSearchType()) {
            case BY_GENE -> searchExecutor = searchExecutorByGene;
            case BY_LOCATION -> throw new NotImplementedException("Search by location not implemented yet.");
            default-> throw new IllegalArgumentException(
                    "Search [" + search.getSearchType().getName() + " not supported.");
        }
    }

    private List<SearchResult> getResults(Search search) {
        List<SearchResult> results = new ArrayList<>();
        for (String input : search.getInputs()) {
            results.addAll(getResultsByInput(input));
        }
        return getSortedByTpnSearchResult(results);

    }

    private List<SearchResult> getResultsByInput(String input) {
        List<SearchResult> foundProjects = searchExecutor.findProjects(input);

        if (foundProjects.isEmpty()) {
            addEmptyResult(foundProjects, input);
        }
        return foundProjects;
    }

    private void addEmptyResult(List<SearchResult> searchResults, String input) {
        SearchResult searchResult = new SearchResult();
        searchResult.setInput(input);
        searchResult.setProject(null);
        searchResults.add(searchResult);
    }

    private List<SearchResult> getSortedByTpnSearchResult(
        List<SearchResult> searchresults) {
        return
            searchresults.stream()
                .sorted(Comparator.comparing(
                    p -> p.getProject() != null ? p.getProject().getTpn() : p.getInput())).collect(
                Collectors.toList());
    }

}
