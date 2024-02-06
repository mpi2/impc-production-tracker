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

import lombok.Data;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class SearchReport
{
    private LocalDateTime date;
    private SearchType searchType;
    private List<String> inputs;
    private Map<String, List<String>> filters;
    private List<SearchResult> results;
    private PagedModel.PageMetadata pageMetadata;
}
