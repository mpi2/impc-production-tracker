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
package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.assay.Assay;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.crispr_attempt_reagent.CrisprAttemptReagent;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.delivery_type.DeliveryMethodType;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.genotype_primer.GenotypePrimer;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.guide.Guide;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_donor.MutagenesisDonor;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_strategy.MutagenesisStrategy;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.Strain;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
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
    private Plan plan;

    private Long imitsMiAttemptId;

    private LocalDate miDate;

    private String miExternalRef;

    private Boolean experimental;

    private String mutagenesisExternalRef;

    private Double voltage;

    private Integer noOfPulses;

    private Integer totalEmbryosInjected;

    private Integer totalEmbryosSurvived;

    private String embryoTransferDay;

    @Column(name = "embryo_2_Cell")
    private String embryo2Cell;

    private Integer totalTransferred;

    private Integer numFounderPups;

    private Integer numFounderSelectedForBreeding;

    @ManyToOne
    private DeliveryMethodType deliveryMethodType;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "crisprAttempt")
    private Assay assay;

    @ManyToOne(targetEntity= MutagenesisStrategy.class)
    private MutagenesisStrategy strategy;

    @ManyToOne
    private Strain strain;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "crisprAttempt", orphanRemoval=true)
    private Set<Guide> guides;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "crisprAttempt", orphanRemoval=true)
    private Set<GenotypePrimer> primers;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "crisprAttempt", orphanRemoval=true)
    private Set<CrisprAttemptReagent> crisprAttemptReagents;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "crisprAttempt", orphanRemoval=true)
    private Set<Nuclease> nucleases;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "crisprAttempt", orphanRemoval=true)
    private Set<MutagenesisDonor> mutagenesisDonors;
}
