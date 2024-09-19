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

}
