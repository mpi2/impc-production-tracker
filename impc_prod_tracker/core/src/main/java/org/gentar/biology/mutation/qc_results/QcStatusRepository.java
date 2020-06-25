package org.gentar.biology.mutation.qc_results;

import org.springframework.data.repository.CrudRepository;

public interface QcStatusRepository extends CrudRepository<QcStatus, Long>
{
    QcStatus findByNameIgnoreCase(String name);
}
