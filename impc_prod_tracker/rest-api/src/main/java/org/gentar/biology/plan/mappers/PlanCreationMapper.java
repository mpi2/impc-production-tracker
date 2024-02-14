package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanCreationDTO;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.springframework.stereotype.Component;

@Component
public class PlanCreationMapper implements Mapper<Plan, PlanCreationDTO>
{
    private final PlanBasicDataMapper planBasicDataMapper;
    private final PlanTypeMapper planTypeMapper;
    private final AttemptTypeMapper attemptTypeMapper;

    public PlanCreationMapper(
        PlanBasicDataMapper planBasicDataMapper,
        PlanTypeMapper planTypeMapper,
        AttemptTypeMapper attemptTypeMapper)
    {
        this.planBasicDataMapper = planBasicDataMapper;
        this.planTypeMapper = planTypeMapper;
        this.attemptTypeMapper = attemptTypeMapper;
    }

    @Override
    public PlanCreationDTO toDto(Plan plan)
    {
        PlanBasicDataDTO planBasicDataDTO = planBasicDataMapper.toDto(plan);
        PlanCreationDTO planCreationDTO = new PlanCreationDTO();
        planCreationDTO.setPlanBasicDataDTO(planBasicDataDTO);
        if (plan.getProject() != null)
        {
            planCreationDTO.setTpn(plan.getProject().getTpn());
        }
        setPlanTypeNameToDto(planCreationDTO, plan);
        setAttemptTypeNameToDto(planCreationDTO, plan);
        return planCreationDTO;
    }

    private void setPlanTypeNameToDto(PlanCreationDTO planCreationDTO, Plan plan)
    {
        planCreationDTO.setPlanTypeName(planTypeMapper.toDto(plan.getPlanType()));
    }

    private void setAttemptTypeNameToDto(PlanCreationDTO planCreationDTO, Plan plan)
    {
        planCreationDTO.setAttemptTypeName(attemptTypeMapper.toDto(plan.getAttemptType()));
    }

    @Override
    public Plan toEntity(PlanCreationDTO planCreationDTO)
    {
        Plan plan = planBasicDataMapper.toEntity(planCreationDTO.getPlanBasicDataDTO());
        setPlanTypeToEntity(plan, planCreationDTO);
        setAttemptTypeToEntity(plan, planCreationDTO);
        return plan;
    }

    private void setPlanTypeToEntity(Plan plan, PlanCreationDTO planCreationDTO)
    {
        plan.setPlanType(planTypeMapper.toEntity(planCreationDTO.getPlanTypeName()));
    }

    private void setAttemptTypeToEntity(Plan plan, PlanCreationDTO planCreationDTO)
    {
        plan.setAttemptType(attemptTypeMapper.toEntity(planCreationDTO.getAttemptTypeName()));
    }
}
