package uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History, Long>
{
    List<History> findAllByPlanIdOrderByDate(Long planId);
}
