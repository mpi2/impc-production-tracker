package org.gentar.biology.outcome;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyMapper;
import org.gentar.biology.mutation.MutationMapper;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.specimen.SpecimenMapper;
import org.springframework.stereotype.Component;

@Component
public class OutcomeMapper implements Mapper<Outcome, OutcomeDTO>
{
    private EntityMapper entityMapper;
    private ColonyMapper colonyMapper;
    private SpecimenMapper specimenMapper;
    private MutationMapper mutationMapper;
    private OutcomeService outcomeService;
    private PlanService planService;

    public OutcomeMapper(
        EntityMapper entityMapper,
        ColonyMapper colonyMapper,
        SpecimenMapper specimenMapper,
        MutationMapper mutationMapper,
        OutcomeService outcomeService,
        PlanService planService)
    {
        this.entityMapper = entityMapper;
        this.colonyMapper = colonyMapper;
        this.specimenMapper = specimenMapper;
        this.mutationMapper = mutationMapper;
        this.outcomeService = outcomeService;
        this.planService = planService;
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
        outcomeDTO.setColonyDTO(colonyMapper.toDto(outcome.getColony()));
        outcomeDTO.setSpecimenDTO(specimenMapper.toDto(outcome.getSpecimen()));
        outcomeDTO.setMutationDTOS(mutationMapper.toDtos(outcome.getMutations()));
    }

    @Override
    public Outcome toEntity(OutcomeDTO dto)
    {
        Outcome outcome = entityMapper.toTarget(dto, Outcome.class);
        Colony colony = colonyMapper.toEntity(dto.getColonyDTO());
        colony.setOutcome(outcome);
        outcome.setColony(colony);
        outcome.setPlan(planService.getNotNullPlanByPin(dto.getPin()));
        OutcomeType outcomeType =
            outcomeService.getOutcomeTypeByNameFailingWhenNull(dto.getOutcomeTypeName());
        outcome.setOutcomeType(outcomeType);
        return outcome;
    }
}
