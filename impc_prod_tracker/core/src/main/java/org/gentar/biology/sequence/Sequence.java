package org.gentar.biology.sequence;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.intention.project_intention_sequence.ProjectIntentionSequence;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.type.SequenceType;
import org.gentar.biology.sequence_location.SequenceLocation;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @ManyToOne
    private SequenceType sequenceType;

    @ManyToOne
    private SequenceCategory sequenceCategory;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
    @JoinColumn(name = "sequence_id")
    private List<SequenceLocation> sequenceLocations;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "sequence")
    private List<ProjectIntentionSequence> projectIntentionSequences;

    // Copy Constructor
    public Sequence(Sequence sequence)
    {
        this.id = sequence.getId();
        this.sequence = sequence.getSequence();
        this.sequenceType = sequence.getSequenceType();
        this.sequenceCategory = sequence.getSequenceCategory();
        if (sequence.getSequenceLocations() != null)
        {
            // Create copy by each element in collection.
            this.sequenceLocations = sequence.getSequenceLocations().stream()
                .map(SequenceLocation::new).collect(Collectors.toList());
        }
        if (sequence.getProjectIntentionSequences() != null)
        {
            this.projectIntentionSequences = new ArrayList<>(sequence.getProjectIntentionSequences());
        }
    }
}
