package org.gentar.biology.outcome;

import org.gentar.Mapper;
import org.gentar.biology.colony.Colony;
import org.springframework.stereotype.Component;

@Component
public class OutcomeSummaryMapper implements Mapper<Outcome, OutcomeSummaryDTO>
{
    @Override
    public OutcomeSummaryDTO toDto(Outcome outcome)
    {
        OutcomeSummaryDTO outcomeSummaryDTO = new OutcomeSummaryDTO();
        if (outcome != null)
        {
            outcomeSummaryDTO.setTpo(outcome.getTpo());
            Colony colony = outcome.getColony();
            if (colony != null)
            {
                outcomeSummaryDTO.setExternalReference(colony.getName());
            }
        }
        return outcomeSummaryDTO;
    }

    @Override
    public Outcome toEntity(OutcomeSummaryDTO dto)
    {
        return null;
    }
}
