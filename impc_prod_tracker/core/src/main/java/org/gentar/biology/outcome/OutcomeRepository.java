package org.gentar.biology.outcome;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OutcomeRepository extends CrudRepository<Outcome, Long>
{
    List<Outcome> findAll();
    Outcome findByTpo(String tpo);

    @Query("SELECT max(o.tpo) FROM Outcome o")
    String getMaxTpo();
}
