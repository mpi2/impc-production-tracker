package org.gentar.report.collection.gene_interest.mutation;

import org.gentar.biology.mutation.Mutation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface GeneInterestReportMutationRepository extends CrudRepository<Mutation, Long>
{
    @Query("select " +
            "m.id as mutationId, g.id as geneId, g as gene " +
            "from " +
            "Mutation m LEFT OUTER JOIN m.genes g " +
            "where " +
            "m.id IN :id")
    List<GeneInterestReportMutationGeneProjection> findSelectedMutationGeneProjectionsForGeneInterestReport(@Param("id") List mutationIds );

    @Query("select " +
            "m.id as mutationId, m.min as mutationIdentificationNumber, mc.name as mutationCategorizationName " +
            "from " +
            "Mutation m LEFT OUTER JOIN m.mutationCategorizations mc LEFT OUTER JOIN mc.mutationCategorizationType mct " +
            "where " +
            "mct.name='esc_allele_class' and " +
            "m.min IN :min")
    List<GeneInterestReportEsCellMutationTypeProjection> findSelectedEsCellMutationTypeProjectionsForGeneInterestReport(@Param("min") List mutationMins );
}

