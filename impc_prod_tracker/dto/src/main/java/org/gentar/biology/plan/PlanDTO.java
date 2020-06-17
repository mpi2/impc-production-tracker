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
package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptCreationDTO;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptResponseDTO;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.springframework.hateoas.RepresentationModel;
import org.gentar.biology.plan.attempt.breeding.BreedingAttemptDTO;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptDTO;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import java.util.List;

@Data
@NoArgsConstructor
public class PlanDTO extends RepresentationModel
{
    private Long id;

    private String pin;

    private String tpn;

    private List<String> funderNames;
    private String workUnitName;
    private String workGroupName;
    private String statusName;
    private String summaryStatusName;

    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    private String comment;

    private Boolean productsAvailableForGeneralPublic;

    @JsonProperty("typeName")
    private String planTypeName;

    private String attemptTypeName;

    // Starting point outcome for phenotyping plans
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypingStartingPoint")
    private PlanStartingPointDTO planStartingPointDTO;

    // Starting point outcomes for breeding plans
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("BreedingStartingPoints")
    private List<PlanStartingPointDTO> planStartingPointDTOS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("crisprAttempt")
    private CrisprAttemptDTO crisprAttemptDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("breedingAttempt")
    private BreedingAttemptDTO breedingAttemptDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypingAttempt")
    private PhenotypingAttemptCreationDTO phenotypingAttemptCreationDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypingAttemptResponse")
    private PhenotypingAttemptResponseDTO phenotypingAttemptResponseDTO;

    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;
}
