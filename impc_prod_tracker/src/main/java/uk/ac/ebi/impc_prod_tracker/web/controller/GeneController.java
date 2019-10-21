package uk.ac.ebi.impc_prod_tracker.web.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.service.gene.external_ref.GeneExternalService;
import uk.ac.ebi.impc_prod_tracker.service.gene.GeneService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GeneController {

    private GeneService geneService;
    private GeneExternalService geneExternalService;

    public GeneController(GeneService geneService, GeneExternalService geneExternalService)
    {
        this.geneService = geneService;
        this.geneExternalService = geneExternalService;
    }

    @GetMapping(value = {"/genes"})
    public List<?> getGeneSymbols (@RequestParam String symbol)
    {
        return geneService.getGenesBySymbol(StringUtils.capitalize(symbol));
    }

    @GetMapping(value = {"/geneInExternalData"})
    public Gene getGeneInExternalData (@RequestParam String symbol)
    {
        return geneExternalService.getFromExternalGenesBySymbolOrAccId(symbol);
    }

    @GetMapping(value = {"/geneSynonymInExternalData"})
    public Gene getSynonymInExternalData (@RequestParam String symbol)
    {
        return geneExternalService.getSynonymFromExternalGenes(symbol);
    }
}
