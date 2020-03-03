package org.gentar.biology.intention.project_intention_sequence;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.sequence.Sequence;
import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectIntentionSequence extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "project_intention_id")
    @MapsId
    private ProjectIntention projectIntention;

    @ToString.Exclude
    @ManyToOne(cascade=CascadeType.ALL)
    private Sequence sequence;

    private Integer index;
}
