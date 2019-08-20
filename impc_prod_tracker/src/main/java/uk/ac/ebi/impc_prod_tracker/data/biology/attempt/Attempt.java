package uk.ac.ebi.impc_prod_tracker.data.biology.attempt;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.attempt_type.AttemptType;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.breeding_attempt.BreedingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.PhenotypingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class Attempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    @ManyToOne
    private AttemptType attemptType;

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "attempt")
    private CrisprAttempt crisprAttempt;

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "attempt")
    private PhenotypingAttempt phenotypingAttempt;

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "attempt")
    private BreedingAttempt breedingAttempt;
}
