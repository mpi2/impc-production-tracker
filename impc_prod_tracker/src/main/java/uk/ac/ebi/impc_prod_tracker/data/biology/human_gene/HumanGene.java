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
package uk.ac.ebi.impc_prod_tracker.data.biology.human_gene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease.HumanDisease;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene_synonym.HumanGeneSynonym;
import uk.ac.ebi.impc_prod_tracker.data.biology.ortholog.Ortholog;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HumanGene extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    @NotNull
    private String symbol;

    @NotNull
    private String name;

    @NotNull
    private String hgncId;

    @ManyToMany
    @JoinTable(
        name = "human_gene_synonym_rel",
        joinColumns = @JoinColumn(name = "human_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "human_gene_synonym_id"))
    private Set<HumanGeneSynonym> humanGeneSynonyms;

    @ManyToMany
    @JoinTable(
        name = "human_gene_disease",
        joinColumns = @JoinColumn(name = "human_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "human_disease_id"))
    private Set<HumanDisease> humanDiseases;

    @OneToMany(mappedBy = "humanGene")
    private Set<Ortholog> orthologs;

}
