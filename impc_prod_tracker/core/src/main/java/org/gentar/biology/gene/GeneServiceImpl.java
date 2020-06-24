package org.gentar.biology.gene;

import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class GeneServiceImpl implements GeneService
{
    private GeneRepository geneRepository;
    private GeneExternalService geneExternalService;

    public GeneServiceImpl(GeneRepository geneRepository, GeneExternalService geneExternalService)
    {
        this.geneRepository = geneRepository;
        this.geneExternalService = geneExternalService;
    }

    public List<Gene> getGenesBySymbolStartingWith(String symbol)
    {
        return geneRepository.findBySymbolStartingWith(symbol);
    }

    public Gene getGeneByAccessionId(String accessionId)
    {
        return geneRepository.findFirstByAccIdIgnoreCase(accessionId);
    }

    @Override
    public Gene getGeneBySymbol(String symbol)
    {
        return geneRepository.findBySymbol(symbol);
    }

    @Transactional
    @Override
    public Gene create(Gene gene)
    {
        return geneRepository.save(gene);
    }

    @Override
    public Gene findAndCreateInLocalIfNeeded(String accessionIdOrSymbol)
    {
        Gene gene = findInLocal(accessionIdOrSymbol);
        if (gene == null)
        {
            gene = findInExternalReference(accessionIdOrSymbol);
            if (gene != null)
            {
                create(gene);
            }
        }
        return gene;
    }

    @Override
    public Gene findAndCreateInLocalIfNeededFailIfNull(String accessionIdOrSymbol)
    throws UserOperationFailedException
    {
        Gene gene = findAndCreateInLocalIfNeeded(accessionIdOrSymbol);
        if (gene == null)
        {
            throw new UserOperationFailedException(
                "Gene with accession id or symbol [" + accessionIdOrSymbol + "] does not exist");
        }
        return gene;
    }

    private Gene findInLocal(String accessionIdOrSymbol)
    {
        Gene gene;
        if (isAccessionId(accessionIdOrSymbol))
        {
            gene = getGeneByAccessionId(accessionIdOrSymbol);
        }
        else
        {
            gene = getGeneBySymbol(accessionIdOrSymbol);
        }
        return gene;
    }

    private Gene findInExternalReference(String accessionIdOrSymbol)
    {
        Gene gene = geneExternalService.getGeneFromExternalDataBySymbolOrAccId(accessionIdOrSymbol);
        if (gene == null)
        {
            gene = geneExternalService.getSynonymFromExternalGenes(accessionIdOrSymbol);
        }
        return gene;
    }

    private boolean isAccessionId(String accessionIdOrSymbol)
    {
        return accessionIdOrSymbol.contains(":");
    }
}
