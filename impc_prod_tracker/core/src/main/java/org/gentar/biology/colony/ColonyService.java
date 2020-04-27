package org.gentar.biology.colony;

import org.springframework.stereotype.Component;

@Component
public class ColonyService
{
    private ColonyRepository colonyRepository;

    public ColonyService(ColonyRepository colonyRepository)
    {
        this.colonyRepository = colonyRepository;
    }

    public Colony getColonyByName(String colonyName)
    {
        return colonyRepository.findByName(colonyName);
    }
}
