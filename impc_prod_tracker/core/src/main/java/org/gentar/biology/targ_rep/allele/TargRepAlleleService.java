package org.gentar.biology.targ_rep.allele;

import java.util.List;
import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



/**
 * TargRepAlleleService.
 */
public interface TargRepAlleleService {
    List<TargRepAllele> getTargRepAllelesByGeneFailIfNull(TargRepGene gene);

    TargRepAllele getNotNullTargRepAlelleById(Long id);

    Page<TargRepAllele> getPageableTargRepAllele(Pageable page);
}
