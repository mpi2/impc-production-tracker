package org.gentar.biology.targ_rep.ikmc_project;

import org.springframework.data.repository.CrudRepository;

/**
 * TargRepIkmcProjectRepository.
 */
public interface TargRepIkmcProjectRepository extends CrudRepository<TargRepIkmcProject, Long> {
    TargRepIkmcProject findTargRepIkmcProjectById(Long id);
}
