/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.mutation;

import org.gentar.biology.mutation.genome_browser.DeletionCoordinatesProjection;
import org.gentar.biology.mutation.genome_browser.GenomeBrowserCombinedProjection;
import org.gentar.biology.mutation.genome_browser.SerializedGuideProjection;
import org.gentar.biology.mutation.genome_browser.TargetedExonsProjection;
import org.gentar.biology.mutation.mutation_ensembl.mutationEnsemblMutationPartProjection;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Primary
public interface MutationRepository extends CrudRepository<Mutation, Long>
{
    @Query("SELECT max(m.min) FROM Mutation m")
    String getMaxMin();

    Mutation findFirstById(Long id);

    Mutation findByMin(String min);

   List<Mutation> findBySymbol(String symbol);

    List<Mutation> findAllBySymbolLike(String symbolSearchTerm);

    List<Mutation> findBySymbolContaining(String symbolSearchTerm);

    @Query(value = "select min, g.symbol, g.acc_id from mutation m, mutation_gene mg , gene g where m.id = mg.mutation_id and mg.gene_id = g.id and m.min IN (:mins)",
            nativeQuery = true)
    List<mutationEnsemblMutationPartProjection> findMutationEnsembleMutationPartByMins(@Param("mins") List<String> mins);

    @Query(value = "WITH cte AS (SELECT wu.name                                                                                         AS centre,\n" +
            "                    m.min                                                                                           AS min,\n" +
            "                    m.symbol                                                                                        AS allele_symbol,\n" +
            "                    g.symbol                                                                                        AS gene_symbol,\n" +
            "                    s.sequence                                                                                      AS fasta,\n" +
            "                    -- Format deletion coordinates as chr:start-stop\n" +
            "                    CASE\n" +
            "                        WHEN mmd.chr IS NOT NULL AND mmd.start IS NOT NULL AND mmd.stop IS NOT NULL\n" +
            "                            THEN mmd.chr || ':' || mmd.start || '-' || mmd.stop\n" +
            "                        ELSE NULL\n" +
            "                        END                                                                                         AS deletion_coordinate,\n" +
            "                    'https://www.gentar.org/#/projects/' || p.tpn || '/plans/' || plan.pin || '/outcomes/' ||\n" +
            "                    o.tpo                                                                                           AS url\n" +
            "             FROM Gene g\n" +
            "                      INNER JOIN mutation_gene mg ON g.id = mg.gene_id\n" +
            "                      INNER JOIN mutation_outcome mo ON mg.mutation_id = mo.mutation_id\n" +
            "                      INNER JOIN mutation m ON mg.mutation_id = m.id\n" +
            "                      INNER JOIN outcome o ON mo.outcome_id = o.id\n" +
            "                      INNER JOIN outcome_type ot ON o.outcome_type_id = ot.id\n" +
            "                      INNER JOIN Plan plan ON o.plan_id = plan.id\n" +
            "                      INNER JOIN gentar.work_unit wu ON wu.id = plan.work_unit_id\n" +
            "                      INNER JOIN Project p ON plan.project_id = p.id\n" +
            "                      INNER JOIN gentar.mutation_sequence ms ON m.id = ms.mutation_id\n" +
            "                      INNER JOIN gentar.sequence s ON s.id = ms.sequence_id\n" +
            "                      LEFT JOIN gentar.molecular_mutation_deletion mmd ON m.id = mmd.mutation_id),\n" +
            "     cte_grouped AS (SELECT min,\n" +
            "                            centre,\n" +
            "                            allele_symbol,\n" +
            "                            gene_symbol,\n" +
            "                            url,\n" +
            "                            fasta,\n" +
            "                            STRING_AGG(DISTINCT deletion_coordinate, ', ')            AS deletion_coordinates\n" +
            "                     FROM cte\n" +
            "                     GROUP BY min, centre, allele_symbol, gene_symbol, url, fasta)\n" +
            "\n" +
            "SELECT centre,\n" +
            "       min,\n" +
            "       allele_symbol,\n" +
            "       gene_symbol,\n" +
            "       deletion_coordinates,\n" +
            "       fasta,\n" +
            "       url\n" +
            "FROM cte_grouped;", nativeQuery = true)
    List<DeletionCoordinatesProjection> findAllDeletionCoordinates();

    @Query(value = "WITH cte AS (SELECT wu.name                                                                                         AS centre,\n" +
            "                    m.min                                                                                           AS min,\n" +
            "                    m.symbol                                                                                        AS allele_symbol,\n" +
            "                    g.symbol                                                                                        AS gene_symbol,\n" +
            "                    s.sequence                                                                                      AS fasta,\n" +
            "                    tex.exon_id                                                                                     AS exons,\n" +
            "                    'https://www.gentar.org/#/projects/' || p.tpn || '/plans/' || plan.pin || '/outcomes/' ||\n" +
            "                    o.tpo                                                                                           AS url\n" +
            "             FROM Gene g\n" +
            "                      INNER JOIN mutation_gene mg ON g.id = mg.gene_id\n" +
            "                      INNER JOIN mutation_outcome mo ON mg.mutation_id = mo.mutation_id\n" +
            "                      INNER JOIN mutation m ON mg.mutation_id = m.id\n" +
            "                      INNER JOIN outcome o ON mo.outcome_id = o.id\n" +
            "                      INNER JOIN outcome_type ot ON o.outcome_type_id = ot.id\n" +
            "                      INNER JOIN Plan plan ON o.plan_id = plan.id\n" +
            "                      INNER JOIN gentar.work_unit wu ON wu.id = plan.work_unit_id\n" +
            "                      INNER JOIN Project p ON plan.project_id = p.id\n" +
            "                      INNER JOIN gentar.mutation_sequence ms ON m.id = ms.mutation_id\n" +
            "                      INNER JOIN gentar.sequence s ON s.id = ms.sequence_id\n" +
            "                      LEFT JOIN gentar.targeted_exon tex ON m.id = tex.mutation_id),\n" +
            "     cte_grouped AS (SELECT min,\n" +
            "                            centre,\n" +
            "                            allele_symbol,\n" +
            "                            gene_symbol,\n" +
            "                            url,\n" +
            "                            fasta,\n" +
            "                            STRING_AGG(DISTINCT exons::TEXT, ', ')           AS exons\n" +
            "                     FROM cte\n" +
            "                     GROUP BY min, centre, allele_symbol, gene_symbol, url, fasta)\n" +
            "\n" +
            "SELECT centre,\n" +
            "       min,\n" +
            "       allele_symbol,\n" +
            "       gene_symbol,\n" +
            "       exons,\n" +
            "       fasta,\n" +
            "       url\n" +
            "FROM cte_grouped;", nativeQuery = true)
    List<TargetedExonsProjection> findAllTargetedExons();


    @Query(value = "WITH cte AS (SELECT wu.name                                                                                         AS centre,\n" +
            "                    m.min                                                                                           AS min,\n" +
            "                    m.symbol                                                                                        AS allele_symbol,\n" +
            "                    g.symbol                                                                                        AS gene_symbol,\n" +
            "                    s.sequence                                                                                      AS fasta,\n" +
            "                    tex.exon_id                                                                                     AS exons,\n" +
            "                    'https://www.gentar.org/#/projects/' || p.tpn || '/plans/' || plan.pin || '/outcomes/' ||\n" +
            "                    o.tpo                                                                                           AS url\n" +
            "             FROM Gene g\n" +
            "                      INNER JOIN mutation_gene mg ON g.id = mg.gene_id\n" +
            "                      INNER JOIN mutation_outcome mo ON mg.mutation_id = mo.mutation_id\n" +
            "                      INNER JOIN mutation m ON mg.mutation_id = m.id\n" +
            "                      INNER JOIN outcome o ON mo.outcome_id = o.id\n" +
            "                      INNER JOIN outcome_type ot ON o.outcome_type_id = ot.id\n" +
            "                      INNER JOIN Plan plan ON o.plan_id = plan.id\n" +
            "                      INNER JOIN gentar.work_unit wu ON wu.id = plan.work_unit_id\n" +
            "                      INNER JOIN Project p ON plan.project_id = p.id\n" +
            "                      INNER JOIN gentar.mutation_sequence ms ON m.id = ms.mutation_id\n" +
            "                      INNER JOIN gentar.sequence s ON s.id = ms.sequence_id\n" +
            "                      LEFT JOIN gentar.canonical_targeted_exon tex ON m.id = tex.mutation_id),\n" +
            "     cte_grouped AS (SELECT min,\n" +
            "                            centre,\n" +
            "                            allele_symbol,\n" +
            "                            gene_symbol,\n" +
            "                            url,\n" +
            "                            fasta,\n" +
            "                            STRING_AGG(DISTINCT exons::TEXT, ', ')           AS exons\n" +
            "                     FROM cte\n" +
            "                     GROUP BY min, centre, allele_symbol, gene_symbol, url, fasta)\n" +
            "\n" +
            "SELECT centre,\n" +
            "       min,\n" +
            "       allele_symbol,\n" +
            "       gene_symbol,\n" +
            "       exons,\n" +
            "       fasta,\n" +
            "       url\n" +
            "FROM cte_grouped;", nativeQuery = true)
    List<TargetedExonsProjection> findAllCanonicalTargetedExons();



    @Query(value = "WITH cte AS (\n" +
            "    SELECT\n" +
            "        wu.name AS centre,\n" +
            "        m.min AS min,\n" +
            "        m.symbol AS allele_symbol,\n" +
            "        g.symbol AS gene_symbol,\n" +
            "        s.sequence AS fasta,\n" +
            "        -- Format deletion coordinates as chr:start-stop\n" +
            "        CASE\n" +
            "            WHEN mmd.chr IS NOT NULL AND mmd.start IS NOT NULL AND mmd.stop IS NOT NULL\n" +
            "            THEN mmd.chr || ':' || mmd.start || '-' || mmd.stop\n" +
            "            ELSE NULL\n" +
            "        END AS deletion_coordinate,\n" +
            "        tex.exon_id AS targeted_exons,\n" +
            "        ctex.exon_id AS canonical_targeted_exons,\n" +
            "        'https://www.gentar.org/#/projects/' || p.tpn || '/plans/' || plan.pin || '/outcomes/' || o.tpo AS url\n" +
            "    FROM Gene g\n" +
            "    INNER JOIN mutation_gene mg ON g.id = mg.gene_id\n" +
            "    INNER JOIN mutation_outcome mo ON mg.mutation_id = mo.mutation_id\n" +
            "    INNER JOIN mutation m ON mg.mutation_id = m.id\n" +
            "    INNER JOIN outcome o ON mo.outcome_id = o.id\n" +
            "    INNER JOIN outcome_type ot ON o.outcome_type_id = ot.id\n" +
            "    INNER JOIN Plan plan ON o.plan_id = plan.id\n" +
            "    INNER JOIN gentar.work_unit wu ON wu.id = plan.work_unit_id\n" +
            "    INNER JOIN Project p ON plan.project_id = p.id\n" +
            "    INNER JOIN gentar.mutation_sequence ms ON m.id = ms.mutation_id\n" +
            "    INNER JOIN gentar.sequence s ON s.id = ms.sequence_id\n" +
            "    LEFT JOIN gentar.molecular_mutation_deletion mmd ON m.id = mmd.mutation_id\n" +
            "    LEFT JOIN gentar.targeted_exon tex ON m.id = tex.mutation_id\n" +
            "    LEFT JOIN gentar.canonical_targeted_exon ctex ON m.id = ctex.mutation_id\n" +
            "),\n" +
            "cte_grouped AS (\n" +
            "    SELECT\n" +
            "        min,\n" +
            "        centre,\n" +
            "        allele_symbol,\n" +
            "        gene_symbol,\n" +
            "        url,\n" +
            "        fasta,\n" +
            "        STRING_AGG(DISTINCT deletion_coordinate, ', ') AS deletion_coordinates,\n" +
            "        STRING_AGG(DISTINCT targeted_exons::TEXT, ', ') AS targeted_exons,\n" +
            "        STRING_AGG(DISTINCT canonical_targeted_exons::TEXT, ', ') AS canonical_targeted_exons\n" +
            "    FROM cte\n" +
            "    GROUP BY min, centre, allele_symbol, gene_symbol, url, fasta\n" +
            ")\n" +
            "\n" +
            "SELECT\n" +
            "    centre,\n" +
            "    min,\n" +
            "    allele_symbol,\n" +
            "    gene_symbol,\n" +
            "    deletion_coordinates,\n" +
            "    targeted_exons,\n" +
            "    canonical_targeted_exons,\n" +
            "    fasta,\n" +
            "    url\n" +
            "FROM cte_grouped;", nativeQuery = true)
    List<GenomeBrowserCombinedProjection> findAllGenomeBrowserProjections();

    @Query(value = "WITH cte AS (\n" +
            "    SELECT\n" +
            "        wu.name AS centre,\n" +
            "        m.min AS min,\n" +
            "        m.symbol AS allele_symbol,\n" +
            "        g.symbol AS gene_symbol,\n" +
            "        s.sequence AS fasta,\n" +
            "        -- Format deletion coordinates as chr:start-stop\n" +
            "        CASE\n" +
            "            WHEN mmd.chr IS NOT NULL AND mmd.start IS NOT NULL AND mmd.stop IS NOT NULL\n" +
            "            THEN mmd.chr || ':' || mmd.start || '-' || mmd.stop\n" +
            "            ELSE NULL\n" +
            "        END AS deletion_coordinate,\n" +
            "        tex.exon_id AS targeted_exons,\n" +
            "        ctex.exon_id AS canonical_targeted_exons,\n" +
            "        'https://www.gentar.org/#/projects/' || p.tpn || '/plans/' || plan.pin || '/outcomes/' || o.tpo AS url\n" +
            "    FROM Gene g\n" +
            "    INNER JOIN mutation_gene mg ON g.id = mg.gene_id\n" +
            "    INNER JOIN mutation_outcome mo ON mg.mutation_id = mo.mutation_id\n" +
            "    INNER JOIN mutation m ON mg.mutation_id = m.id\n" +
            "    INNER JOIN outcome o ON mo.outcome_id = o.id\n" +
            "    INNER JOIN outcome_type ot ON o.outcome_type_id = ot.id\n" +
            "    INNER JOIN Plan plan ON o.plan_id = plan.id\n" +
            "    INNER JOIN gentar.work_unit wu ON wu.id = plan.work_unit_id\n" +
            "    INNER JOIN Project p ON plan.project_id = p.id\n" +
            "    INNER JOIN gentar.mutation_sequence ms ON m.id = ms.mutation_id\n" +
            "    INNER JOIN gentar.sequence s ON s.id = ms.sequence_id\n" +
            "    LEFT JOIN gentar.molecular_mutation_deletion mmd ON m.id = mmd.mutation_id\n" +
            "    LEFT JOIN gentar.targeted_exon tex ON m.id = tex.mutation_id\n" +
            "    LEFT JOIN gentar.canonical_targeted_exon ctex ON m.id = ctex.mutation_id\n" +
            "),\n" +
            "cte_grouped AS (\n" +
            "    SELECT\n" +
            "        min,\n" +
            "        centre,\n" +
            "        allele_symbol,\n" +
            "        gene_symbol,\n" +
            "        url,\n" +
            "        fasta,\n" +
            "        STRING_AGG(DISTINCT deletion_coordinate, ', ') AS deletion_coordinates,\n" +
            "        STRING_AGG(DISTINCT targeted_exons::TEXT, ', ') AS targeted_exons,\n" +
            "        STRING_AGG(DISTINCT canonical_targeted_exons::TEXT, ', ') AS canonical_targeted_exons\n" +
            "    FROM cte\n" +
            "    GROUP BY min, centre, allele_symbol, gene_symbol, url, fasta\n" +
            ")\n" +
            "\n" +
            "SELECT\n" +
            "    centre,\n" +
            "    min,\n" +
            "    allele_symbol,\n" +
            "    gene_symbol,\n" +
            "    deletion_coordinates,\n" +
            "    targeted_exons,\n" +
            "    canonical_targeted_exons,\n" +
            "    fasta,\n" +
            "    url\n" +
            "FROM cte_grouped where centre = :workUnit;", nativeQuery = true)
    List<GenomeBrowserCombinedProjection> findAllGenomeBrowserProjectionsByWorkuUnit(@Param("workUnit") String workUnit);


    @Query(value = "select\n" +
            "    p.pin,\n" +
            "    CASE\n" +
            "        WHEN g.chr='x' THEN 'chrX'\n" +
            "        WHEN g.chr is null THEN 'NOT SPECIFIED'\n" +
            "        WHEN g.chr='GL456233.2' THEN g.chr\n" +
            "        ELSE 'chr' || g.chr\n" +
            "    END AS \"chrom\",\n" +
            "\n" +
            "    CASE\n" +
            "       WHEN nt.name in ('Cas9', 'D10A') THEN\n" +
            "          CASE\n" +
            "              WHEN g.strand='+' THEN (g.start - 1)\n" +
            "              WHEN g.strand='-' THEN ((g.start -1) + length(g.pam))\n" +
            "          END\n" +
            "       WHEN  nt.name in ('Cpf1') THEN\n" +
            "          CASE\n" +
            "              WHEN g.strand='+' THEN ((g.start - 1) + length(g.pam))\n" +
            "              WHEN g.strand='-' THEN (g.start -1)\n" +
            "          END\n" +
            "    END AS \"chrom_start\",\n" +
            "\n" +
            "    CASE\n" +
            "       WHEN nt.name in ('Cas9', 'D10A') THEN\n" +
            "          CASE\n" +
            "              WHEN g.strand='+' THEN (g.stop - length(g.pam))\n" +
            "              WHEN g.strand='-' THEN g.stop\n" +
            "          END\n" +
            "       WHEN  nt.name in ('Cpf1') THEN\n" +
            "          CASE\n" +
            "              WHEN g.strand='+' THEN g.stop\n" +
            "              WHEN g.strand='-' THEN (g.stop - length(g.pam))\n" +
            "          END\n" +
            "    END AS \"chrom_end\",\n" +
            "\n" +
            "    m.symbol || '_' || g.gid as \"guide_name\",\n" +
            "    0 AS \"score\",\n" +
            "\n" +
            "    CASE\n" +
            "        WHEN g.strand is null THEN '.'\n" +
            "        WHEN g.strand='' THEN '.'\n" +
            "        ELSE g.strand\n" +
            "    END AS \"strand\",\n" +
            "\n" +
            "    CASE\n" +
            "       WHEN nt.name in ('Cas9', 'D10A') THEN\n" +
            "          CASE\n" +
            "              WHEN g.strand='+' THEN (g.start - 1)\n" +
            "              WHEN g.strand='-' THEN ((g.start -1) + length(g.pam))\n" +
            "          END\n" +
            "       WHEN  nt.name in ('Cpf1') THEN\n" +
            "          CASE\n" +
            "              WHEN g.strand='+' THEN ((g.start - 1) + length(g.pam))\n" +
            "              WHEN g.strand='-' THEN (g.start -1)\n" +
            "          END\n" +
            "    END AS \"thick_start\",\n" +
            "\n" +
            "    CASE\n" +
            "       WHEN nt.name in ('Cas9', 'D10A') THEN\n" +
            "          CASE\n" +
            "              WHEN g.strand='+' THEN (g.stop - length(g.pam))\n" +
            "              WHEN g.strand='-' THEN g.stop\n" +
            "          END\n" +
            "       WHEN  nt.name in ('Cpf1') THEN\n" +
            "          CASE\n" +
            "              WHEN g.strand='+' THEN g.stop\n" +
            "              WHEN g.strand='-' THEN (g.stop - length(g.pam))\n" +
            "          END\n" +
            "    END AS \"thick_end\",\n" +
            "\n" +
            "    '0,0,255' AS \"item_rgb\",\n" +
            "    m.min as min\n" +
            "FROM\n" +
            "    merged_guide g\n" +
            "    INNER JOIN plan p on g.attempt_id = p.id\n" +
            "    INNER JOIN nuclease n on p.id = n.attempt_id\n" +
            "    INNER JOIN nuclease_type nt on n.nuclease_type_id = nt.id\n" +
            "    INNER JOIN status ps on p.status_id = ps.id\n" +
            "    INNER JOIN outcome o on p.id = o.plan_id\n" +
            "    INNER JOIN mutation_outcome mo on o.id = mo.outcome_id\n" +
            "    INNER JOIN mutation m on mo.mutation_id = m.id\n" +
            "    INNER JOIN mutation_gene mg on m.id = mg.mutation_id\n" +
            "    INNER JOIN gene on mg.gene_id = gene.id\n" +
            "    INNER JOIN colony c on o.id = c.outcome_id\n" +
            "    INNER JOIN status s on c.status_id = s.id\n" +
            "    LEFT JOIN mutation_sequence ms on m.id = ms.mutation_id\n" +
            "    LEFT JOIN sequence ss on ms.sequence_id = ss.id\n" +
            "WHERE\n" +
            "    g.start is not null and\n" +
            "    g.stop is not null and\n" +
            "    g.chr is not null and\n" +
            "    g.strand is not null and\n" +
            "    g.pam is not null and\n" +
            "    g.chr != 'NA' and\n" +
            "    g.genome_build='GRCm39' and\n" +
            "    g.chr != 's' and\n" +
            "    g.chr !='GL456233.2' and\n" +
            "    ps.name not in ('Attempt Aborted','Breeding Aborted','Mouse Allele Modification Aborted','Plan Abandoned') and\n" +
            "    nt.name in ('Cas9', 'D10A', 'Cpf1') and\n" +
            "    m.symbol is not null and\n" +
            "    m.symbol <> '' and\n" +
            "    gene.symbol is not null and\n" +
            "    s.name in ('Genotype Confirmed', 'Genotype Extinct') and\n" +
            "    ss.sequence is not null and\n" +
            "    ss. sequence !=''\n" +
            "group by\n" +
            "pin, chrom, chrom_start, chrom_end, guide_name, score, strand, thick_start, thick_end, item_rgb, m.min;\n", nativeQuery = true)
    List<SerializedGuideProjection> findAllSerializedGuides();

}
