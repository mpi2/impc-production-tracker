package org.gentar.report.collection.mgi_crispr_allele.sequence;

import org.gentar.biology.mutation.sequence.MutationSequence;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiCrisprAlleleReportMutationSequenceRepository extends CrudRepository<MutationSequence, Long> {

    @Query("select " +
            "ms.mutation.id as mutationId, ms.index as index, s.sequence as sequence, t.name as sequenceType, c.name as sequenceCategory " +
            "from " +
            "MutationSequence ms LEFT OUTER JOIN ms.sequence s  LEFT OUTER JOIN s.sequenceType t LEFT OUTER JOIN s.sequenceCategory c " +
            "where " +
            "ms.mutation.id IN :id")
    List<MgiCrisprAlleleReportMutationSequenceProjection> findSelectedMutationSequenceProjections(@Param("id") List mutationIds );
}
