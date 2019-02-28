package uk.ac.ebi.impc_prod_tracker.data.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.entity.Plan;

public interface PlanRepository extends CrudRepository<Plan, Long> {
}
