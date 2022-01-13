package org.gentar.biology.targ_rep.allele;

import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TargRepAlleleService
{
    List<TargRepAllele> getTargRepAllelesByGeneFailIfNull(TargRepGene gene);
    TargRepAllele getNotNullTargRepAlelleById(Long id);
    Page<TargRepAllele> getPageableTargRepAllele(Pageable page);
}
