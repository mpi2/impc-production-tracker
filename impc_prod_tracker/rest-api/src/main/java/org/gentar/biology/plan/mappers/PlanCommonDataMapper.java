package org.gentar.biology.plan.mappers;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.organization.work_group.WorkGroupMapper;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class PlanCommonDataMapper implements Mapper<Plan, PlanCommonDataDTO>
{
    private EntityMapper entityMapper;
    private FunderMapper funderMapper;
    private WorkUnitMapper workUnitMapper;
    private WorkGroupMapper workGroupMapper;

    public PlanCommonDataMapper(
        EntityMapper entityMapper,
        FunderMapper funderMapper,
        WorkUnitMapper workUnitMapper,
        WorkGroupMapper workGroupMapper)
    {
        this.entityMapper = entityMapper;
        this.funderMapper = funderMapper;
        this.workUnitMapper = workUnitMapper;
        this.workGroupMapper = workGroupMapper;
    }

    @Override
    public PlanCommonDataDTO toDto(Plan plan)
    {
        PlanCommonDataDTO planCommonDataDTO = new PlanCommonDataDTO();
        if (plan != null)
        {
            String workUnitName = plan.getWorkUnit() == null ? null : plan.getWorkUnit().getName();
            String workGroupName = plan.getWorkGroup() == null ? null : plan.getWorkGroup().getName();
            planCommonDataDTO.setWorkUnitName(workUnitName);
            planCommonDataDTO.setWorkGroupName(workGroupName);
            planCommonDataDTO.setComment(plan.getComment());
            planCommonDataDTO.setProductsAvailableForGeneralPublic(
                plan.getProductsAvailableForGeneralPublic());
            setFundersNamesToDto(planCommonDataDTO, plan);
        }

        return planCommonDataDTO;
    }

    private void setFundersNamesToDto(PlanCommonDataDTO planCommonDataDTO, Plan plan)
    {
        planCommonDataDTO.setFunderNames(funderMapper.toDtos(plan.getFunders()));
    }

    @Override
    public Plan toEntity(PlanCommonDataDTO planCommonDataDTO)
    {
        Plan plan = entityMapper.toTarget(planCommonDataDTO, Plan.class);
        Set<Funder> funders =
            new HashSet<>(funderMapper.toEntities(planCommonDataDTO.getFunderNames()));
        plan.setFunders(funders);
        plan.setWorkUnit(workUnitMapper.toEntity(planCommonDataDTO.getWorkUnitName()));
        plan.setWorkGroup(workGroupMapper.toEntity(planCommonDataDTO.getWorkGroupName()));
        return plan;
    }
}
