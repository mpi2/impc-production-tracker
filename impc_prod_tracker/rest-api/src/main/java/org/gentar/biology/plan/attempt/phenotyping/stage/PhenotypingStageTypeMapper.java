package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageTypeMapper implements Mapper<PhenotypingStageType, String>
{
    private PhenotypingStageTypeService phenotypingStageTypeService;

    private static final String PHENOTYPING_STAGE_TYPE_NOT_FOUND_ERROR = "Phenotyping stage type '%s' does not exist.";

    public PhenotypingStageTypeMapper(PhenotypingStageTypeService phenotypingStageTypeService)
    {
        this.phenotypingStageTypeService = phenotypingStageTypeService;
    }

    @Override
    public String toDto(PhenotypingStageType entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    @Override
    public PhenotypingStageType toEntity(String name)
    {
        PhenotypingStageType phenotypingStageType = phenotypingStageTypeService.getPhenotypingStageTypeByName(name);

        if (phenotypingStageType == null)
        {
            throw new UserOperationFailedException(String.format(PHENOTYPING_STAGE_TYPE_NOT_FOUND_ERROR, phenotypingStageType));
        }

        return phenotypingStageType;
    }
}
