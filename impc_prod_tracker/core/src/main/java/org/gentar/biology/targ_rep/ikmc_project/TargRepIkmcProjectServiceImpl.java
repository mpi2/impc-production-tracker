package org.gentar.biology.targ_rep.ikmc_project;

import org.gentar.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TargRepIkmcProjectServiceImpl implements TargRepIkmcProjectService {

    private final TargRepIkmcProjectRepository targRepIkmcProjectRepository;

    private static final String TARG_REP_IKMC_PROJECT_NOT_EXISTS_ERROR =
            "An IKMC project with the id [%s] does not exist.";

    public TargRepIkmcProjectServiceImpl(TargRepIkmcProjectRepository targRepIkmcProjectRepository) {
        this.targRepIkmcProjectRepository = targRepIkmcProjectRepository;
    }

    @Override
    public TargRepIkmcProject getNotNullTargRepIkmcProjectById(Long id) throws NotFoundException {
        TargRepIkmcProject targRepIkmcProject = targRepIkmcProjectRepository.findTargRepIkmcProjectById(id);
        if (targRepIkmcProject == null)
        {
            throw new NotFoundException(String.format(TARG_REP_IKMC_PROJECT_NOT_EXISTS_ERROR, id));
        }
        return targRepIkmcProject;
    }
}
