package org.gentar.biology.sequence_location;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.location.Location;
import org.gentar.biology.sequence.Sequence;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class SequenceLocation extends BaseEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "sequenceLocationSeq", sequenceName = "SEQUENCE_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceLocationSeq")
    private Long id;

    @ToString.Exclude
    @ManyToOne(targetEntity = Sequence.class, cascade=CascadeType.ALL)
    private Sequence sequence;

    private Integer index;

    @ToString.Exclude
    @ManyToOne(targetEntity = Location.class, cascade=CascadeType.ALL)
    private Location location;

}
