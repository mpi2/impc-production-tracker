package org.gentar.biology.location;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.sequence_location.SequenceLocation;
import org.gentar.biology.species.Species;
import org.gentar.biology.strain.Strain;

import jakarta.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Location extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "locationSeq", sequenceName = "LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locationSeq")
    private Long id;

    private String chr;

    private Long start;

    private Long stop;

    @Pattern(regexp = "^[\\+-\\?]{1}$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String strand;

    private String genomeBuild;

    @ManyToOne(targetEntity = Species.class)
    private Species species;

    @ManyToOne(targetEntity = Strain.class)
    private Strain strain;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @IgnoreForAuditingChanges
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private List<SequenceLocation> sequenceLocations;

    public Location(Location location)
    {
        this.id = location.getId();
        this.chr = location.getChr();
        this.start = location.getStart();
        this.stop = location.getStop();
        this.strand = location.getStrand();
        this.genomeBuild = location.getGenomeBuild();
        this.species = location.getSpecies();
        this.strain = location.getStrain();
        if (location.getSequenceLocations() != null)
        {
            this.sequenceLocations = new ArrayList<>(location.getSequenceLocations());
        }
    }
}
