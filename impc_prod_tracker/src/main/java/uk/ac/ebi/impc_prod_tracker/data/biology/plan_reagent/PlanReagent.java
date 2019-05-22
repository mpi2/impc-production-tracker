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
package uk.ac.ebi.impc_prod_tracker.data.biology.plan_reagent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.reagent.Reagent;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class PlanReagent extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "planReagentSeq", sequenceName = "PLAN_REAGENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planReagentSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Plan.class)
    private Plan plan;

    @NotNull
    @ManyToOne(targetEntity = Reagent.class)
    private Reagent reagent;

    @NotNull
    private Integer concentration;
}
