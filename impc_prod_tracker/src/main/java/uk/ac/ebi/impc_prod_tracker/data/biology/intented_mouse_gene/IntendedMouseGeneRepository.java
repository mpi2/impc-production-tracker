package uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface IntendedMouseGeneRepository extends CrudRepository<IntendedMouseGene, Long>
{
    Iterable<IntendedMouseGene> findAllBySymbolIn(Collection<String> symbols);
}
