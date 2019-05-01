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
package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assay_type.AssayType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.delivery_type.DeliveryType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class CrisprAttempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    private LocalDateTime miDate;

    private Long imitsMiAttemptId;

    private String miExternalRef;

    private String mutagenesisExternalRef;

    private Boolean individuallySetGrnaConcentrations;

    private Boolean guidesGeneratedInPlasmid;

    private Float grnaConcentration;

    @Column(name = "no_g0_where_mutation_detected")
    private Integer noG0WhereMutationDetected;

    @Column(name = "no_nhej_g0_mutants")
    private Integer noNhejG0Mutants;

    @Column(name = "no_deletion_g0_mutants")
    private Integer noDeletionG0Mutants;

    @Column(name = "no_hr_g0_mutants")
    private Integer noHrG0Mutants;

    @Column(name = "no_hdr_g0_mutants")
    private Integer noHdrG0Mutants;

    @Column(name = "no_hdr_g0_mutants_all_donors_inserted")
    private Integer noHdrG0MutantsAllDonorsInserted;

    @Column(name = "no_hdr_g0_mutants_subset_donors_inserted")
    private Integer noHdrG0MutantsSubsetDonorsInserted;

    private Integer totalEmbryosInjected;

    private Integer totalEmbryosSurvived;

    private Integer totalTransferred;

    private Integer noFounderPups;

    private Integer noFounderSelectedForBreeding;

    private Integer founderNumAssays;

    @ManyToOne
    private AssayType assayType;

    private Boolean experimental;

    @ManyToOne
    private DeliveryType deliveryType;

    private Double voltage;

    private Integer noOfPulses;

    private String embryoTransferDay;

    @Column(name = "embryo_2_Cell")
    private String embryo2Cell;

    private String comment;
}
