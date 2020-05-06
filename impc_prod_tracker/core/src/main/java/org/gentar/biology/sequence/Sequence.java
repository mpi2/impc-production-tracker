package org.gentar.biology.sequence;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.intention.project_intention_sequence.ProjectIntentionSequence;
import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.type.SequenceType;
import org.gentar.biology.sequence_location.SequenceLocation;
import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Sequence extends BaseEntity
{
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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "sequence_id")
    private List<SequenceLocation> sequenceLocations;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "sequence")
    private List<ProjectIntentionSequence> projectIntentionSequences;
}
