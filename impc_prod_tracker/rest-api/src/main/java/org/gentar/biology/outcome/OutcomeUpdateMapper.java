package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class OutcomeUpdateMapper implements Mapper<Outcome, OutcomeUpdateDTO>
{
    private final OutcomeCommonMapper outcomeCommonMapper;

    public OutcomeUpdateMapper(OutcomeCommonMapper outcomeCommonMapper)
    {
        this.outcomeCommonMapper = outcomeCommonMapper;
    }

    @Override
    public OutcomeUpdateDTO toDto(Outcome outcome)
    {
        OutcomeUpdateDTO outcomeUpdateDTO = new OutcomeUpdateDTO();
        outcomeUpdateDTO.setOutcomeCommonDTO(outcomeCommonMapper.toDto(outcome));
        return outcomeUpdateDTO;
    }

    @Override
    public Outcome toEntity(OutcomeUpdateDTO outcomeUpdateDTO)
    {
        return outcomeCommonMapper.toEntity(outcomeUpdateDTO.getOutcomeCommonDTO());
    }
}
