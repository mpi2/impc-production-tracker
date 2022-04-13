package org.gentar.biology.targ_rep.ikmc_project;

/**
 * TargRepIkmcProjectService.
 */
public interface TargRepIkmcProjectService {
    /**
     * Get a {@link TargRepIkmcProject} object identified by the given name.
     *
     * @return TargRepIkmcProject identified by id. Null if not found.
     */
    TargRepIkmcProject getNotNullTargRepIkmcProjectById(Long id);
}
