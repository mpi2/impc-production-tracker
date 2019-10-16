package uk.ac.ebi.impc_prod_tracker.data.biology.gene;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele.Allele;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.flag.GeneFlag;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.species.Species;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Gene extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private String symbol;

    private String name;

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

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "genes")
    private Set<Allele> alleles;

    @ManyToOne(targetEntity= Species.class)
    private Species species;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany()
    @JoinTable(
            name = "gene_flag_relation",
            joinColumns = @JoinColumn(name = "gene_id"),
            inverseJoinColumns = @JoinColumn(name = "gene_flag_id"))
    private Set<GeneFlag> geneFlags;

    private String accId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "gene_id")
    private Set<ProjectIntentionGene> projectIntentionGenes;
}
