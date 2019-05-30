package uk.ac.ebi.impc_prod_tracker.data.biology.outcome;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;

public interface OutcomeRepository extends CrudRepository<Outcome, Long>
{
    Iterable<Outcome> findAllByColony(Colony colony);
}
