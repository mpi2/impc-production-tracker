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
package org.gentar.biology.allele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import org.gentar.BaseEntity;
import org.gentar.biology.allele.categorizarion.AlleleCategorization;
import org.gentar.biology.allele.genbank_file.GenbankFile;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.molecular_mutation_type.MolecularMutationType;
import org.gentar.biology.outcome.Outcome;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Allele extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private String mgiAlleleId;

    private String mgiAlleleSymbolSuperscript;

    private Boolean mgiAlleleSymbolWithoutImpcAbbreviation;

    private Boolean alleleConfirmed;

    private String alleleSymbolSuperscriptTemplate;

    // The next two fields are for Arturo's pipeline
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String autoDescription;

    @Column(unique = true)
    private Long imitsAlleleId;


    @ManyToOne(targetEntity= AlleleType.class)
    private AlleleType alleleType;

    @ManyToOne(targetEntity= MolecularMutationType.class)
    private MolecularMutationType molecularMutationType;

    @OneToOne(targetEntity = GenbankFile.class)
    private GenbankFile genebankFile;

    @Lob
    @Column(name="bam_file")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] bam_file;

    @Lob
    @Column(name="bam_file_index")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] bam_file_index;

    @Lob
    @Column(name="vcf_file")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] vcf_file;

    @Lob
    @Column(name="vcf_file_index")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] vcf_file_index;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "allele_gene",
            joinColumns = @JoinColumn(name = "allele_id"),
            inverseJoinColumns = @JoinColumn(name = "gene_id"))
    private Set<Gene> genes;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "allele_outcome",
            joinColumns = @JoinColumn(name = "allele_id"),
            inverseJoinColumns = @JoinColumn(name = "outcome_id"))
    private Set<Outcome> outcomes;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "allele_allele_categorization",
            joinColumns = @JoinColumn(name = "allele_id"),
            inverseJoinColumns = @JoinColumn(name = "allele_categorization_id"))
    private Set<AlleleCategorization> alleleCategorizations;



}
