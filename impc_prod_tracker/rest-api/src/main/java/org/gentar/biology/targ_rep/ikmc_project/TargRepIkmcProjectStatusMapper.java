package org.gentar.biology.targ_rep.ikmc_project;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.ikmc_project.status.TargRepIkmcProjectStatus;
import org.gentar.biology.targ_rep.ikmc_project.status.TargRepIkmcProjectStatusService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class TargRepIkmcProjectStatusMapper implements Mapper<TargRepIkmcProjectStatus, TargRepIkmcProjectStatusDTO>
{
    private final TargRepIkmcProjectStatusService targRepIkmcProjectStatusService;
    private static final String TARG_REP_IKMC_PROJECT_STATUS_NOT_FOUND_ERROR
            = "The targ_rep_ikmc_project_status '%s' does not exist.";

    public TargRepIkmcProjectStatusMapper(TargRepIkmcProjectStatusService targRepIkmcProjectStatusService) {
        this.targRepIkmcProjectStatusService = targRepIkmcProjectStatusService;
    }

    @Override
    public TargRepIkmcProjectStatusDTO toDto(TargRepIkmcProjectStatus entity)
    {
        TargRepIkmcProjectStatusDTO targRepIkmcProjectStatusDTO = new TargRepIkmcProjectStatusDTO();
        if (entity != null) {
            targRepIkmcProjectStatusDTO.setName(entity.getName());
        }
        return targRepIkmcProjectStatusDTO;
    }

    @Override
    public TargRepIkmcProjectStatus toEntity(TargRepIkmcProjectStatusDTO dto)
    {
        String name = dto.getName();
        TargRepIkmcProjectStatus targRepIkmcProjectStatus =
                targRepIkmcProjectStatusService.getTargRepIkmcProjectStatusByName(name);
        if (targRepIkmcProjectStatus == null)
        {
            throw new UserOperationFailedException(String.format(TARG_REP_IKMC_PROJECT_STATUS_NOT_FOUND_ERROR, name));
        }
        return targRepIkmcProjectStatus;
    }

}
