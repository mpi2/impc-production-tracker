package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.statemachine.TransitionMapper;

public class PhenotypingStageCreationMapper implements Mapper<PhenotypingStage, PhenotypingStageCreationDTO>
{

    private PhenotypingStageTypeMapper phenotypeStageTypeMapper;
    private PhenotypingStageService phenotypingStageService;
    private TransitionMapper transitionMapper;
    private PhenotypingStageCommonMapper phenotypingStageCommonMapper;

    public PhenotypingStageCreationMapper(PhenotypingStageTypeMapper phenotypeStageTypeMapper,
                                          PhenotypingStageService phenotypingStageService,
                                          TransitionMapper transitionMapper,
                                          PhenotypingStageCommonMapper phenotypingStageCommonMapper) {
        this.phenotypeStageTypeMapper = phenotypeStageTypeMapper;
        this.phenotypingStageService = phenotypingStageService;
        this.transitionMapper = transitionMapper;
        this.phenotypingStageCommonMapper = phenotypingStageCommonMapper;
    }

    @Override
    public PhenotypingStageCreationDTO toDto(PhenotypingStage entity) {
        // Phenotyping Stage Creation information is always a dto send by the user. We don't need this
        // conversion here.
        return null;
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageCreationDTO dto) {
        PhenotypingStage phenotypingStage = phenotypingStageCommonMapper.toEntity(dto.getPhenotypingStageCommonDTO());
        phenotypingStage.setPhenotypingStageType(phenotypeStageTypeMapper.toEntity(dto.getPhenotypingTypeName()));
        return phenotypingStage;
    }
}
