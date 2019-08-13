package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.GeneRepository;

import java.util.List;

@Component
public class MouseGeneService {
    private GeneRepository geneRepository;

    public MouseGeneService(GeneRepository geneRepository) { this.geneRepository = geneRepository; }

    public List<Gene> getMouseGenesBySymbol (String symbol)
    {
        return geneRepository.findBySymbolStartingWith(symbol);
    }

    public Gene getMouseGenesByMgiId (String mgi_id)
    {
        return geneRepository.findByMgiId(mgi_id);
    }

}
