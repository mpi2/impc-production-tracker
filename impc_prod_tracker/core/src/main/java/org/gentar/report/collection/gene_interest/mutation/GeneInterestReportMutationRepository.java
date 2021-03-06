package org.gentar.report.collection.gene_interest.mutation;

import org.gentar.biology.mutation.Mutation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GeneInterestReportMutationRepository extends CrudRepository<Mutation, Long>
{
    @Query("select " +
            "m.id as mutationId, g.id as geneId, g as gene " +
            "from " +
            "Mutation m LEFT OUTER JOIN m.genes g " +
            "where " +
            "m.id IN :id")
    List<GeneInterestReportMutationGeneProjection> findSelectedMutationGeneProjectionsForGeneInterestReport(@Param("id") List mutationIds );
}

