package uk.ac.ebi.impc_prod_tracker.web.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.service.GeneService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GeneController {

    private GeneService geneService;

    public GeneController (GeneService geneService)
    {
        this.geneService = geneService;
    }

    @GetMapping(value = {"/genes"})
    public List<?> getGeneSymbols (@RequestParam String symbol)
    {
//        if (specie.equals("mouse"))
//        {
//            return geneService.getMouseGenesBySymbol(StringUtils.capitalize(symbol));
//        }
//        else if (specie.equals("human"))
//        {
//            return humanGeneService.getHumanGenesBySymbol(symbol.toUpperCase());
//        }

        return geneService.getGenesBySymbol(StringUtils.capitalize(symbol));
    }
}
