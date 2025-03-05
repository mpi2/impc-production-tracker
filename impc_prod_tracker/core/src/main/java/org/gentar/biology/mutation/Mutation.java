/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.genbank_file.GenbankFile;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.mutation.mutation_deletion.MolecularMutationDeletion;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.attempt.crispr.targeted_exon.TargetedExon;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    // Public identifier of the mutation.
    private String min;

    private String mgiAlleleId;

    private String symbol;

    @Column(columnDefinition = "boolean default false")
    private Boolean mgiAlleleSymbolRequiresConstruction;

    @Column(columnDefinition = "boolean default false")
    private Boolean mgiAlleleSymbolWithoutImpcAbbreviation;

    @Column(columnDefinition = "boolean default false")
    private Boolean alleleConfirmed;

    private String alleleSymbolSuperscriptTemplate;

    // The next two fields are for Arturo's pipeline
    @Column(columnDefinition = "TEXT")
    @ToString.Exclude
    private String description;

    @Column(columnDefinition = "TEXT")
    @ToString.Exclude
    private String autoDescription;

    @IgnoreForAuditingChanges
    @Column(unique = true, insertable = false, updatable = false)
    private Long imitsAllele;

    @ManyToOne(targetEntity= GeneticMutationType.class)
    private GeneticMutationType geneticMutationType;

    @ManyToOne(targetEntity= MolecularMutationType.class)
    private MolecularMutationType molecularMutationType;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "mutation", cascade= CascadeType.ALL, orphanRemoval=true)
    private Set<MolecularMutationDeletion> molecularMutationDeletion;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "mutation", cascade= CascadeType.ALL, orphanRemoval=true)
    private Set<TargetedExon> targetedExons;


    @ManyToOne(targetEntity = GenbankFile.class)
    private GenbankFile genbankFile;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    private byte[] bamFile;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    private byte[] bamFileIndex;

    @Lob
    @JdbcTypeCode(Types.BINARY)

    private byte[] vcfFile;

    @Lob
    @JdbcTypeCode(Types.BINARY)
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
    @IgnoreForAuditingChanges
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
    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name = "mutation_id")
    private Set<MutationQcResult> mutationQcResults;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "mutation", cascade= CascadeType.ALL, orphanRemoval=true)
    private Set<MutationSequence> mutationSequences;


    @Column(columnDefinition = "TEXT")
    @ToString.Exclude
    private String qcNote;


    private Boolean isManualMutationDeletion;

    private Boolean isMutationDeletionChecked;
    
    // Copy Constructor
    public Mutation(Mutation mutation)
    {
        this.id = mutation.getId();
        this.min = mutation.getMin();
        this.mgiAlleleId = mutation.getMgiAlleleId();
        this.symbol = mutation.getSymbol();
        this.mgiAlleleSymbolRequiresConstruction = mutation.getMgiAlleleSymbolRequiresConstruction();
        this.mgiAlleleSymbolWithoutImpcAbbreviation =
            mutation.getMgiAlleleSymbolWithoutImpcAbbreviation();
        this.alleleConfirmed = mutation.getAlleleConfirmed();
        this.alleleSymbolSuperscriptTemplate = mutation.getAlleleSymbolSuperscriptTemplate();
        this.description = mutation.getDescription();
        this.autoDescription = mutation.getAutoDescription();
        this.imitsAllele = mutation.imitsAllele;
        this.geneticMutationType = mutation.geneticMutationType;
        this.molecularMutationType = mutation.molecularMutationType;
        this.molecularMutationDeletion = mutation.molecularMutationDeletion;
        this.targetedExons = mutation.targetedExons;
        this.genbankFile = mutation.genbankFile;
        this.bamFile = mutation.bamFile;
        this.bamFileIndex =mutation.getBamFileIndex();
        this.vcfFile = mutation.getVcfFile();
        this.vcfFileIndex = mutation.getVcfFileIndex();
        this.genes = mutation.getGenes();
        if (mutation.getOutcomes() != null)
        {
            this.outcomes = new HashSet<>(mutation.getOutcomes());
        }
        if (mutation.getMutationCategorizations() != null)
        {
            this.mutationCategorizations = new HashSet<>(mutation.getMutationCategorizations());
        }
        if (mutation.getMutationQcResults() != null)
        {
            this.mutationQcResults = new HashSet<>(mutation.getMutationQcResults());
        }
        if (mutation.getMutationSequences() != null)
        {
            // Create copy by each element in collection.
            this.mutationSequences = mutation.getMutationSequences().stream()
                .map(MutationSequence::new).collect(Collectors.toSet());
        }
        this.setCreatedBy(mutation.getCreatedBy());
        this.qcNote = mutation.qcNote;
        this.isManualMutationDeletion = mutation.getIsManualMutationDeletion();
        this.isMutationDeletionChecked = mutation.getIsMutationDeletionChecked();
    }
}
