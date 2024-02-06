package org.gentar.organization.funder;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Service;

@Service
public class FunderMapper implements Mapper<Funder, String>
{
    private final FunderService funderService;

    private static final String FUNDER_NOT_FOUND_ERROR = "Funder '%s' does not exist.";

    public FunderMapper(FunderService funderService)
    {
        this.funderService = funderService;
    }

    @Override
    public String toDto(Funder entity)
    {
        return entity.getName();
    }

    @Override
    public Funder toEntity(String name)
    {
        Funder funder = funderService.getFunderByName(name);
        if (funder == null)
        {
            throw new UserOperationFailedException(FUNDER_NOT_FOUND_ERROR);
        }
        return funder;
    }
}
