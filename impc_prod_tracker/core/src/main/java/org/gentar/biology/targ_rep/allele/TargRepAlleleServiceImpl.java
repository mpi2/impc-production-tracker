package org.gentar.biology.targ_rep.allele;

import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TargRepAlleleServiceImpl implements TargRepAlleleService
{
    private final TargRepAlleleRepository alleleRepository;
    private static final String TARG_REP_ALLELE_NOT_EXISTS_ERROR =
            "A targ rep allele with the id [%s] does not exist.";

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

    @Override
    public TargRepAllele getNotNullTargRepAlelleById(Long id) throws NotFoundException {
        TargRepAllele targRepAllele = alleleRepository.findTargRepAlleleById(id);
        if (targRepAllele == null)
        {
            throw new NotFoundException(String.format(TARG_REP_ALLELE_NOT_EXISTS_ERROR, id));
        }
        return targRepAllele;
    }

    @Override
    public Page<TargRepAllele> getPageableTargRepAllele(Pageable page)
    {
        Page<TargRepAllele> targRepAlleles = alleleRepository.findAll(page);
        return targRepAlleles;
    }


}
