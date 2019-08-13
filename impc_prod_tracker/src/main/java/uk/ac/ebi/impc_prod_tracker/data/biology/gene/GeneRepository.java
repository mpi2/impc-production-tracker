package uk.ac.ebi.impc_prod_tracker.data.biology.gene;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeneRepository extends CrudRepository<Gene, Long> {
//    Set<Gene> findMouseGeneBySymbol(String symbol);

    List<Gene> findBySymbolStartingWith(String symbol);

    Gene findByMgiId(String mgi_id);

}
