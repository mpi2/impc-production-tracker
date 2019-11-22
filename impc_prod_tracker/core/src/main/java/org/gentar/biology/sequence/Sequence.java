package org.gentar.biology.sequence;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.type.SequenceType;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Sequence extends BaseEntity {

    @Id
    @SequenceGenerator(name = "sequenceSeq", sequenceName = "SEQUENCE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceSeq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String sequence;

    @ManyToOne(targetEntity= SequenceType.class)
    private SequenceType sequenceType;

    @ManyToOne(targetEntity= SequenceCategory.class)
    private SequenceCategory sequenceCategory;

}
