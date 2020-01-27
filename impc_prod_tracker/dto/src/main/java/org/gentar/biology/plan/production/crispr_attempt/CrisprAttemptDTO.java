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
package org.gentar.biology.plan.production.crispr_attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.strain.StrainDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CrisprAttemptDTO
{
    @JsonIgnore
    private Long crisprAttemptId;

    private Long imitsMiAttemptId;

    private LocalDate miDate;

    @JsonProperty("attemptExternalRef")
    private String miExternalRef;

    private Boolean experimental;

    private String comment;
    private String mutagenesisExternalRef;
    private String deliveryTypeMethodName;
    private Double voltage;

    @JsonProperty("numberOfPulses")
    private Integer noOfPulses;

    @JsonProperty("nucleases")
    private List<NucleaseDTO> nucleaseDTOS;

    @JsonProperty("guidesAttributes")
    private List<GuideDTO> guideDTOS;

    @JsonProperty("mutagenesisDonorsAttributes")
    private List<MutagenesisDonorDTO> mutagenesisDonorDTOS;

    @JsonProperty("mutagenesisStrategy")
    private MutagenesisStrategyDTO mutagenesisStrategyDTO;

    @JsonProperty("reagentsAttributes")
    private List<ReagentDTO> reagentDTOS;

    @JsonProperty("genotypePrimersAttributes")
    private List<GenotypePrimerDTO> genotypePrimerDTOS;

    @JsonProperty("totalEmbryosInjected")
    private Integer totalEmbryosInjected;

    @JsonProperty("totalEmbryosSurvived")
    private Integer totalEmbryosSurvived;

    @JsonProperty("embryoTransferDay")
    private String embryoTransferDay;

    @JsonProperty("embryo2Cell")
    private String embryo2Cell;

    @JsonProperty("totalTransferred")
    private Integer totalTransferred;

    private Integer numFounderPups;

    @JsonProperty("assayAttributes")
    private AssayDTO assay;

    private Integer numFounderSelectedForBreeding;

    @JsonProperty("strainInjectedAttributes")
    private StrainDTO strain;
}
