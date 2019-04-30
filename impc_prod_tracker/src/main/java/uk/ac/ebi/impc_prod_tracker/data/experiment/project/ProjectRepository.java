package uk.ac.ebi.impc_prod_tracker.data.experiment.project;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project_priority.ProjectPriority;

public interface ProjectRepository extends CrudRepository<ProjectPriority, Long>
{
}
