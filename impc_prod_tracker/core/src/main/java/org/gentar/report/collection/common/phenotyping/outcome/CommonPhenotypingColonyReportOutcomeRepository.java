package org.gentar.report.collection.common.phenotyping.outcome;

import org.gentar.biology.outcome.Outcome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface CommonPhenotypingColonyReportOutcomeRepository extends CrudRepository<Outcome, Long> {

    // NOTE: m.alleleConfirmed = TRUE is not used for this MGI report
    @Query("select o.id as outcomeId, m.id as mutationId, m.symbol as symbol from Outcome o LEFT OUTER JOIN o.mutations m ")
    List<CommonPhenotypingColonyReportOutcomeMutationProjection> findAllOutcomeMutationProjections();

    @Query("select o.id as outcomeId, m.id as mutationId, m.symbol as symbol from Outcome o LEFT OUTER JOIN o.mutations m where o.id IN :ids")
    List<CommonPhenotypingColonyReportOutcomeMutationProjection> findSelectedOutcomeMutationProjections(@Param("ids") List<Long> outcomeIds);
}
