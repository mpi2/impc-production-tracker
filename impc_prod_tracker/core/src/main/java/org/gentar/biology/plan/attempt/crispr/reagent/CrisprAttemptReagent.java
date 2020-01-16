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
package org.gentar.biology.plan.attempt.crispr.reagent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.reagent.Reagent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class CrisprAttemptReagent extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "crisprAttemptReagentSeq", sequenceName = "CRISPR_ATTEMPT_REAGENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crisprAttemptReagentSeq")
    private Long id;

    @EqualsAndHashCode.Exclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private CrisprAttempt crisprAttempt;

    @NotNull
    @ManyToOne(targetEntity = Reagent.class)
    private Reagent reagent;

    private Integer concentration;
}
