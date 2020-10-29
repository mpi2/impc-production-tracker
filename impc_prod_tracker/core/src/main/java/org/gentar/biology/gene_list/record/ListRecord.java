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
package org.gentar.biology.gene_list.record;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.biology.gene_list.GeneList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ListRecord
{
    @Id
    @SequenceGenerator(name = "listRecordSeq", sequenceName = "LIST_RECORD_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listRecordSeq")
    private Long id;

    @ManyToOne
    @JoinColumn(name="gene_list_id")
    private GeneList geneList;

    private String note;

    @Column(columnDefinition = "boolean default false")
    private Boolean visible;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "listRecord", orphanRemoval=true)
    private Set<GeneByListRecord> genesByRecord;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
        name = "List_record_List_record_type",
        joinColumns = @JoinColumn(name = "list_record_id"),
        inverseJoinColumns = @JoinColumn(name = "list_record_type_id"))
    private Set<ListRecordType> listRecordTypes;
}
