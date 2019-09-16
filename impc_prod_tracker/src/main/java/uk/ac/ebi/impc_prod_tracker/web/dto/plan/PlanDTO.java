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
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.phenotyping.PhenotypingAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.breeding_attempt.BreedingAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt.CrisprAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.status_stamps.StatusStampsDTO;
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
    @JsonProperty("tpn")
    private String tpn;

    @JsonProperty("funder_name")
    private String funderName;

    @JsonProperty("work_unit_name")
    private String workUnitName;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("status_name")
    private String statusName;

    @JsonProperty("status_dates")
    private List<StatusStampsDTO> statusStampsDTOS;

    private String comment;

    @JsonProperty("products_available_for_general_public")
    private Boolean productsAvailableForGeneralPublic;

    @JsonProperty("type_name")
    private String planTypeName;

    @JsonProperty("attempt_type_name")
    private String attemptTypeName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("crispr_attempt_attributes")
    private CrisprAttemptDTO crisprAttemptDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("breeding_attempt_attributes")
    private BreedingAttemptDTO breedingAttemptDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotyping_attempt_attributes")
    private PhenotypingAttemptDTO phenotypingAttemptDTO;
}
