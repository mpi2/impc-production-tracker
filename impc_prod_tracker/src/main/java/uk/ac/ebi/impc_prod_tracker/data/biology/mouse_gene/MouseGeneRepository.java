package uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MouseGeneRepository extends CrudRepository<MouseGene, Long> {
//    Set<MouseGene> findMouseGeneBySymbol(String symbol);

    List<MouseGene> findBySymbolStartingWith(String symbol);

}
