package org.gentar.report.collection.mgi_es_cell_allele.targ_rep;

import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MgiEsCellAlleleTargRepRepository extends CrudRepository<TargRepEsCell, Long> {

    @Query(value = "SELECT targ_rep_gene.acc_id AS mgiAccessionId," +
            "             targ_rep_allele.assembly AS assembly, " +
            "             targ_rep_allele.cassette AS cassette," +
            "             targ_rep_allele.cassette_start AS cassetteStart, " +
            "             targ_rep_allele.cassette_end AS cassetteEnd, " +
            "             targ_rep_allele.loxp_start AS loxpStart, " +
            "             targ_rep_allele.loxp_end AS loxpEnd," +
            "             targ_rep_pipeline.name AS pipeline, " +
            "             targ_rep_es_cell.ikmc_project_name AS ikmcProjectId, " +
            "             targ_rep_es_cell.name AS esCellClone, " +
            "             targ_rep_es_cell.parental_cell_line AS parentCellLine," +
            "             targ_rep_production_qc.mgi_allele_symbol_superscript AS alleleSymbolSuperscript," +
            "             CASE WHEN targ_rep_mutation_subtype.name IN ('Insertion', 'Point Mutation') THEN targ_rep_mutation_subtype.name " +
            "                  WHEN targ_rep_production_qc.allele_type IS NOT NULL THEN (CASE WHEN targ_rep_production_qc.allele_type = '''''' THEN 'Deletion' ELSE targ_rep_production_qc.allele_type END) " +
            "                 ELSE (CASE WHEN targ_rep_mutation_type.name = '''''' THEN 'Deletion' ELSE targ_rep_mutation_type.name END)" +
            "             END AS mutationType," +
            "             targ_rep_mutation_subtype.name AS mutationSubtype" +
            "      FROM targ_rep_es_cell" +
            "        LEFT JOIN es_cell_attempt ON es_cell_attempt.targ_rep_es_cell_id = targ_rep_es_cell.id " +
            "        LEFT JOIN targ_rep_production_qc on targ_rep_production_qc.es_cell_id = targ_rep_es_cell.id" +
            "        LEFT JOIN targ_rep_pipeline ON targ_rep_pipeline.id = targ_rep_es_cell.pipeline_id" +
            "        INNER JOIN targ_rep_allele ON targ_rep_allele.id = targ_rep_es_cell.allele_id" +
            "        INNER JOIN targ_rep_mutation_type ON targ_rep_mutation_type.id = targ_rep_allele.mutation_type_id" +
            "        LEFT JOIN targ_rep_mutation_subtype ON targ_rep_mutation_subtype.id = targ_rep_allele.mutation_subtype_id" +
            "        INNER JOIN targ_rep_gene ON targ_rep_gene.id = targ_rep_allele.gene_id" +
            "      WHERE(targ_rep_es_cell.report_to_public = true OR es_cell_attempt.targ_rep_es_cell_id IS NOT NULL)" +
            "        AND targ_rep_allele.type = 'TargRep::TargetedAllele'" +
            "        AND targ_rep_es_cell.name in :clones", nativeQuery = true)
    List<MgiEsCellAlleleTargRepProjection> findSelectedTargRepProjections(@Param("clones") List<String> cloneList);

    @Query(value = "SELECT targ_rep_es_cell.name AS esCellClone " +
            "      FROM targ_rep_es_cell" +
            "        LEFT JOIN es_cell_attempt ON es_cell_attempt.targ_rep_es_cell_id = targ_rep_es_cell.id " +
            "        LEFT JOIN targ_rep_production_qc on targ_rep_production_qc.es_cell_id = targ_rep_es_cell.id" +
            "        LEFT JOIN targ_rep_pipeline ON targ_rep_pipeline.id = targ_rep_es_cell.pipeline_id" +
            "        INNER JOIN targ_rep_allele ON targ_rep_allele.id = targ_rep_es_cell.allele_id" +
            "        INNER JOIN targ_rep_mutation_type ON targ_rep_mutation_type.id = targ_rep_allele.mutation_type_id" +
            "        LEFT JOIN targ_rep_mutation_subtype ON targ_rep_mutation_subtype.id = targ_rep_allele.mutation_subtype_id" +
            "        INNER JOIN targ_rep_gene ON targ_rep_gene.id = targ_rep_allele.gene_id" +
            "      WHERE(targ_rep_es_cell.report_to_public = true OR es_cell_attempt.targ_rep_es_cell_id IS NOT NULL)" +
            "        AND targ_rep_allele.type = 'TargRep::TargetedAllele'", nativeQuery = true)
    List<MgiESCellAlleleTargRepESCellCloneProjection> findAllTargRepESCellCloneProjections();
}
