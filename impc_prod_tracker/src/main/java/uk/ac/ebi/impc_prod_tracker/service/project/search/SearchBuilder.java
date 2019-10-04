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

import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SearchBuilder
{
    private String speciesName;
    private SearchType searchType;
    private List<String> inputs;
    private Map<FilterTypes, List<String>> filters;

    private static final String INVALID_SEARCH_TYPE =
        "Search type [%s] not recognized. Please use one of these values: %s.";

    private SearchBuilder() {}

    public static SearchBuilder getInstance()
    {
        return new SearchBuilder();
    }

    public Search build()
    {
        Search search = new Search();
        search.setSearchType(searchType);
        search.setInputs(inputs);
        search.setFilters(filters);
        return search;
    }

    public SearchBuilder withSearchType(String searchTypeName)
    {
        if (searchTypeName == null)
        {
            this.searchType = null;
        }
        else
        {
            this.searchType = SearchType.valueOfName(searchTypeName);
            if (this.searchType == null)
            {
                throw new IllegalArgumentException(
                    String.format(
                        INVALID_SEARCH_TYPE, searchTypeName, SearchType.getValidValuesNames()));
            }

        }

        return this;
    }

    public SearchBuilder withInputs(List<String> inputs)
    {
        this.inputs = inputs == null ? Collections.emptyList(): inputs;
        return this;
    }

    public SearchBuilder withTpns(List<String> tpns)
    {
        filters.put(FilterTypes.TPN, tpns);
        return this;
    }

    public SearchBuilder withWorkUnitNames(List<String> workUnitNames)
    {
        filters.put(FilterTypes.WORK_UNIT_NAME, workUnitNames);
        return this;
    }
}
