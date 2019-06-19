package uk.ac.ebi.impc_prod_tracker.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.service.HumanGeneService;
import uk.ac.ebi.impc_prod_tracker.service.MouseGeneService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GeneController {

    private MouseGeneService mouseGeneService;
    private HumanGeneService humanGeneService;

    public GeneController (MouseGeneService mouseGeneService, HumanGeneService humanGeneService)
    {
        this.mouseGeneService = mouseGeneService;
        this.humanGeneService = humanGeneService;
    }

    @GetMapping(value = {"/genes"})
    public List<?> getGeneSymbols (@RequestParam String symbol, @RequestParam String specie)
    {
        if (specie.equals("mouse"))
        {
            return mouseGeneService.getMouseGenesBySymbol(StringUtils.capitalize(symbol));
        }
        else if (specie.equals("human"))
        {
            return humanGeneService.getHumanGenesBySymbol(symbol.toUpperCase());
        }

        return null;
    }
}
