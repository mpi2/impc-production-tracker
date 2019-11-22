package org.gentar.biology.outcome_location;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.location.Location;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class OutcomeLocation extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "outcomeLocationSeq", sequenceName = "OUTCOME_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outcomeLocationSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Outcome.class)
    private Outcome outcome;

    @NotNull
    @ManyToOne(targetEntity = Location.class)
    private Location location;

    @NotNull
    private Integer index;
}
