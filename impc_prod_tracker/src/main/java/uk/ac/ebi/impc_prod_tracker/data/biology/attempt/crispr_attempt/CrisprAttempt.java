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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt_reagent.CrisprAttemptReagent;
import uk.ac.ebi.impc_prod_tracker.data.biology.guide.Guide;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assay_type.AssayType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.delivery_type.DeliveryType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class CrisprAttempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Plan plan;

    private LocalDateTime miDate;

    private Long imitsMiAttemptId;

    private String miExternalRef;

    private String mutagenesisExternalRef;

    private Boolean individuallySetGrnaConcentrations;

    private Boolean guidesGeneratedInPlasmid;

    private Double grnaConcentration;

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

    @Column(columnDefinition = "TEXT")
    private String comment;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "crisprAttempt")
    private Set<CrisprAttemptReagent> crisprAttemptReagents;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "crisprAttempt")
    private Set<Guide> guides;
}
