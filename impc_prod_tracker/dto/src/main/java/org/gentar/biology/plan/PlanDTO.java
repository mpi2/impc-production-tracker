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
import org.gentar.biology.outcome.OutcomeDTO;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptDTO;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("crisprAttempt")
    private CrisprAttemptDTO crisprAttemptDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("breedingAttempt")
    private BreedingAttemptDTO breedingAttemptDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypingAttemptDetails")
    private PhenotypingAttemptDTO phenotypingAttemptDTO;

    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;

    // Starting point outcome for phenotyping plans
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypingStartingPointDetails")
    private PlanStartingPointDTO phenotypingStartingPoint;

    // Starting point outcomes for breeding plans
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("breedingStartingPointDetails")
    private List<PlanStartingPointDTO> breedingOutcomeDTOS;
}
