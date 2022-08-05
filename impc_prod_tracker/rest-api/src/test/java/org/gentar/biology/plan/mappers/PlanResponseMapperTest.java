package org.gentar.biology.plan.mappers;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.status.PlanStatusStamp;
import org.gentar.biology.plan.status.PlanSummaryStatusStamp;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusStampMapper;
import org.gentar.statemachine.TransitionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlanResponseMapperTest
{
    public static final String STATUS_NAME = "Status Name";
    private PlanResponseMapper testInstance;

    @Mock
    private PlanCreationMapper planCreationMapper;
    @Mock
    private StatusStampMapper statusStampMapper;
    @Mock
    private PlanService planService;
    @Mock
    private TransitionMapper transitionMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new PlanResponseMapper(
            planCreationMapper,
            statusStampMapper,
            planService,
            transitionMapper);
    }

    @Test
    void toDto()
    {
        Plan plan = buildPlan();

        testInstance.toDto(plan);

        verify(planCreationMapper, times(1)).toDto(plan);
        verify(statusStampMapper, times(1)).toDtos(plan.getPlanStatusStamps());
        verify(statusStampMapper, times(1)).toDtos(plan.getPlanSummaryStatusStamps());
        verify(planService, times(1)).evaluateNextTransitions(plan);
        verify(transitionMapper, times(1)).toDtos(any());
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
        Status status = new Status();
        status.setName(STATUS_NAME);
        plan.setPlanStatus(status);
        Set<PlanStatusStamp> statusStamps = new HashSet<>();
        PlanStatusStamp planStatusStamp = new PlanStatusStamp();
        statusStamps.add(planStatusStamp);
        plan.setPlanStatusStamps(statusStamps);
        Set<PlanSummaryStatusStamp> planSummaryStatusStamps = new HashSet<>();
        PlanSummaryStatusStamp planSummaryStatusStamp = new PlanSummaryStatusStamp();
        planSummaryStatusStamps.add(planSummaryStatusStamp);
        plan.setPlanSummaryStatusStamps(planSummaryStatusStamps);

        return plan;
    }
}