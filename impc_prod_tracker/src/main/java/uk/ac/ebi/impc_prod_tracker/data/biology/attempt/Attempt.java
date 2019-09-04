package uk.ac.ebi.impc_prod_tracker.data.biology.attempt;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.attempt_type.AttemptType;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.breeding_attempt.BreedingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.PhenotypingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Attempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Plan plan;

    @ManyToOne
    private AttemptType type;

//    @ToString.Exclude
//    @OneToOne(cascade=CascadeType.ALL, mappedBy = "attempt")
//    private CrisprAttempt crisprAttempt;
//
//    @ToString.Exclude
//    @OneToOne(cascade=CascadeType.ALL, mappedBy = "attempt")
//    private PhenotypingAttempt phenotypingAttempt;
//
//    @ToString.Exclude
//    @OneToOne(cascade=CascadeType.ALL, mappedBy = "attempt")
//    private BreedingAttempt breedingAttempt;
}
