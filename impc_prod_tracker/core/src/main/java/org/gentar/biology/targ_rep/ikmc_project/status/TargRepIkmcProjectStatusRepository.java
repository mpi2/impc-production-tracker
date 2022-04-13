package org.gentar.biology.targ_rep.ikmc_project.status;

import org.springframework.data.repository.CrudRepository;

/**
 * TargRepIkmcProjectStatusRepository.
 */
public interface TargRepIkmcProjectStatusRepository
    extends CrudRepository<TargRepIkmcProjectStatus, Long> {
    TargRepIkmcProjectStatus findTargRepIkmcProjectStatusByNameIgnoreCase(String name);
}
