package org.gentar.biology.plan.mappers;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanCreationDTO;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.springframework.stereotype.Component;

@Component
public class PlanCreationMapper implements Mapper<Plan, PlanCreationDTO>
{
    private EntityMapper entity;
    private PlanBasicDataMapper planBasicDataMapper;
    private PlanTypeMapper planTypeMapper;
    private AttemptTypeMapper attemptTypeMapper;

    public PlanCreationMapper(
        EntityMapper entity,
        PlanBasicDataMapper planBasicDataMapper,
        PlanTypeMapper planTypeMapper,
        AttemptTypeMapper attemptTypeMapper)
    {
        this.entity = entity;
        this.planBasicDataMapper = planBasicDataMapper;
        this.planTypeMapper = planTypeMapper;
        this.attemptTypeMapper = attemptTypeMapper;
    }

    @Override
    public PlanCreationDTO toDto(Plan plan)
    {
        PlanBasicDataDTO planBasicDataDTO = planBasicDataMapper.toDto(plan);
        PlanCreationDTO planCreationDTO = entity.toTarget(planBasicDataDTO, PlanCreationDTO.class);
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
