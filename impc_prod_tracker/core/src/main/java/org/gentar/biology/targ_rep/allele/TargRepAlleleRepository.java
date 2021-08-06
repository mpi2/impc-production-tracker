package org.gentar.biology.targ_rep.allele;

import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Primary
public interface TargRepAlleleRepository extends CrudRepository<TargRepAllele, Long>
{
    List<TargRepAllele> findByGene(TargRepGene gene);
}
