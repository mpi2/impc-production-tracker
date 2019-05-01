package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.breeding_attempt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.breeding_attempt.breeding_type.BreedingType;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain_type.TrackedStrainType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class BreedingAttempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    @Column(name = "number_of_cre_matings_started")
    private Integer numberOfCreMatingsStarted;

    @Column(name = "number_of_cre_matings_successfull")
    private Integer numberOfCareMatingsSuccessful;

    @ManyToOne
    private TrackedStrainType deleterStrain;

    @ManyToOne
    private BreedingType type;

    private Long imitsMouseAlleleModId;

    @ManyToMany
    @JoinTable(
        name = "breeding_attempt_colony",
        joinColumns = @JoinColumn(name = "breeding_attempt_id"),
        inverseJoinColumns = @JoinColumn(name = "colony_id"))
    private Set<Colony> colonies;
}
