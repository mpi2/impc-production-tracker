package uk.ac.ebi.impc_prod_tracker.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import uk.ac.ebi.impc_prod_tracker.data.entity.Plan;

public interface PlanRepository extends CrudRepository<Plan, Long> {

    @Override
    @PreAuthorize("hasPermission(null, 'READ_PLAN')")
    @PostFilter("hasPermission(filterObject, 'FILTER_PLAN')")
    Iterable<Plan> findAll();
}
