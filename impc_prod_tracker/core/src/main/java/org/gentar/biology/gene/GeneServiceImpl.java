package org.gentar.biology.gene;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneServiceImpl implements GeneService
{
    private GeneRepository geneRepository;

    public GeneServiceImpl(GeneRepository geneRepository)
    {
        this.geneRepository = geneRepository;
    }

    public List<Gene> getGenesBySymbol(String symbol)
    {
        return geneRepository.findBySymbolStartingWith(symbol);
    }

    public Gene getGeneByAccessionId(String accessionId)
    {
        return geneRepository.findFirstByAccIdIgnoreCase(accessionId);
    }
}
