package uk.ac.ebi.impc_prod_tracker.data.experiment.project_mouse_gene;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene;

import java.util.Collection;

public interface ProjectMouseGeneRepository extends CrudRepository<ProjectMouseGene, Long>
{
    Iterable<ProjectMouseGene> findAllByMouseGeneIn(Collection<IntendedMouseGene> mouseGeneList);
}
