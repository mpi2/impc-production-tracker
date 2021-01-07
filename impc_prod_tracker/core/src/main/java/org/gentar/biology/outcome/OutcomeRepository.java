package org.gentar.biology.outcome;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutcomeRepository extends CrudRepository<Outcome, Long>
{
    List<Outcome> findAll();
    Outcome findByTpo(String tpo);

    @Query("SELECT max(o.tpo) FROM Outcome o")
    String getMaxTpo();

    //@Query("select o.id as outcomeId, m.id as mutationId, m as mutation from Outcome o LEFT OUTER JOIN o.mutations m where m.alleleConfirmed = TRUE")
    @Query("select o.id as outcomeId, m.id as mutationId, m.symbol as symbol from Outcome o LEFT OUTER JOIN o.mutations m ")
    List<OutcomeMutationProjection> findAllOutcomeMutationProjections();

    //@Query("select o.id as outcomeId, m.id as mutationId, m as mutation from Outcome o LEFT OUTER JOIN o.mutations m where m.alleleConfirmed = TRUE and o.id IN :id")
    @Query("select o.id as outcomeId, m.id as mutationId, m.symbol as symbol from Outcome o LEFT OUTER JOIN o.mutations m where o.id IN :id")
    List<OutcomeMutationProjection> findSelectedOutcomeMutationProjections(@Param("id") List<Long> outcomeIds);
}
