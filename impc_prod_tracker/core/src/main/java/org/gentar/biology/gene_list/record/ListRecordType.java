package org.gentar.biology.gene_list.record;

import lombok.*;
import org.gentar.biology.gene_list.GeneList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ListRecordType
{
    @Id
    @SequenceGenerator(name = "listRecordTypeSeq", sequenceName = "LIST_RECORD_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listRecordTypeSeq")
    private Long id;

    private String name;

    @ManyToOne
    private GeneList geneList;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "listRecordTypes")
    private Set<ListRecord> listRecords;

    //private Boolean visible;

}
