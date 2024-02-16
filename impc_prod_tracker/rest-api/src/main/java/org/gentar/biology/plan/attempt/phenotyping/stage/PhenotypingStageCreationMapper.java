package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class PhenotypingStageCreationMapper implements Mapper<PhenotypingStage, PhenotypingStageCreationDTO>
{
    private final PhenotypingStageCommonMapper phenotypingStageCommonMapper;
    private final PhenotypingStageService phenotypingStageService;

    public PhenotypingStageCreationMapper(PhenotypingStageCommonMapper phenotypingStageCommonMapper,
                                          PhenotypingStageService phenotypingStageService) {
        this.phenotypingStageCommonMapper = phenotypingStageCommonMapper;
        this.phenotypingStageService = phenotypingStageService;
    }

    @Override
    public PhenotypingStageCreationDTO toDto(PhenotypingStage entity) {
        return null;
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageCreationDTO dto)
    {
        PhenotypingStage phenotypingStage = phenotypingStageCommonMapper.toEntity(dto.getPhenotypingStageCommonDTO());
        if (phenotypingStage == null)
        {
            phenotypingStage = new PhenotypingStage();
        }
        String phenotypingStageTypeName = dto.getPhenotypingTypeName();
        PhenotypingStageType phenotypingStageType = phenotypingStageService
                .getPhenotypingStageTypeByNameFailingWhenNull(phenotypingStageTypeName);
        phenotypingStage.setPhenotypingStageType(phenotypingStageType);
        return phenotypingStage;
    }

    @Override
    public Set<PhenotypingStage> toEntities(Collection<PhenotypingStageCreationDTO> dtos)
    {
        Set<PhenotypingStage> phenotypingStages = new HashSet<>();
        if (dtos != null)
        {
            dtos.forEach(dto -> phenotypingStages.add(toEntity(dto)));
        }
        return phenotypingStages;
    }
}
