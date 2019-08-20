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
package uk.ac.ebi.impc_prod_tracker.web.dto.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.AttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.status_dates.StatusDatesDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class PlanDTO extends RepresentationModel
{
    @JsonIgnore
    private Long id;
    @NonNull
    private String pin;
    @NonNull
    private String tpn;
    private String funderName;
    private String consortiumName;
    private String workGroupName;
    private String workUnitName;
    private Boolean isActive;
    private String statusName;

    @JsonProperty("statusDates")
    private List<StatusDatesDTO> statusDatesDTOS;

    private String planTypeName;
    private String privacyName;
    private String comments;
    private Boolean productsAvailableForGeneralPublic;
    private String typeName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("attemptTypeName")
    private AttemptDTO attemptDTO;
}
