package org.gentar.biology.targ_rep.gene;

import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

@Primary
public interface TargRepGeneRepository extends CrudRepository<TargRepGene, Long>
{
    TargRepGene findBySymbol(String symbol);
}
