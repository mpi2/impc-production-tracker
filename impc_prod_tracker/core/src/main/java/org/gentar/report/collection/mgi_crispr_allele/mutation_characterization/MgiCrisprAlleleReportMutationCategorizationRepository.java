package org.gentar.report.collection.mgi_crispr_allele.mutation_characterization;

import org.gentar.biology.mutation.Mutation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiCrisprAlleleReportMutationCategorizationRepository extends CrudRepository<Mutation, Long> {

    @Query("select " +
            "m.id as mutationId, mc.name as mutationCategorization, mct.name as mutationCategorizationType " +
            "from " +
            "Mutation m " +
            "LEFT OUTER JOIN m.mutationCategorizations mc " +
            "LEFT OUTER JOIN mc.mutationCategorizationType mct ON mc.mutationCategorizationType = mct " +
            "where " +
            "m.id IN :id")
    List<MgiCrisprAlleleReportMutationCategorizationProjection> findSelectedMutationCharacterizationProjections(@Param("id") List mutationIds );

}
