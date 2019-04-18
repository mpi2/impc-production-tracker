package uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene;

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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_flag.GeneFlag;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_allele.MouseAllele;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene_synomym.MouseGeneSynonym;
import uk.ac.ebi.impc_prod_tracker.data.biology.ortholog.Ortholog;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MouseGene extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    private String symbol;

    private String mgiId;

    private String type;

    private String genomeBuild;

    private Long entrezGeneId;

    private String ncbiChromosome;

    private Long ncbiStart;

    private Long ncbiStop;

    @Pattern(regexp = "^([\\+-\\?]{1}|)$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String ncbiStrand;

    private String ensemblGeneId;

    private String ensemblChromosome;

    private Long ensemblStart;

    private Long ensemblStop;

    @Pattern(regexp = "^([\\+-\\?]{1}|)$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String ensemblStrand;

    @ManyToMany
    @JoinTable(
            name = "mouse_gene_synonym_relation",
            joinColumns = @JoinColumn(name = "mouse_gene_id"),
            inverseJoinColumns = @JoinColumn(name = "mouse_gene_synonym_id"))
    private Set<MouseGeneSynonym> mouseGeneSynonyms;

    @ManyToMany
    @JoinTable(
            name = "mouse_gene_flag",
            joinColumns = @JoinColumn(name = "mouse_gene_id"),
            inverseJoinColumns = @JoinColumn(name = "gene_flag_id"))
    private Set<GeneFlag> mouseGeneFlags;

    @ManyToMany
    @JoinTable(
        name = "mouse_gene_allele",
        joinColumns = @JoinColumn(name = "mouse_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "allele_id"))
    private Set<MouseAllele> mouseAlleles;

    @OneToMany(mappedBy = "mouseGene")
    private Set<Ortholog> orthologs;
}
