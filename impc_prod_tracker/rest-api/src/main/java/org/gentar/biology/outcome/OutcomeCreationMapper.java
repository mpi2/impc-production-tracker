package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.gentar.biology.outcome.type.OutcomeType;
import org.springframework.stereotype.Component;

@Component
public class OutcomeCreationMapper implements Mapper<Outcome, OutcomeCreationDTO>
{
    private OutcomeCommonMapper outcomeCommonMapper;
    private OutcomeService outcomeService;

    public OutcomeCreationMapper(OutcomeCommonMapper outcomeCommonMapper, OutcomeService outcomeService)
    {
        this.outcomeCommonMapper = outcomeCommonMapper;
        this.outcomeService = outcomeService;
    }

    @Override
    public OutcomeCreationDTO toDto(Outcome outcome)
    {
        OutcomeCreationDTO outcomeCreationDTO = new OutcomeCreationDTO();
        outcomeCreationDTO.setOutcomeCommonDTO(outcomeCommonMapper.toDto(outcome));
        if (outcome.getOutcomeType() != null)
        {
            outcomeCreationDTO.setOutcomeTypeName(outcome.getOutcomeType().getName());
        }
        return outcomeCreationDTO;
    }

    @Override
    public Outcome toEntity(OutcomeCreationDTO outcomeCreationDTO)
    {
        Outcome outcome = outcomeCommonMapper.toEntity(outcomeCreationDTO.getOutcomeCommonDTO());
        if (outcome == null)
        {
            outcome = new Outcome();
        }
        String outcomeTypeName = outcomeCreationDTO.getOutcomeTypeName();
        OutcomeType outcomeType = outcomeService.getOutcomeTypeByNameFailingWhenNull(outcomeTypeName);
        outcome.setOutcomeType(outcomeType);
        return outcome;
    }
}
