/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.gene.external_ref;

import org.gentar.biology.gene.Gene;
import java.util.List;
import java.util.Map;

public interface GeneExternalService
{
    /**
     * Get the gene from the external reference data given its symbol or accession id.
     * @param input Symbol or accession id.
     * @return A {@link Gene} object.
     */
    Gene getGeneFromExternalDataBySymbolOrAccId(String input);

    /**
     * Get a map of marker symbols and their respective accession ids.
     * @param inputs list of marker symbols
     * @return Map with the association accession id-symbol.
     */
    Map<String, String> getAccIdsByMarkerSymbols(List<String> inputs);

    /**
     * Gets a map with the format <accessionId, symbol> for the list of accessionIds.
     * @param accessionIds List of accession ids.
     * @return Map of strings with accession ids and symbols.
     */
    Map<String, String> getSymbolsByAccessionIds(List<String> accessionIds);

    Map<String, String> getSymbolsByAccessionIdsBulk(List<String> accIds);

    /**
     * Get list of genes that partially match the given input.
     * @param input Part of the accession id or symbol.
     * @return List of genes that partially match the given accession id or symbol.
     */
    List<Gene> getGenesFromExternalDataBySymbolOrAccId(String input);

    Gene getSynonymFromExternalGenes(String symbol);
}
