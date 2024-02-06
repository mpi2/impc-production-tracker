/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.plan.attempt.crispr;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
import org.gentar.biology.plan.attempt.crispr.reagent.CrisprAttemptReagent;
import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.MutagenesisDonor;
import org.gentar.biology.plan.attempt.crispr.nuclease.Nuclease;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.strain.Strain;

import jakarta.persistence.*;
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

    @IgnoreForAuditingChanges
    @Column(unique = true, insertable = false, updatable = false)
    private Long imitsMiAttempt;

    private LocalDate miDate;

    private String miExternalRef;

    @Column(columnDefinition = "boolean default false")
    private Boolean experimental;

    private String mutagenesisExternalRef;

    private Integer totalEmbryosInjected;

    private Integer totalEmbryosSurvived;

    @Column(name = "embryo_2_Cell")
    private String embryo2Cell;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private String embryoTransferDay;

    private Integer totalTransferred;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "crisprAttempt", orphanRemoval=true)
    private Assay assay;

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
