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
package uk.ac.ebi.impc_prod_tracker.web.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.service.biology.GeneIdentifierValidator;
import uk.ac.ebi.impc_prod_tracker.service.biology.gene.external_ref.GeneExternalService;
import uk.ac.ebi.impc_prod_tracker.service.biology.gene.GeneService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GeneController {

    private GeneService geneService;
    private GeneExternalService geneExternalService;
    private GeneIdentifierValidator geneIdentifierValidator;

    public GeneController(
        GeneService geneService,
        GeneExternalService geneExternalService,
        GeneIdentifierValidator geneIdentifierValidator)
    {
        this.geneService = geneService;
        this.geneExternalService = geneExternalService;
        this.geneIdentifierValidator = geneIdentifierValidator;
    }

    @GetMapping(value = {"/genes"})
    public List<?> getGeneSymbols (@RequestParam String symbol)
    {
        return geneService.getGenesBySymbol(StringUtils.capitalize(symbol));
    }

    @GetMapping(value = {"/genesInExternalData"})
    public List<Gene> getGeneInExternalData (@RequestParam String input)
    {
        validateInput(input);
        return geneExternalService.getGenesFromExternalDataBySymbolOrAccId(input);
    }

    private void validateInput(String input)
    {
        int MINIMUM_SIGNIFICANT_CHARACTERS = 3;
        geneIdentifierValidator.validateIdentifier(input, MINIMUM_SIGNIFICANT_CHARACTERS);
    }

    @GetMapping(value = {"/geneSynonymInExternalData"})
    public Gene getSynonymInExternalData (@RequestParam String symbol)
    {
        return geneExternalService.getSynonymFromExternalGenes(symbol);
    }
}
