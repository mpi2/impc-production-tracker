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

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class in charge of taking a {@link Search} and return a {@link SearchReport} with the results of
 * the search.
 */
@Component
public class Searcher
{
    private SearchExecutor searchExecutor;

    public SearchReport execute(Search search)
    {
        SearchReport searchReport = buildReportWithInitialData(search);
        resolveSearchExecutor(search);
        List<SearchResult> results = getResults(search);
        searchReport.setResults(results);

        return searchReport;
    }

    private SearchReport buildReportWithInitialData(Search search)
    {
        SearchReport searchReport = new SearchReport();
        searchReport.setDate(LocalDateTime.now());
        searchReport.setSearchType(search.getSearchType());
        searchReport.setSpeciesName(search.getSpeciesName());
        searchReport.setInputs(search.getInputs());
        searchReport.setFilters(search.getFilters());
        return searchReport;
    }

    private void resolveSearchExecutor(Search search)
    {
        switch (search.getSearchType())
        {
            case BY_GENE:
                searchExecutor = new SearchExecutorByGene();
                break;
            case BY_LOCATION:
                throw new NotImplementedException("Search by location not implemented yet.");
            default:
                throw new IllegalArgumentException(
                    "Search [" + search.getSearchType().getName() + " not supported.");
        }
    }

    private List<SearchResult> getResults(Search search)
    {
        List<SearchResult> results = new ArrayList<>();
        for (String input : search.getInputs())
        {
            results.addAll(getResultsByInput(input));
        }
        return results;
    }

    private List<SearchResult> getResultsByInput(String input)
    {
        List<Project> foundProjects = searchExecutor.findProjects(input);
        List<SearchResult> searchResults = new ArrayList<>();
        foundProjects.forEach(p -> searchResults.add(new SearchResult(input, p, null)));
        return searchResults;
    }
}
