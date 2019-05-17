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
package uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.crispr_attempt;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.GenotypePrimerDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.MutagenesisDonorDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.NucleaseDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.ReagentDTO;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CrisprAttemptDTO extends RepresentationModel
{
    private LocalDateTime miDate;
    private String miExternalRef;
    private String mutagenesisExternalRef;
    private Boolean individuallySetGrnaConcentrations;
    private Boolean guidesGeneratedInPlasmid;
    private Double gRnaConcentration;
    private Integer noG0WhereMutationDetected;
    private Integer noNhejG0Mutants;
    private Integer noDeletionG0Mutants;
    private Integer noHrG0Mutants;
    private Integer noHdrG0Mutants;
    private Integer noHdrG0MutantsAllDonorsInserted;
    private Integer noHdrG0MutantsSubsetDonorsInserted;
    private Integer totalEmbryosInjected;
    private Integer totalEmbryosSurvived;
    private Integer totalTransferred;
    private Integer noFounderPups;
    private Integer noFounderSelectedForBreeding;
    private Integer founderNumAssays;
    private String assayType;
    private Boolean experimental;
    private String embryoTransferDay;
    private String embryo2Cell;
    private String deliveryMethod;
    private Double voltage;
    private Integer numberOfPulses;
    private List<NucleaseDTO> nucleases;
    private List<ReagentDTO> reagents;
    private List<GenotypePrimerDTO> primers;
    private List<MutagenesisDonorDTO> mutagenesisDonors;
    private Integer embryoSurvived2Cell;
    private String comment;
}
