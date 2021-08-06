package org.gentar.biology.targ_rep.allele;

import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TargRepAlleleServiceImpl implements TargRepAlleleService
{
    private final TargRepAlleleRepository alleleRepository;

    public TargRepAlleleServiceImpl(TargRepAlleleRepository alleleRepository) {
        this.alleleRepository = alleleRepository;
    }

    @Override
    public List<TargRepAllele> getTargRepAllelesByGeneFailIfNull(TargRepGene gene) throws UserOperationFailedException
    {
        List<TargRepAllele> alleles = alleleRepository.findByGene(gene);
        if (alleles.isEmpty())
        {
            throw new NotFoundException(
                    "There are not ES Cells available for [" + gene.getSymbol() + "] marker_symbol does not exist.");
        }
        return alleles;
    }
}
