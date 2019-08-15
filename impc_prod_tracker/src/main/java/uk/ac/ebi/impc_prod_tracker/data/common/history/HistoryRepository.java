package uk.ac.ebi.impc_prod_tracker.data.common.history;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History, Long>
{
    List<History> findAllByEntityNameAndEntityIdOrderByDate(String entityName, Long entityId);
}
