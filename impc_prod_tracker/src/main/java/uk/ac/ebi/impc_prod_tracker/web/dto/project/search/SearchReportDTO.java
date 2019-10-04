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
package uk.ac.ebi.impc_prod_tracker.web.dto.project.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Representation of a Search Report.
 */
@Data
public class SearchReportDTO extends RepresentationModel
{
    private LocalDateTime date;
    private String speciesName;
    private String searchTypeName;
    private List<String> inputs;
    private Map<FilterTypes, List<String>> filters;
    private List<SearchResultDTO> results;

    @JsonProperty("page")
    private PagedModel.PageMetadata pageMetadata;
}
