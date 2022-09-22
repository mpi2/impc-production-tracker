package org.gentar.report.collection.mgi_modification_allele.outcome;

import org.gentar.biology.outcome.Outcome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiModificationAlleleReportOutcomeRepository extends CrudRepository<Outcome, Long> {

    @Query("select o.id as outcomeId, m.id as mutationId, m.min as mutationIdentificationNumber, m.mgiAlleleId as mgiAlleleAccId, m.symbol as symbol from Outcome o LEFT OUTER JOIN o.mutations m where o.id IN :ids")
    List<MgiModificationAlleleReportOutcomeMutationProjection> findSelectedOutcomeMutationProjections(@Param("ids") List<Long> outcomeIds);
}
