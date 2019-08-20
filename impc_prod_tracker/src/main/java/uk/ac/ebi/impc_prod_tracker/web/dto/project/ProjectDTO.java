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
package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.GeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.LocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.status_dates.StatusDatesDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ProjectDTO extends RepresentationModel
{
    @JsonIgnore
    private Long id;

    @NonNull
    private String tpn;

    @NonNull
    private String assigmentStatusName;

    @JsonProperty("assignmentStatusDates")
    private List<StatusDatesDTO> statusDatesDTOS;

    private Boolean withdrawn;
    private Boolean recovery;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intentionByGeneAttributes")
    private List<GeneDTO> geneDTOS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intentionByLocationAttributes")
    private List<LocationDTO> locationDTOS;

    @JsonIgnore
    private Long imitsMiPlanId;

    private String comment;
    private Boolean isActive;


//    private List<HumanGeneSymbolDTO> humanGenes;
//    @Data
//    public static class HumanGeneSymbolDTO
//    {
//        String symbol;
//        String support;
//        Long support_count;
//    }
}
