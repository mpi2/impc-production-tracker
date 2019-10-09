package uk.ac.ebi.impc_prod_tracker.service.gene;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.GeneRepository;
import java.util.List;

@Component
public class GeneService
{
    private GeneRepository geneRepository;

    public GeneService(GeneRepository geneRepository)
    {
        this.geneRepository = geneRepository;
    }

    public List<Gene> getGenesBySymbol (String symbol)
    {
        return geneRepository.findBySymbolStartingWith(symbol);
    }
}
