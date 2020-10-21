package org.gentar.biology.gene;

import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.species.Species;
import org.gentar.biology.species.SpeciesNames;
import org.gentar.biology.species.SpeciesService;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class GeneServiceImpl implements GeneService
{
    private final GeneRepository geneRepository;
    private final GeneExternalService geneExternalService;
    private final SpeciesService speciesService;

    private static final String GENE_IS_SYNONYM_ERROR =
        "%s is a synonym for the gene %s (%s). Please use the valid symbol %s instead.";

    public GeneServiceImpl(
        GeneRepository geneRepository,
        GeneExternalService geneExternalService,
        SpeciesService speciesService)
    {
        this.geneRepository = geneRepository;
        this.geneExternalService = geneExternalService;
        this.speciesService = speciesService;
    }

    @Override
    public List<Gene> getGenesBySymbolStartingWith(String symbol)
    {
        return geneRepository.findBySymbolStartingWith(symbol);
    }

    @Override
    @Cacheable("geneByAccessionId")
    public Gene getGeneByAccessionId(String accessionId)
    {
        return geneRepository.findFirstByAccIdIgnoreCase(accessionId);
    }

    @Override
    @Cacheable("geneBySymbol")
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
    public Gene findAndCreateInLocalIfNeeded(String accessionIdOrSymbol, SpeciesNames speciesName)
    {
        Gene gene = findInLocal(accessionIdOrSymbol);
        if (gene == null)
        {
            validateSpecies(speciesName);
            gene = findInExternalReference(accessionIdOrSymbol);
            if (gene != null)
            {
                // Check that what we found is actually new. The gene could already exist in the
                // system if 'accessionIdOrSymbol' is actually a synonym.
                Gene alreadyExistingGene = getGeneByAccessionId(gene.getAccId());
                if (alreadyExistingGene == null)
                {
                    Species species = getByNameOrDefaultIfNull(speciesName);
                    gene.setSpecies(species);
                    create(gene);
                }
                else
                {
                    gene = alreadyExistingGene;
                }
            }
        }
        return gene;
    }

    // Temporal validation. Human not supported yet
    private void validateSpecies(SpeciesNames speciesName)
    {
        if (speciesName != null)
        {
            if (SpeciesNames.HUMAN.equals(speciesName))
            {
                throw new UserOperationFailedException("Human genes not supported yet.");
            }
        }
    }

    private Species getByNameOrDefaultIfNull(SpeciesNames speciesName)
    {
        Species species;
        if (speciesName == null)
        {
            species = speciesService.getSpeciesByName(SpeciesNames.MOUSE.getLabel());
        }
        else
        {
            species = speciesService.getSpeciesByName(speciesName.getLabel());
        }
        return species;
    }

    @Override
    public Gene findAndCreateInLocalIfNeededFailIfNull(String accessionIdOrSymbol, SpeciesNames speciesName)
    throws UserOperationFailedException
    {
        Gene gene = findAndCreateInLocalIfNeeded(accessionIdOrSymbol, speciesName);
        if (gene == null)
        {
            throw new NotFoundException(
                "Gene with accession id or symbol [" + accessionIdOrSymbol + "] does not exist.");
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
            Gene synonym = geneExternalService.getSynonymFromExternalGenes(accessionIdOrSymbol);
            if (synonym != null)
            {
                throw new UserOperationFailedException(
                    String.format(GENE_IS_SYNONYM_ERROR,
                        accessionIdOrSymbol, synonym.getSymbol(), synonym.getAccId(), synonym.getSymbol()));
            }
        }
        return gene;
    }

    private boolean isAccessionId(String accessionIdOrSymbol)
    {
        return accessionIdOrSymbol.contains(":");
    }
}
