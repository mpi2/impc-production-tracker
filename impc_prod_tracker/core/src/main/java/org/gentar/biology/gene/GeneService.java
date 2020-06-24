/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.gene;
import org.gentar.exceptions.UserOperationFailedException;

import java.util.List;

public interface GeneService
{
    List<Gene> getGenesBySymbolStartingWith(String symbol);

    /**
     * Find a gene by it's accession id.
     * @param accessionId Accession id.
     * @return Gene object if something was found. Null otherwise.
     */
    Gene getGeneByAccessionId(String accessionId);

    /**
     * Find a gene by it's symbol.
     * @param symbol Accession id.
     * @return Gene object if something was found. Null otherwise.
     */
    Gene getGeneBySymbol(String symbol);

    /**
     * Saves a gene in the database.
     * @param gene Gene in memory.
     * @return Created gene.
     */
    Gene create(Gene gene);

    /**
     * Given an accession id or a symbol, this method returns a gene matching it.
     *
     * First this looks in the local gene table. If it is not found there, it looks in the
     * reference data. If no data is found, this tries to see if the input is actually a synonym in
     * the reference data. If no data is found as a synonym either, then null is returned.
     *
     * If the gene is found in the external reference data, then the result is copied in the local
     * gene table.
     * @param accessionIdOrSymbol String with the accession id or the symbol for the gene.
     * @return Gene object or null.
     */
    Gene findAndCreateInLocalIfNeeded(String accessionIdOrSymbol);

    /**
     * Given an accession id or a symbol, this method returns a gene matching it.
     * Fails if the gene is not found.
     * @param accessionIdOrSymbol String with the accession id or the symbol for the gene. Calls
     *                            findAndCreateInLocalIfNeeded(accessionIdOrSymbol).
     * @return Gene object.
     * @throws UserOperationFailedException
     */
    Gene findAndCreateInLocalIfNeededFailIfNull(String accessionIdOrSymbol)
    throws UserOperationFailedException;
}
