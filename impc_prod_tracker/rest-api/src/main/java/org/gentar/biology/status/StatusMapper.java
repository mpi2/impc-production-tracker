package org.gentar.biology.status;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper implements Mapper<Status, String>
{
    private StatusService statusService;

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
           status = new Status();
            status.setName(name);
        }
        return status;
    }
}
