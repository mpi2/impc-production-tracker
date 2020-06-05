package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class OutcomeResponseMapper implements Mapper<Outcome, OutcomeResponseDTO>
{
    private OutcomeCommonMapper outcomeCommonMapper;

    public OutcomeResponseMapper(OutcomeCommonMapper outcomeCommonMapper)
    {
        this.outcomeCommonMapper = outcomeCommonMapper;
    }

    @Override
    public OutcomeResponseDTO toDto(Outcome outcome)
    {
        OutcomeResponseDTO outcomeResponseDTO = new OutcomeResponseDTO();
        if (outcome != null)
        {
            OutcomeCommonDTO outcomeCommonDTO = outcomeCommonMapper.toDto(outcome);
            outcomeResponseDTO.setOutcomeCommonDTO(outcomeCommonDTO);
            outcomeResponseDTO.setId(outcome.getId());
            if (outcome.getPlan() != null)
            {
                outcomeResponseDTO.setPin(outcome.getPlan().getPin());
            }

            outcomeResponseDTO.setTpo(outcome.getTpo());
            if (outcome.getOutcomeType() != null)
            {
                outcomeResponseDTO.setOutcomeTypeName(outcome.getOutcomeType().getName());
            }
        }
        return outcomeResponseDTO;
    }

    @Override
    public Outcome toEntity(OutcomeResponseDTO outcomeResponseDTO)
    {
        // An Outcome response does not need to be converted to an entity.
        return null;
    }
}
