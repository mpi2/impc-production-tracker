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
package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.hibernate.annotations.Type;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.genbank_file.GenbankFile;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.outcome.Outcome;
import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Mutation extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "mutationSeq", sequenceName = "MUTATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutationSeq")
    @Column(name = "id", updatable=false)
    private Long id;

    private String mgiAlleleId;

    private String mgiAlleleSymbol;

    private Boolean mgiAlleleSymbolRequiresConstruction;

    private Boolean mgiAlleleSymbolWithoutImpcAbbreviation;

    private Boolean alleleConfirmed;

    private String alleleSymbolSuperscriptTemplate;

    // The next two fields are for Arturo's pipeline
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String autoDescription;

    @Column(unique = true)
    private Long imitsAllele;

    @ManyToOne(targetEntity= GeneticMutationType.class)
    private GeneticMutationType geneticMutationType;

    @ManyToOne(targetEntity= MolecularMutationType.class)
    private MolecularMutationType molecularMutationType;

    @OneToOne(targetEntity = GenbankFile.class)
    private GenbankFile genbankFile;

    @Lob
    @Column(name="bam_file")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] bamFile;

    @Lob
    @Column(name="bam_file_index")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] bamFileIndex;

    @Lob
    @Column(name="vcf_file")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] vcfFile;

    @Lob
    @Column(name="vcf_file_index")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] vcfFileIndex;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "mutation_gene",
            joinColumns = @JoinColumn(name = "mutation_id"),
            inverseJoinColumns = @JoinColumn(name = "gene_id"))
    private Set<Gene> genes;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "mutation_outcome",
            joinColumns = @JoinColumn(name = "mutation_id"),
            inverseJoinColumns = @JoinColumn(name = "outcome_id"))
    private Set<Outcome> outcomes;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "mutation_mutation_categorization",
            joinColumns = @JoinColumn(name = "mutation_id"),
            inverseJoinColumns = @JoinColumn(name = "mutation_categorization_id"))
    private Set<MutationCategorization> mutationCategorizations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany
    @JoinColumn(name = "mutation_id")
    private Set<MutationQcResult> mutationQcResults;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "mutation")
    private Set<MutationSequence> mutationSequences;
}
