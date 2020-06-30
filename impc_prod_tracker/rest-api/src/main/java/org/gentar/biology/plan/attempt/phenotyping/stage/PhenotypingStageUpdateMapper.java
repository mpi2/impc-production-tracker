package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.Mapper;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageUpdateMapper implements Mapper<PhenotypingStage, PhenotypingStageUpdateDTO>
{
    private PhenotypingStageCommonMapper phenotypingStageCommonMapper;
    private PhenotypingStageService phenotypingStageService;

    public PhenotypingStageUpdateMapper(PhenotypingStageCommonMapper phenotypingStageCommonMapper,
                                        PhenotypingStageService phenotypingStageService)
    {
        this.phenotypingStageCommonMapper = phenotypingStageCommonMapper;
        this.phenotypingStageService = phenotypingStageService;
    }

    @Override
    public PhenotypingStageUpdateDTO toDto(PhenotypingStage entity)
    {
        // No needed
        return null;
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageUpdateDTO dto)
    {
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        if (dto.getPhenotypingStageCommonDTO() != null)
        {
            phenotypingStage = phenotypingStageCommonMapper.toEntity(dto.getPhenotypingStageCommonDTO());
        }
        phenotypingStage.setId(dto.getId());
        phenotypingStage.setPsn(dto.getPsn());
        setEvent(phenotypingStage, dto);
        return phenotypingStage;
    }

    private void setEvent(PhenotypingStage phenotypingStage, PhenotypingStageUpdateDTO dto)
    {
        ProcessEvent processEvent = null;
        StatusTransitionDTO statusTransitionDTO = dto.getStatusTransitionDTO();
        if (statusTransitionDTO != null)
        {
            String action = statusTransitionDTO.getActionToExecute();
            processEvent = phenotypingStageService.getProcessEventByName(phenotypingStage, action);
        }
        phenotypingStage.setEvent(processEvent);
    }
}
