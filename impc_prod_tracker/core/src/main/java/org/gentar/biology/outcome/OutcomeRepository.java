package org.gentar.biology.outcome;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OutcomeRepository extends CrudRepository<Outcome, Long>
{
    @Query("SELECT max(o.tpo) FROM Outcome o")
    String getMaxTpo();
}
