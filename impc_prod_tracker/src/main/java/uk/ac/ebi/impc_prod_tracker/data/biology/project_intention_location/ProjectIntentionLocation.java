package uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_location;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.chromosome_feature_type.ChromosomeFeatureType;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectIntentionLocation extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "project_intention_id")
    @MapsId
    private ProjectIntention projectIntention;

    @ManyToOne
    private Location location;

    private int index;

    @ManyToOne(targetEntity= ChromosomeFeatureType.class)
    private ChromosomeFeatureType chromosomeFeatureType;
}

