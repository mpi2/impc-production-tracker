package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageCreationMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PhenotypingAttemptCreationMapper implements Mapper<PhenotypingAttempt, PhenotypingAttemptCreationDTO>
{
    private final PhenotypingStageCreationMapper phenotypingStageCreationMapper;
    private final PhenotypingAttemptCommonMapper phenotypingAttemptCommonMapper;

    public PhenotypingAttemptCreationMapper(PhenotypingStageCreationMapper phenotypingStageCreationMapper,
                                            PhenotypingAttemptCommonMapper phenotypingAttemptCommonMapper)
    {
        this.phenotypingStageCreationMapper = phenotypingStageCreationMapper;
        this.phenotypingAttemptCommonMapper = phenotypingAttemptCommonMapper;
    }

    @Override
    public PhenotypingAttemptCreationDTO toDto(PhenotypingAttempt entity)
    {
//      No needed
        return null;
    }

    @Override
    public PhenotypingAttempt toEntity(PhenotypingAttemptCreationDTO dto)
    {
        PhenotypingAttempt phenotypingAttempt = phenotypingAttemptCommonMapper.toEntity(dto.getPhenotypingAttemptCommonDTO());
        setPhenotypingStages(phenotypingAttempt, dto);
        return phenotypingAttempt;
    }

    private void setPhenotypingStages(PhenotypingAttempt phenotypingAttempt, PhenotypingAttemptCreationDTO dto)
    {
        if (dto.getPhenotypingStageCreationDTOS() != null)
        {
            Set<PhenotypingStage> phenotypingStages = phenotypingStageCreationMapper.toEntities(
                    dto.getPhenotypingStageCreationDTOS());
            phenotypingStages.forEach(phenotypingStage -> phenotypingStage.setPhenotypingAttempt(phenotypingAttempt));
            phenotypingAttempt.setPhenotypingStages(phenotypingStages);
        }
    }
}
