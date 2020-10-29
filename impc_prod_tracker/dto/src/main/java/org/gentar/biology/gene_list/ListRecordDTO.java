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
package org.gentar.biology.gene_list;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;
import java.util.List;

@Relation(collectionRelation = "records")
@Data
public class ListRecordDTO
{
    private Long id;
    private String note;
    private Boolean visible;
    private List<GeneByListRecordDTO> genes;
    private List<String> recordTypes;

    // Internal field to help the correct mapping of the recordTypes
    @JsonIgnore
    private String consortiumName;

    @JsonProperty("projects")
    private List<ProjectByGeneSummaryDTO> projectByGeneSummaryDTOS;
}
