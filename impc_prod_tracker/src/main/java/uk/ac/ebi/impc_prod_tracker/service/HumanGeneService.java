package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGeneRepository;

import java.util.List;

@Component
public class HumanGeneService {
    private HumanGeneRepository humanGeneRepository;

    public HumanGeneService (HumanGeneRepository humanGeneRepository) { this.humanGeneRepository = humanGeneRepository; }

    public List<HumanGene> getHumanGenesBySymbol (String symbol)
    {
        return humanGeneRepository.findBySymbolStartingWith(symbol);
    }
}
