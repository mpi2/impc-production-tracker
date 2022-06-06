package org.gentar.biology.targ_rep.ikmc_project.status;

import org.springframework.stereotype.Component;

/**
 * TargRepIkmcProjectStatusServiceImpl.
 */
@Component
public class TargRepIkmcProjectStatusServiceImpl implements TargRepIkmcProjectStatusService {

    private final TargRepIkmcProjectStatusRepository targRepIkmcProjectStatusRepository;

    public TargRepIkmcProjectStatusServiceImpl(
        TargRepIkmcProjectStatusRepository targRepIkmcProjectStatusRepository) {
        this.targRepIkmcProjectStatusRepository = targRepIkmcProjectStatusRepository;
    }

    @Override
    public TargRepIkmcProjectStatus getTargRepIkmcProjectStatusByName(String name) {
        return targRepIkmcProjectStatusRepository
            .findTargRepIkmcProjectStatusByNameIgnoreCase(name);
    }
}
