package uk.ac.ebi.impc_prod_tracker.data.biology.attempt_parent_outcome;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.Attempt;

public interface AttemptParentOutcomeRepository extends CrudRepository<AttemptParentOutcome, Long> {
    AttemptParentOutcome findByAttempt(Attempt attempt);
}
