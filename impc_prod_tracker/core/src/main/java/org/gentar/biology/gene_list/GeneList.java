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
package org.gentar.biology.gene_list;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.organization.consortium.Consortium;

import jakarta.persistence.*;
import java.util.List;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class GeneList extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name="consortium_id")
    private Consortium consortium;

    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "geneList", orphanRemoval=true)
    private List<ListRecord> listRecords;
}
