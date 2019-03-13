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
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_flag.GeneFlag;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_allele.HumanAllele;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease.HumanDisease;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene_synonym.HumanGeneSynonym;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene.MouseGene;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HumanGene extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "humanGeneSeq", sequenceName = "HUMAN_GENE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humanGeneSeq")
    private Long id;

    @NotNull
    private String symbol;

    @NotNull
    private String name;

    private String omimGeneId;

    @NotNull
    private String chromosome;

    @NotNull
    private Long start;

    @NotNull
    private Long stop;

    @Pattern(regexp = "^[\\+-\\?]{1}$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String strand;

    private String genomeBuild;

    @ManyToMany
    @JoinTable(
        name = "human_gene_flag",
        joinColumns = @JoinColumn(name = "human_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "gene_flag_id"))
    private Set<GeneFlag> humanGeneFlags;

    @ManyToMany
    @JoinTable(
        name = "human_gene_synonym_rel",
        joinColumns = @JoinColumn(name = "human_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "human_gene_synonym_id"))
    private Set<HumanGeneSynonym> humanGeneSynonyms;

    @ManyToMany
    @JoinTable(
        name = "human_gene_allele",
        joinColumns = @JoinColumn(name = "human_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "human_allele_id"))
    private Set<HumanAllele> humanAlleles;

    @ManyToMany
    @JoinTable(
        name = "human_gene_disease",
        joinColumns = @JoinColumn(name = "human_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "human_disease_id"))
    private Set<HumanDisease> humanDiseases;

    @ManyToMany
    @JoinTable(
        name = "ortholog",
        joinColumns = @JoinColumn(name = "human_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "mouse_gene_id"))
    private Set<MouseGene> mouseOrthologs;

}
