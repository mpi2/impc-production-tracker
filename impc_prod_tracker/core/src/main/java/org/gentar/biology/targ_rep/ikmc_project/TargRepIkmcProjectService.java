package org.gentar.biology.targ_rep.ikmc_project;

public interface TargRepIkmcProjectService {
    /**
     * Get a {@link TargRepIkmcProject} object identified by the given name.
     * @param id
     * @return TargRepIkmcProject identified by id. Null if not found.
     */
    TargRepIkmcProject getNotNullTargRepIkmcProjectById(Long id);
}
