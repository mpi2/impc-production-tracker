package org.gentar.biology.status;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper implements Mapper<Status, String>
{
    private StatusService statusService;

    private static final String STATUS_NOT_FOUND_ERROR = "Status '%s' does not exist.";

    public StatusMapper(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public String toDto(Status entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    @Override
    public Status toEntity(String name)
    {
        Status status = statusService.getStatusByName(name);
        if (status == null)
        {
            throw new UserOperationFailedException(String.format(STATUS_NOT_FOUND_ERROR, status));
        }
        return status;
    }
}
