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

package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.search.SearchResult;
import org.gentar.biology.project.search.SearchResultDTO;
import org.springframework.stereotype.Component;

@Component
public class SearchResultMapper implements Mapper<SearchResult, SearchResultDTO> {
    private final ProjectResponseMapper projectResponseMapper;

    public SearchResultMapper(ProjectResponseMapper projectResponseMapper) {
        this.projectResponseMapper = projectResponseMapper;
    }

    public SearchResultDTO toDto(SearchResult searchResult) {
        SearchResultDTO searchResultDTO = new SearchResultDTO();
        searchResultDTO.setInput(searchResult.getInput());
        searchResultDTO.setComment(searchResult.getComment());
        Project project = searchResult.getProject();

        if (project != null && isProjectGeneExist(project)) {
            searchResultDTO.setProject(projectResponseMapper.toDto(project));
        }
        return searchResultDTO;
    }

    private boolean isProjectGeneExist(Project project) {
        return project.getProjectIntentions().stream()
            .anyMatch(p -> p.getProjectIntentionGene() != null);
    }
}
