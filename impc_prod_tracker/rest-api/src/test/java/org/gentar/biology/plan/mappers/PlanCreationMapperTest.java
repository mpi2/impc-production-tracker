package org.gentar.biology.plan.mappers;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanBasicDataDTO;
import org.gentar.biology.plan.PlanCommonDataDTO;
import org.gentar.biology.plan.PlanCreationDTO;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PlanCreationMapperTest
{
    private PlanCreationMapper testInstance;

    @Mock
    private PlanBasicDataMapper planBasicDataMapper;
    @Mock
    private PlanTypeMapper planTypeMapper;
    @Mock
    private AttemptTypeMapper attemptTypeMapper;

    @BeforeEach
    public void setup()
    {
        testInstance =
            new PlanCreationMapper(planBasicDataMapper, planTypeMapper, attemptTypeMapper);
    }

    @Test
    void toDto()
    {
        Plan plan = buildPlan();
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        when(planBasicDataMapper.toDto(plan)).thenReturn(planBasicDataDTO);

        testInstance.toDto(plan);

        verify(planTypeMapper, times(1)).toDto(plan.getPlanType());
        verify(attemptTypeMapper, times(1)).toDto(plan.getAttemptType());
    }

    private Plan buildPlan()
    {
        Plan plan = new Plan();
        PlanType planType = new PlanType();
        planType.setName(PlanTypeName.PRODUCTION.getLabel());
        plan.setPlanType(planType);
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypesName.CRISPR.getLabel());
        plan.setAttemptType(attemptType);
        return plan;
    }

    @Test
    void toEntity()
    {
        PlanCreationDTO planCreationDTO = buildPlanCreationDTO();
        PlanBasicDataDTO basicDataDTO = new PlanBasicDataDTO();
        planCreationDTO.setPlanBasicDataDTO(basicDataDTO);
        Plan plan = new Plan();
        when(planBasicDataMapper.toEntity(basicDataDTO)).thenReturn(plan);

        testInstance.toEntity(planCreationDTO);

        verify(planTypeMapper, times(1)).toEntity(planCreationDTO.getPlanTypeName());
        verify(attemptTypeMapper, times(1)).toEntity(planCreationDTO.getAttemptTypeName());
    }

    private PlanCreationDTO buildPlanCreationDTO()
    {
        PlanCreationDTO planCreationDTO = new PlanCreationDTO();
        PlanBasicDataDTO planBasicDataDTO = new PlanBasicDataDTO();
        PlanCommonDataDTO planCommonDataDTO = new PlanCommonDataDTO();
        planBasicDataDTO.setPlanCommonDataDTO(planCommonDataDTO);
        planCreationDTO.setPlanBasicDataDTO(planBasicDataDTO);
        planCreationDTO.setPlanTypeName(PlanTypeName.PRODUCTION.getLabel());
        planCreationDTO.setAttemptTypeName(AttemptTypesName.CRISPR.getLabel());
        return planCreationDTO;
    }
}
