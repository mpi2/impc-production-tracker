package uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_sequence;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.chromosome_feature_type.ChromosomeFeatureType;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence.Sequence;
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

    @ManyToOne
    private Sequence sequence;

    private int index;

    @ManyToOne(targetEntity= ChromosomeFeatureType.class)
    private ChromosomeFeatureType chromosomeFeatureType;
}
