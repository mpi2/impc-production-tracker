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
package org.gentar.biology.colony;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.allele.Allele;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.strain.Strain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Colony extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "colonySeq", sequenceName = "COLONY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colonySeq")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private Strain strain;

    @EqualsAndHashCode.Exclude
    @ManyToOne(targetEntity = Outcome.class)
    private Outcome outcome;

    private Boolean genotypeConfirmed;

    private String genotypingComment;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "colonies")
    private Set<Allele> alleles;
}
