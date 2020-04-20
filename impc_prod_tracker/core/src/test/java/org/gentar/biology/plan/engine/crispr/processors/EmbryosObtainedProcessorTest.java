package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanState;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmbryosObtainedProcessorTest
{
    private EmbryosObtainedProcessor testInstance;

    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    public void setup()
    {
        testInstance = new EmbryosObtainedProcessor(planStateSetter);
    }

    @Test
    public void testWhenNullEmbryos()
    {
        Plan plan = new Plan();
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        crisprAttempt.setTotalEmbryosInjected(null);
        crisprAttempt.setTotalEmbryosSurvived(null);
        plan.setCrisprAttempt(crisprAttempt);

        testInstance.process(plan);

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testWhenZeroEmbryos()
    {
        Plan plan = new Plan();
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        crisprAttempt.setTotalEmbryosInjected(0);
        crisprAttempt.setTotalEmbryosSurvived(0);
        plan.setCrisprAttempt(crisprAttempt);

        testInstance.process(plan);

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testWhenEmbryosInjected()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.AttemptInProgress.getInternalName())
            .build();
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        crisprAttempt.setTotalEmbryosInjected(1);
        crisprAttempt.setTotalEmbryosSurvived(0);
        plan.setCrisprAttempt(crisprAttempt);
        plan.setEvent(CrisprProductionPlanEvent.changeToEmbryosObtained);

        testInstance.process(plan);

        verify(
            planStateSetter,
            times(1)).setStatusByName(any(Plan.class),
            eq(CrisprProductionPlanEvent.changeToEmbryosObtained.getEndState().getInternalName()));
    }

    @Test
    public void testWhenEmbryosSurvived()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.AttemptInProgress.getInternalName())
            .build();
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        crisprAttempt.setTotalEmbryosInjected(0);
        crisprAttempt.setTotalEmbryosSurvived(1);
        plan.setCrisprAttempt(crisprAttempt);
        plan.setEvent(CrisprProductionPlanEvent.changeToEmbryosObtained);

        testInstance.process(plan);

        verify(
            planStateSetter,
            times(1)).setStatusByName(any(Plan.class),
            eq(CrisprProductionPlanEvent.changeToEmbryosObtained.getEndState().getInternalName()));
    }
}