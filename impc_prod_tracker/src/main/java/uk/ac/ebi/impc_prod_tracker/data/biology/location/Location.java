package uk.ac.ebi.impc_prod_tracker.data.biology.location;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.specie.Specie;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Location extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "trackedLocationSeq", sequenceName = "TRACKED_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trackedLocationSeq")
    private Long id;

    private String chr;

    private Long start;

    private Long stop;

    @Pattern(regexp = "^[\\+-\\?]{1}$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String strand;

    private String genomeBuild;

    @ManyToOne(targetEntity = Specie.class)
    private Specie specie;
}
