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

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.Attempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.assay.Assay;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.delivery_type.DeliveryMethodType;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.Strain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

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
    private Attempt attempt;

    private Long imitsMiAttemptId;

    private LocalDateTime miDate;

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

    @OneToOne
    private Assay assay;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "crisprAttempt")
    private Set<CrisprAttempt> crisprAttempt;

    @ManyToOne
    private Strain strain;
}
