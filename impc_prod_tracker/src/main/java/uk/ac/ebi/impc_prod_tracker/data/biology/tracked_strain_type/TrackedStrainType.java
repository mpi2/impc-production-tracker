package uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

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
}
