package org.gentar.biology.plan.mappers;

import org.gentar.EntityMapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_group.WorkGroupMapper;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PlanCommonDataMapperTest
{
    public static final String WORK_UNIT_NAME = "workUnitName";
    public static final String WORK_GROUP_NAME = "workGroupName";
    public static final String PLAN_COMMENT = "plan Comment";
    public static final boolean PRODUCTS_AVAILABLE_FOR_GENERAL_PUBLIC = true;
    private PlanCommonDataMapper testInstance;

    @Autowired
    private EntityMapper entityMapper;
    @Mock
    private FunderMapper funderMapper;
    @Mock
    private WorkUnitMapper workUnitMapper;
    @Mock
    private WorkGroupMapper workGroupMapper;

    @BeforeEach
    void setUp()
    {
        testInstance =
            new PlanCommonDataMapper(entityMapper, funderMapper, workUnitMapper, workGroupMapper);
    }

    @Test
    void toDto()
    {
        Plan plan = buildPlan();
        PlanCommonDataDTO planCommonDataDTO = testInstance.toDto(plan);

        verify(funderMapper, times(1)).toDtos(plan.getFunders());
        assertThat(planCommonDataDTO, is(notNullValue()));
        assertThat(planCommonDataDTO.getComment(), is(PLAN_COMMENT));
        assertThat(planCommonDataDTO.getWorkUnitName(), is(WORK_UNIT_NAME));
        assertThat(planCommonDataDTO.getWorkGroupName(), is(WORK_GROUP_NAME));
        assertThat(
            planCommonDataDTO.getProductsAvailableForGeneralPublic(),
            is(PRODUCTS_AVAILABLE_FOR_GENERAL_PUBLIC));
    }

    private Plan buildPlan()
    {
        Plan plan = new Plan();
        Set<Funder> funders = new HashSet<>();
        Funder funder = new Funder();
        funders.add(funder);
        plan.setFunders(funders);
        PlanType planType = new PlanType();
        planType.setName(PlanTypeName.PRODUCTION.getLabel());
        plan.setPlanType(planType);
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypesName.CRISPR.getLabel());
        plan.setAttemptType(attemptType);
        WorkUnit workUnit = new WorkUnit();
        workUnit.setName(WORK_UNIT_NAME);
        plan.setWorkUnit(workUnit);
        WorkGroup workGroup = new WorkGroup();
        workGroup.setName(WORK_GROUP_NAME);
        plan.setWorkGroup(workGroup);
        plan.setComment(PLAN_COMMENT);
        plan.setProductsAvailableForGeneralPublic(PRODUCTS_AVAILABLE_FOR_GENERAL_PUBLIC);
        return plan;
    }

    @Test
    void toEntity()
    {
        PlanCommonDataDTO planCommonDataDTO = buildPlanCommonDataDTO();
        WorkUnit workUnit = new WorkUnit();
        workUnit.setName(WORK_UNIT_NAME);
        when(workUnitMapper.toEntity(WORK_UNIT_NAME)).thenReturn(workUnit);
        WorkGroup workGroup = new WorkGroup();
        workGroup.setName(WORK_GROUP_NAME);
        when(workGroupMapper.toEntity(WORK_GROUP_NAME)).thenReturn(workGroup);

        Plan plan = testInstance.toEntity(planCommonDataDTO);

        verify(funderMapper, times(1)).toEntities(planCommonDataDTO.getFunderNames());
        assertThat(plan, is(notNullValue()));
        assertThat(plan.getComment(), is(PLAN_COMMENT));
        assertThat(plan.getWorkUnit().getName(), is(WORK_UNIT_NAME));
        assertThat(plan.getWorkGroup().getName(), is(WORK_GROUP_NAME));
        assertThat(
            plan.getProductsAvailableForGeneralPublic(), is(PRODUCTS_AVAILABLE_FOR_GENERAL_PUBLIC));
    }

    private PlanCommonDataDTO buildPlanCommonDataDTO()
    {
        PlanCommonDataDTO planCommonDataDTO = new PlanCommonDataDTO();
        planCommonDataDTO.setComment(PLAN_COMMENT);
        planCommonDataDTO.setWorkUnitName(WORK_UNIT_NAME);
        planCommonDataDTO.setWorkGroupName(WORK_GROUP_NAME);
        planCommonDataDTO.setProductsAvailableForGeneralPublic(PRODUCTS_AVAILABLE_FOR_GENERAL_PUBLIC);
        return planCommonDataDTO;
    }
}