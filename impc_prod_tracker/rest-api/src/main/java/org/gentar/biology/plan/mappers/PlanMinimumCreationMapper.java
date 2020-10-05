package org.gentar.biology.plan.mappers;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.PlanMinimumCreationDTO;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.springframework.stereotype.Component;

@Component
public class PlanMinimumCreationMapper implements Mapper<Plan, PlanMinimumCreationDTO>
{
    private final EntityMapper entity;
    private final PlanCommonDataMapper planCommonDataMapper;
    private final PlanTypeMapper planTypeMapper;
    private final AttemptTypeMapper attemptTypeMapper;

    public PlanMinimumCreationMapper(
        EntityMapper entity,
        PlanCommonDataMapper planCommonDataMapper,
        PlanTypeMapper planTypeMapper,
        AttemptTypeMapper attemptTypeMapper)
    {
        this.entity = entity;
        this.planCommonDataMapper = planCommonDataMapper;
        this.planTypeMapper = planTypeMapper;
        this.attemptTypeMapper = attemptTypeMapper;
    }

    @Override
    public PlanMinimumCreationDTO toDto(Plan plan)
    {
        PlanCommonDataDTO planCommonDataDTO = planCommonDataMapper.toDto(plan);
        PlanMinimumCreationDTO minimumPlanCreationDTO =
            entity.toTarget(planCommonDataDTO, PlanMinimumCreationDTO.class);
        minimumPlanCreationDTO.setPlanTypeName(plan.getPlanType().getName());
        setPlanTypeNameToDto(minimumPlanCreationDTO, plan);
        setAttemptTypeNameToDto(minimumPlanCreationDTO, plan);
        return minimumPlanCreationDTO;
    }

    private void setPlanTypeNameToDto(PlanMinimumCreationDTO minimumPlanCreationDTO, Plan plan)
    {
        minimumPlanCreationDTO.setPlanTypeName(planTypeMapper.toDto(plan.getPlanType()));
    }

    private void setAttemptTypeNameToDto(PlanMinimumCreationDTO minimumPlanCreationDTO, Plan plan)
    {
        minimumPlanCreationDTO.setAttemptTypeName(attemptTypeMapper.toDto(plan.getAttemptType()));
    }

    @Override
    public Plan toEntity(PlanMinimumCreationDTO minimumPlanCreationDTO)
    {
        Plan plan = planCommonDataMapper.toEntity(minimumPlanCreationDTO.getPlanCommonDataDTO());
        setPlanTypeToEntity(plan, minimumPlanCreationDTO);
        setAttemptTypeToEntity(plan, minimumPlanCreationDTO);
        return plan;
    }

    private void setPlanTypeToEntity(Plan plan, PlanMinimumCreationDTO minimumPlanCreationDTO)
    {
        String planTypeName = minimumPlanCreationDTO.getPlanTypeName();
        plan.setPlanType(planTypeMapper.toEntity(planTypeName));
    }

    private void setAttemptTypeToEntity(Plan plan, PlanMinimumCreationDTO minimumPlanCreationDTO)
    {
        plan.setAttemptType(attemptTypeMapper.toEntity(minimumPlanCreationDTO.getAttemptTypeName()));
    }
}
