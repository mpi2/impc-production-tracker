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
package uk.ac.ebi.impc_prod_tracker.data.biology.gene.flag;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
// DO NOT CHANGE THE LINES BELOW TO @Data
// - This caused the HHH000100: Fail-safe cleanup (collections) Hibernate error
// - when entering data into gene_flag
@Data
@Entity
public class GeneFlag extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "geneFlagSeq", sequenceName = "GENE_FLAG_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geneFlagSeq")
    private Long id;

    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "geneFlags")
    private Set<Gene> genes;
}
