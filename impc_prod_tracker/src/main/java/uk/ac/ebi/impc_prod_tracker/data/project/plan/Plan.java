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
package uk.ac.ebi.impc_prod_tracker.data.project.plan;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.flag.PlanFlag;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.privacy.PlanPrivacy;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.protocol.Protocol;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.status.PlanStatus;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.type.PlanType;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Plan extends BaseEntity {

    @Id
    @SequenceGenerator(name = "planSeq", sequenceName = "PLAN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planSeq")
    private Long id;

    @NotNull
    private Boolean isActive;

    private Long parentPlanId;

    @NotNull
    @ManyToOne(targetEntity= PlanType.class)
    private PlanType planType;

    @NotNull
    @ManyToOne(targetEntity= PlanPrivacy.class)
    private PlanPrivacy planPrivacy;

    @ManyToMany
    @JoinTable(
        name = "plan_flag_relation",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "plan_flag_id"))
    private Set<PlanFlag> planFlags;

    @ManyToMany
    @JoinTable(
        name = "plan_protocol",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "protocol_id"))
    private Set<Protocol> protocols;

    @NotNull
    @ManyToOne(targetEntity= PlanStatus.class)
    private PlanStatus status;

}
