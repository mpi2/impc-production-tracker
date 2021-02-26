package org.gentar.biology.plan.starting_point;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlanStartingPointRepository extends CrudRepository<PlanStartingPoint, Long>
{
    List<PlanStartingPoint> findByOutcomeId(Long outcome_id);
}
