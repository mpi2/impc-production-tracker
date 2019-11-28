package org.gentar.organization.funder;

import org.gentar.Mapper;
import org.springframework.stereotype.Service;

@Service
public class FunderMapper implements Mapper<Funder, String>
{
    private FunderService funderService;

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
            funder = new Funder();
            funder.setName(name);

        }
        return funder;
    }
}
