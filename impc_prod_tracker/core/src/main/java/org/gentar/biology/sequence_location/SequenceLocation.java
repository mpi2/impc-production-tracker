package org.gentar.biology.sequence_location;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @ManyToOne(targetEntity = Sequence.class)
    private Sequence sequence;

    private Integer index;

    @ManyToOne(targetEntity = Location.class)
    private Location location;

}
