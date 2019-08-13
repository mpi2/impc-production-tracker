package uk.ac.ebi.impc_prod_tracker.data.experiment.project_location;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.chromosome_feature_type.ChromosomeFeatureType;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ProjectLocation extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "projectLocationSeq", sequenceName = "PROJECT_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectLocationSeq")
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Location location;

    @ManyToOne
    private AlleleType alleleType;

    private int index;

    @ManyToOne(targetEntity= ChromosomeFeatureType.class)
    private ChromosomeFeatureType chromosomeFeatureType;
}
