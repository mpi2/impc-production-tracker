package org.gentar.biology.targ_rep.ikmc_project.status;

/**
 * TargRepIkmcProjectStatusService.
 */
public interface TargRepIkmcProjectStatusService {
    /**
     * Get a {@link TargRepIkmcProjectStatus} object identified by the given name.
     *
     * @return TTargRepIkmcProjectStatus identified by name. Null if not found.
     */
    TargRepIkmcProjectStatus getTargRepIkmcProjectStatusByName(String name);
}
