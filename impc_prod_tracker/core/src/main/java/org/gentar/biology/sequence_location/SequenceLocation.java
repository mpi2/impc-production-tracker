package org.gentar.biology.sequence_location;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.location.Location;
import org.gentar.biology.sequence.Sequence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class SequenceLocation extends BaseEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "sequenceLocationSeq", sequenceName = "SEQUENCE_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceLocationSeq")
    private Long id;

    @ToString.Exclude
    @ManyToOne(cascade=CascadeType.ALL)
    private Sequence sequence;

    @NotNull
    private Integer index;

    @ToString.Exclude
    @ManyToOne(cascade=CascadeType.ALL)
    private Location location;

    public SequenceLocation(SequenceLocation sequenceLocation)
    {
        this.id = sequenceLocation.getId();
        this.sequence = sequenceLocation.getSequence();
        this.index = sequenceLocation.getIndex();
        this.location = new Location(sequenceLocation.getLocation());
    }
}
