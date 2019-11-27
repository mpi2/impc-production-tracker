package org.gentar.biology.location;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.species.Species;
import org.gentar.biology.strain.Strain;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
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
}
