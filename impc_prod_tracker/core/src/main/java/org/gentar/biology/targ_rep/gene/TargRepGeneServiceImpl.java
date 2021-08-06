package org.gentar.biology.targ_rep.gene;

import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TargRepGeneServiceImpl implements TargRepGeneService
{
    private final TargRepGeneRepository geneRepository;

    public TargRepGeneServiceImpl(TargRepGeneRepository geneRepository)
    {
        this.geneRepository = geneRepository;
    }

    @Override
    @Cacheable("geneBySymbol")
    public TargRepGene getGeneBySymbolFailIfNull(String symbol) throws UserOperationFailedException
    {
        TargRepGene gene = geneRepository.findBySymbol(symbol);
        if (gene == null)
        {
            throw new NotFoundException(
                    "Gene with accession id or symbol [" + symbol + "] does not exist.");
        }
        return gene;
    }
}
