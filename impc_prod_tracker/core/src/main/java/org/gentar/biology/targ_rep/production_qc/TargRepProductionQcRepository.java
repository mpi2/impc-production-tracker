package org.gentar.biology.targ_rep.production_qc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface TargRepProductionQcRepository extends CrudRepository<TargRepProductionQc, Long> {
       TargRepProductionQc findTargRepProductionQcByEsCellId(Long id);
}
