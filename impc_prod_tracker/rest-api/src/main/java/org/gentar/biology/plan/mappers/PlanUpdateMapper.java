package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class PlanUpdateMapper implements Mapper<Plan, PlanUpdateDTO>
{
    private PlanBasicDataMapper planBasicDataMapper;

    public PlanUpdateMapper(PlanBasicDataMapper planBasicDataMapper)
    {
        this.planBasicDataMapper = planBasicDataMapper;
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
        Plan plan = new Plan();
        PlanBasicDataDTO planBasicDataDTO = planUpdateDTO.getPlanBasicDataDTO();
        if (planBasicDataDTO != null)
        {
            planBasicDataDTO.setId(planUpdateDTO.getId());
            plan = planBasicDataMapper.toEntity(planBasicDataDTO);
        }
        plan.setId(planUpdateDTO.getId());
        return plan;
    }
}
