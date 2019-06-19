package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene.MouseGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene.MouseGeneRepository;

import java.util.List;

@Component
public class MouseGeneService {
    private MouseGeneRepository mouseGeneRepository;

    public MouseGeneService(MouseGeneRepository mouseGeneRepository) { this.mouseGeneRepository = mouseGeneRepository; }

    public List<MouseGene> getMouseGenesBySymbol (String symbol)
    {
        return mouseGeneRepository.findBySymbolStartingWith(symbol);
    }

}
