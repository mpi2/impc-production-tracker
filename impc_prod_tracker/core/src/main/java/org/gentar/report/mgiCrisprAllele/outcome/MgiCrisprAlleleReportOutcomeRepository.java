package org.gentar.report.mgiCrisprAllele.outcome;

import org.gentar.biology.outcome.Outcome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MgiCrisprAlleleReportOutcomeRepository extends CrudRepository<Outcome, Long> {

    @Query("select " +
            "o.id as outcomeId, " +
            "m.id as mutationId, " +
            "m.symbol as symbol, " +
            "m.mgiAlleleId as mgiAlleleAccId, " +
            "m.alleleConfirmed as alleleConfirmed, " +
            "mmt.name as mutationType " +
            "from " +
            "Outcome o LEFT OUTER JOIN o.mutations m " +
            "inner join MolecularMutationType mmt on m.molecularMutationType = mmt " +
            "where " +
            "o.id IN :ids")
    List<MgiCrisprAlleleReportOutcomeMutationProjection> findSelectedOutcomeMutationCrisprReportProjections( @Param("ids") List<Long> outcomeIds);
}
