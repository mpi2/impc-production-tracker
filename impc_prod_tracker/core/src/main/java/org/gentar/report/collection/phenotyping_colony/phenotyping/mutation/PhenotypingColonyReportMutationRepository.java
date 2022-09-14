package org.gentar.report.collection.phenotyping_colony.phenotyping.mutation;

import org.gentar.biology.mutation.Mutation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface PhenotypingColonyReportMutationRepository extends CrudRepository<Mutation, Long>
{
    @Query("select " +
            "m.id as mutationId, g.id as geneId, g as gene " +
            "from " +
            "Mutation m LEFT OUTER JOIN m.genes g " +
            "where " +
            "m.id IN :id")
    List<PhenotypingColonyReportMutationGeneProjection> findSelectedMutationGeneProjections(@Param("id") List mutationIds );
}
