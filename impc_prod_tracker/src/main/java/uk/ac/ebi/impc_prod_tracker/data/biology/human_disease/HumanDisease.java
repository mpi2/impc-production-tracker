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
package uk.ac.ebi.impc_prod_tracker.data.biology.human_disease;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease_synonym.HumanDiseaseSynonym;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGene;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HumanDisease extends BaseEntity {
    @Id
    @SequenceGenerator(name = "humanDiseaseSeq", sequenceName = "HUMAN_DISEASE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humanDiseaseSeq")
    private Long id;

    private String name;

    private String omimDiseaseId;

    private String notes;

    @ManyToMany(mappedBy = "humanDiseases")
    private Set<HumanGene> humanGenes;


    @ManyToMany
    @JoinTable(
        name = "human_disease_synonym_rel",
        joinColumns = @JoinColumn(name = "human_disease_id"),
        inverseJoinColumns = @JoinColumn(name = "human_disease_synonym_id"))
    private Set<HumanDiseaseSynonym> humanDiseaseSynonyms;


}
