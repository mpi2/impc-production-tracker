package org.gentar.biology.targ_rep.allele;

import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@Primary
public interface TargRepAlleleRepository extends PagingAndSortingRepository<TargRepAllele, Long>
{
    List<TargRepAllele> findByGene(TargRepGene gene);
    TargRepAllele findTargRepAlleleById(Long id);
    List<TargRepAllele> findAll();

}
