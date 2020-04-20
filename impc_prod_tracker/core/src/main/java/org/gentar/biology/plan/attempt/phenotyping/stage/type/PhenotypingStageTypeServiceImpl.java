package org.gentar.biology.plan.attempt.phenotyping.stage.type;

import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageTypeServiceImpl implements PhenotypingStageTypeService
{
    private PhenotypingStageTypeRepository phenotypingStageTypeRepository;

    public PhenotypingStageTypeServiceImpl(PhenotypingStageTypeRepository phenotypingStageTypeRepository)
    {
        this.phenotypingStageTypeRepository = phenotypingStageTypeRepository;
    }

    public PhenotypingStageType getPhenotypingStageTypeByName(String name)
    {
        return phenotypingStageTypeRepository.findByNameIgnoreCase(name);
    }
}
