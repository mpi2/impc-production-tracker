package uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain.TrackedStrain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class TrackedStrainType
{
    @Id
    @SequenceGenerator(name = "trackedStrainTypeSeq", sequenceName = "TRACKED_STRAIN_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trackedStrainTypeSeq")
    private Long id;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "types")
    private Set<TrackedStrain> trackedStrains;
}
