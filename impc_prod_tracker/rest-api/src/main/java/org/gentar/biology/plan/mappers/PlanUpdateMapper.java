package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanUpdateDTO;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.organization.funder.FunderService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PlanUpdateMapper implements Mapper<Plan, PlanUpdateDTO>
{
    private final PlanBasicDataMapper planBasicDataMapper;

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
