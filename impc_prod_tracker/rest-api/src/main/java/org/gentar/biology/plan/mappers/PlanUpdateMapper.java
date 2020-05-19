package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.PlanUpdateDTO;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

@Component
public class PlanUpdateMapper implements Mapper<Plan, PlanUpdateDTO>
{
    private PlanBasicDataMapper planBasicDataMapper;
    private PlanService planService;

    public PlanUpdateMapper(PlanBasicDataMapper planBasicDataMapper, PlanService planService)
    {
        this.planBasicDataMapper = planBasicDataMapper;
        this.planService = planService;
    }

    @Override
    public PlanUpdateDTO toDto(Plan plan)
    {
        PlanUpdateDTO planUpdateDTO = new PlanUpdateDTO();
        planUpdateDTO.setPin(plan.getPin());
        if (plan.getProject() != null)
        {
            planUpdateDTO.setTpn(plan.getProject().getTpn());
        }
        PlanBasicDataDTO planBasicDataDTO = planBasicDataMapper.toDto(plan);
        planUpdateDTO.setPlanBasicDataDTO(planBasicDataDTO);
        return planUpdateDTO;
    }

    @Override
    public Plan toEntity(PlanUpdateDTO planUpdateDTO)
    {
        Plan plan = planBasicDataMapper.toEntity(planUpdateDTO.getPlanBasicDataDTO());
        setEvent(plan, planUpdateDTO);
        return plan;
    }

    private void setEvent(Plan plan, PlanUpdateDTO planUpdateDTO)
    {
        String action = planUpdateDTO.getStatusTransitionDTO().getActionToExecute();
        ProcessEvent processEvent = planService.getProcessEventByName(plan, action);
        plan.setEvent(processEvent);
    }
}
