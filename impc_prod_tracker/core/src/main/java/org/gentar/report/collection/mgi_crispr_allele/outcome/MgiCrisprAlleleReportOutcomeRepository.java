package org.gentar.report.collection.mgi_crispr_allele.outcome;

import org.gentar.biology.outcome.Outcome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiCrisprAlleleReportOutcomeRepository extends CrudRepository<Outcome, Long> {

    @Query("select " +
            "o.id as outcomeId, " +
            "m.id as mutationId, " +
            "m.min as mutationMin, " +
            "m.symbol as symbol, " +
            "m.description as description, " +
            "m.mgiAlleleId as mgiAlleleAccId, " +
            "m.alleleConfirmed as alleleConfirmed, " +
            "mmt.name as mutationType, " +
            "mmt.type as mutationCategory " +
            "from " +
            "Outcome o LEFT OUTER JOIN o.mutations m " +
            "INNER JOIN MolecularMutationType mmt on m.molecularMutationType = mmt " +
            "where " +
            "o.id IN :ids")
    List<MgiCrisprAlleleReportOutcomeMutationProjection> findSelectedOutcomeMutationCrisprReportProjections( @Param("ids") List<Long> outcomeIds);
}
