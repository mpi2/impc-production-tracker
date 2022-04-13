package org.gentar.biology.targ_rep.allele;

import java.util.List;
import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * TargRepAlleleRepository.
 */
@Primary
public interface TargRepAlleleRepository extends PagingAndSortingRepository<TargRepAllele, Long> {
    List<TargRepAllele> findByGene(TargRepGene gene);

    TargRepAllele findTargRepAlleleById(Long id);

    List<TargRepAllele> findAll();

}
