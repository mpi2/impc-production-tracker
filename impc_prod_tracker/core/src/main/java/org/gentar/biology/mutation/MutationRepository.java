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

import org.gentar.biology.mutation.genome_browser.GenomeBrowserCombinedProjection;
import org.gentar.biology.mutation.genome_browser.GenomeBrowserProjection;
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

    @Query(value = "WITH cte AS (SELECT wu.name                                                                 AS centre,\n" +
            "                    m.min                                                                   AS min,\n" +
            "                    m.symbol                                                                AS allele_symbol,\n" +
            "                    g.symbol                                                                AS gene_symbol,\n" +
            "                    s.sequence                                                              AS fasta,\n" +
            "                    'https://www.gentar.org/#/projects/' || p.tpn || '/plans/' || plan.pin ||\n" +
            "                    '/outcomes/' ||   o.tpo                                                   AS url,\n" +
            "                    ROW_NUMBER() OVER (PARTITION BY mmd.mutation_id ORDER BY mmd.mutation_id) as rn\n" +
            "             FROM Gene g\n" +
            "                      INNER JOIN mutation_gene mg on g.id = mg.gene_id\n" +
            "                      INNER JOIN mutation_outcome mo on mg.mutation_id = mo.mutation_id\n" +
            "                      INNER JOIN mutation m ON mg.mutation_id = m.id\n" +
            "                      INNER JOIN outcome o ON mo.outcome_id = o.id\n" +
            "                      INNER JOIN outcome_type ot ON o.outcome_type_id = ot.id\n" +
            "                      INNER JOIN Plan plan ON o.plan_id = plan.id\n" +
            "                      INNER JOIN gentar.work_unit wu on wu.id = plan.work_unit_id\n" +
            "                      INNER JOIN Project p ON plan.project_id = p.id\n" +
            "                      INNER JOIN gentar.mutation_sequence ms on m.id = ms.mutation_id\n" +
            "                      INNER JOIN gentar.sequence s on s.id = ms.sequence_id\n" +
            "                      INNER JOIN gentar.molecular_mutation_deletion mmd on m.id = mmd.mutation_id)\n" +
            "SELECT centre, min, allele_symbol, gene_symbol, url, fasta\n" +
            "FROM cte\n" +
            "WHERE rn = 1", nativeQuery = true)
    List<GenomeBrowserProjection> findAllDeletionCoordinates();

    @Query(value = "WITH cte AS (SELECT wu.name                                                                 AS centre,\n" +
            "                    m.min                                                                   AS min,\n" +
            "                    m.symbol                                                                AS allele_symbol,\n" +
            "                    g.symbol                                                                AS gene_symbol,\n" +
            "                    s.sequence                                                              AS fasta,\n" +
            "                    'https://www.gentar.org/#/projects/' || p.tpn || '/plans/' || plan.pin ||\n" +
            "                    '/outcomes/' ||   o.tpo                                                   AS url,\n" +
            "                    ROW_NUMBER() OVER (PARTITION BY te.mutation_id ORDER BY te.mutation_id) as rn\n" +
            "             FROM Gene g\n" +
            "                      INNER JOIN mutation_gene mg on g.id = mg.gene_id\n" +
            "                      INNER JOIN mutation_outcome mo on mg.mutation_id = mo.mutation_id\n" +
            "                      INNER JOIN mutation m ON mg.mutation_id = m.id\n" +
            "                      INNER JOIN outcome o ON mo.outcome_id = o.id\n" +
            "                      INNER JOIN outcome_type ot ON o.outcome_type_id = ot.id\n" +
            "                      INNER JOIN Plan plan ON o.plan_id = plan.id\n" +
            "                      INNER JOIN gentar.work_unit wu on wu.id = plan.work_unit_id\n" +
            "                      INNER JOIN Project p ON plan.project_id = p.id\n" +
            "                      INNER JOIN gentar.mutation_sequence ms on m.id = ms.mutation_id\n" +
            "                      INNER JOIN gentar.sequence s on s.id = ms.sequence_id\n" +
            "                      INNER JOIN gentar.targeted_exon te on m.id = te.mutation_id)\n" +
            "SELECT centre, min, allele_symbol, gene_symbol, url, fasta\n" +
            "FROM cte\n" +
            "WHERE rn = 1", nativeQuery = true)
    List<GenomeBrowserProjection> findAllTargetedExons();


    @Query(value = "WITH cte AS (SELECT wu.name                                                                 AS centre,\n" +
            "                    m.min                                                                   AS min,\n" +
            "                    m.symbol                                                                AS allele_symbol,\n" +
            "                    g.symbol                                                                AS gene_symbol,\n" +
            "                    s.sequence                                                                 AS fasta,\n" +
            "                    'https://www.gentar.org/#/projects/' || p.tpn || '/plans/' || plan.pin ||\n" +
            "                    '/outcomes/' ||   o.tpo                                                   AS url,\n" +
            "                    ROW_NUMBER() OVER (PARTITION BY ctex.mutation_id ORDER BY ctex.mutation_id) as rn\n" +
            "             FROM Gene g\n" +
            "                      INNER JOIN mutation_gene mg on g.id = mg.gene_id\n" +
            "                      INNER JOIN mutation_outcome mo on mg.mutation_id = mo.mutation_id\n" +
            "                      INNER JOIN mutation m ON mg.mutation_id = m.id\n" +
            "                      INNER JOIN outcome o ON mo.outcome_id = o.id\n" +
            "                      INNER JOIN outcome_type ot ON o.outcome_type_id = ot.id\n" +
            "                      INNER JOIN Plan plan ON o.plan_id = plan.id\n" +
            "                      INNER JOIN gentar.work_unit wu on wu.id = plan.work_unit_id\n" +
            "                      INNER JOIN Project p ON plan.project_id = p.id\n" +
            "                      INNER JOIN gentar.mutation_sequence ms on m.id = ms.mutation_id\n" +
            "                      INNER JOIN gentar.sequence s on s.id = ms.sequence_id\n" +
            "                      INNER JOIN gentar.canonical_targeted_exon ctex on m.id = ctex.mutation_id)\n" +
            "SELECT centre, min, allele_symbol, gene_symbol, url, fasta\n" +
            "FROM cte\n" +
            "WHERE rn = 1", nativeQuery = true)
    List<GenomeBrowserProjection> findAllCanonicalTargetedExons();



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

}
