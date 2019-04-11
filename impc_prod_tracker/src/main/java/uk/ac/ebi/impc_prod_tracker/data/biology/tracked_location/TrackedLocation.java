package uk.ac.ebi.impc_prod_tracker.data.biology.tracked_location;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.species.Species;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.Strain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class TrackedLocation extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "trackedLocationSeq", sequenceName = "TRACKED_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trackedLocationSeq")
    private Long id;

    @NotNull
    private String chromosome;

    @NotNull
    private Long start;

    @NotNull
    private Long stop;

    @Pattern(regexp = "^[\\+-\\?]{1}$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String strand;

    private String genomeBuild;

    @ManyToOne(targetEntity = Strain.class)
    private Strain strain;

    @ManyToOne(targetEntity = Species.class)
    private Species species;

    //@OneToMany(mappedBy = "location")
    //private Set<ProjectLocation> projectLocations;

    private String sequence;
}
