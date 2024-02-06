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

import lombok.Getter;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.biology.project.search.filter.ProjectFilter;

import java.util.Collections;
import java.util.List;

/**
 * Defines the structure of a search.
 */
@Getter
public class Search
{
    private SearchType searchType;
    private List<String> inputs;
    private ProjectFilter filters;

    private static final String INVALID_SEARCH_TYPE =
        "Search type [%s] not recognized. Please use one of these values: %s.";
    private static final String NO_SEARCH_TYPE_DEFINED =
        "If you are specifying an input in the search you have to define also the search type.";

    private Search(){}

    public Search(String searchTypeName, List<String> inputs, ProjectFilter filters)
    {
        searchType = getSearchTypeBySearchTypeName(searchTypeName);
        validateSearchTypeDefinedIfThereIsInput(searchType, inputs);
        this.inputs = inputs == null ? Collections.emptyList() : inputs;
        this.filters = filters;
    }

    private SearchType getSearchTypeBySearchTypeName(String searchTypeName)
    {
        SearchType searchType = null;
        if (searchTypeName != null)
        {
            searchType = SearchType.valueOfName(searchTypeName);
            if (searchType == null)
            {
                throw new IllegalArgumentException(
                    String.format(
                        INVALID_SEARCH_TYPE, searchTypeName, SearchType.getValidValuesNames()));
            }
        }
        return searchType;
    }

    private void validateSearchTypeDefinedIfThereIsInput(SearchType searchType, List<String> inputs)
    {
        if (isInputProvided(inputs) && searchType == null)
        {
            throw new UserOperationFailedException(NO_SEARCH_TYPE_DEFINED);
        }
    }

    private boolean isInputProvided(List<String> inputs)
    {
        return inputs != null && !inputs.isEmpty();
    }
}
