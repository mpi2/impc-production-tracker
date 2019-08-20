package uk.ac.ebi.impc_prod_tracker.data.biology.allele_location;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele.Allele;
import uk.ac.ebi.impc_prod_tracker.data.biology.chromosome_feature_type.ChromosomeFeatureType;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class AlleleLocation extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "alleleLocationSeq", sequenceName = "ALLELE_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alleleLocationSeq")
    private Long id;

    @ManyToOne(targetEntity = Allele.class)
    private Allele allele;

    private int index;

    @ManyToOne(targetEntity = Location.class)
    private Location location;

    @ManyToOne(targetEntity= ChromosomeFeatureType.class)
    private ChromosomeFeatureType chromosomeFeatureType;
}
