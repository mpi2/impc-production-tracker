package org.gentar.biology.intention.project_intention_sequence;

import jakarta.persistence.*;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.sequence.Sequence;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectIntentionSequence extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "projectIntentionSequenceSeq", sequenceName = "PROJECT_INTENTION_SEQUENCE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectIntentionSequenceSeq")
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(targetEntity = ProjectIntention.class, cascade=CascadeType.ALL)
    private ProjectIntention projectIntention;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(targetEntity = Sequence.class, cascade=CascadeType.ALL)
    private Sequence sequence;

    @NotNull
    private Integer index;
}
