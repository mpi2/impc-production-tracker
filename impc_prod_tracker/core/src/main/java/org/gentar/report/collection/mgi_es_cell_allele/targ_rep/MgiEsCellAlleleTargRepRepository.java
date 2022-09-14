package org.gentar.report.collection.mgi_es_cell_allele.targ_rep;

import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiEsCellAlleleTargRepRepository extends CrudRepository<TargRepEsCell, Long> {

    @Query("SELECT " +
            "TargRepGene.accId AS mgiAccessionId, " +
            "TargRepAllele.assembly AS assembly, " +
            "TargRepAllele.cassette AS cassette, " +
            "TargRepAllele.cassetteStart AS cassetteStart, " +
            "TargRepAllele.cassetteEnd AS cassetteEnd, " +
            "TargRepAllele.loxpStart AS loxpStart, " +
            "TargRepAllele.loxpEnd AS loxpEnd, " +
            "TargRepPipeline.name AS pipeline, " +
            "TargRepEsCell.ikmcProjectName AS ikmcProjectId, " +
            "TargRepEsCell.name AS esCellClone, " +
            "TargRepEsCell.parentalCellLine AS parentalCellLine, " +
            "CASE WHEN (TargRepMutationSubtype.name IN ('Insertion', 'Point Mutation')) THEN TargRepMutationSubtype.name " +
            "     ELSE (CASE WHEN (TargRepMutationType.name = '''''') THEN 'Deletion' ELSE TargRepMutationType.name END)" +
            "     END AS mutationType, " +
            "TargRepMutationSubtype.name AS mutationSubtype " +
            "FROM " +
            "TargRepEsCell " +
            "LEFT JOIN EsCellAttempt ON EsCellAttempt.targRepEsCellId = TargRepEsCell.id " +
            "INNER JOIN Plan ON Plan = EsCellAttempt.plan " +
            "INNER JOIN Project ON Project = Plan.project " +
            "INNER JOIN Privacy ON Privacy = Project.privacy " +
            "LEFT JOIN TargRepPipeline ON TargRepPipeline = TargRepEsCell.pipeline " +
            "INNER JOIN TargRepAllele ON TargRepAllele = TargRepEsCell.allele " +
            "INNER JOIN TargRepMutationType ON TargRepMutationType = TargRepAllele.mutationType " +
            "LEFT JOIN TargRepMutationSubtype ON TargRepMutationSubtype = TargRepAllele.mutationSubtype " +
            "INNER JOIN TargRepGene ON TargRepGene = TargRepAllele.gene " +
            "WHERE " +
            "Privacy.name='public' AND " +
            "(TargRepEsCell.reportToPublic = TRUE OR EsCellAttempt.targRepEsCellId IS NOT NULL) ")
    List<MgiEsCellAlleleTargRepProjection> findAllTargRepProjections();
}
