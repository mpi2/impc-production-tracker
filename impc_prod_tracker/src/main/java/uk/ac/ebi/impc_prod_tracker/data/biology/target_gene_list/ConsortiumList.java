/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package uk.ac.ebi.impc_prod_tracker.data.biology.target_gene_list;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.target_gene_list.target_group.TargetGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ConsortiumList extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "consortiumListSeq", sequenceName = "CONSORTIUM_LIST_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consortiumListSeq")
    private Long id;

    @ManyToOne
    private Consortium consortium;

    private String note;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "consortiumList", orphanRemoval=true)
    private Set<TargetGroup> targetGroups;
}
