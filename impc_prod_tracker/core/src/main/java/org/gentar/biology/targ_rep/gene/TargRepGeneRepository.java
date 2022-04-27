package org.gentar.biology.targ_rep.gene;

import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

/**
 * TargRepGeneRepository.
 */
@Primary
public interface TargRepGeneRepository extends CrudRepository<TargRepGene, Long> {
    TargRepGene findBySymbol(String symbol);
}
