package uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain_type.TrackedStrainType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class TrackedStrain extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "trackedStrainSeq", sequenceName = "TRACKED_STRAIN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trackedStrainSeq")
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String name;

    private String mgiId;

    private String mgiName;

    @ManyToOne
    private TrackedStrainType type;
}
