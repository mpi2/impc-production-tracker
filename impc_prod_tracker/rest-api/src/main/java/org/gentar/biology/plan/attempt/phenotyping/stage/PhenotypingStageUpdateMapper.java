package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageUpdateMapper implements Mapper<PhenotypingStage, PhenotypingStageUpdateDTO>
{
    @Override
    public PhenotypingStageUpdateDTO toDto(PhenotypingStage entity) {
        return null;
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageUpdateDTO dto) {
        return null;
    }
}
