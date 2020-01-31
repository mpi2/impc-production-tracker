package org.gentar.biology.outcome;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.springframework.stereotype.Component;

@Component
public class OutcomeMapper implements Mapper<Outcome, OutcomeDTO>
{
    private EntityMapper entityMapper;

    public OutcomeMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public OutcomeDTO toDto(Outcome entity)
    {
        OutcomeDTO outcomeDTO = entityMapper.toTarget(entity, OutcomeDTO.class);
        addAdditionalFields(outcomeDTO, entity);
        return outcomeDTO;
    }

    private void addAdditionalFields(OutcomeDTO outcomeDTO, Outcome outcome)
    {
        Plan plan = outcome.getPlan();
        if (plan != null)
        {
            outcomeDTO.setPin(plan.getPin());
        }
    }

    @Override
    public Outcome toEntity(OutcomeDTO dto)
    {
        return null;
    }
}
