package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
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
class FounderObtainedProcessorTest
{
    private FounderObtainedProcessor testInstance;
    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new FounderObtainedProcessor(planStateSetter);
    }

    @Test
    public void testWhenNoAssay()
    {
        testInstance.process(new Plan());

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testWhenNoMutants()
    {
        Plan plan = buildPlanReadyToMoveToFounderObtained();

        testInstance.process(plan);

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testWhenNhejG0Mutants()
    {
        Plan plan = buildPlanReadyToMoveToFounderObtained();
        plan.getCrisprAttempt().getAssay().setNumNhejG0Mutants(1);

        testInstance.process(plan);

        verify(
            planStateSetter,
            times(1)).setStatusByName(any(Plan.class),
            eq(CrisprProductionPlanEvent.updateToFounderObtained.getEndState().getInternalName()));
    }

    @Test
    public void testWhenNumDeletionG0Mutants()
    {
        Plan plan = buildPlanReadyToMoveToFounderObtained();
        plan.getCrisprAttempt().getAssay().setNumDeletionG0Mutants(1);

        testInstance.process(plan);

        verify(
            planStateSetter,
            times(1)).setStatusByName(any(Plan.class),
            eq(CrisprProductionPlanEvent.updateToFounderObtained.getEndState().getInternalName()));
    }

    @Test
    public void testWhenNumHrG0Mutants()
    {
        Plan plan = buildPlanReadyToMoveToFounderObtained();
        plan.getCrisprAttempt().getAssay().setNumHrG0Mutants(1);

        testInstance.process(plan);

        verify(
            planStateSetter,
            times(1)).setStatusByName(any(Plan.class),
            eq(CrisprProductionPlanEvent.updateToFounderObtained.getEndState().getInternalName()));
    }

    @Test
    public void testWhenNumHdrG0Mutants()
    {
        Plan plan = buildPlanReadyToMoveToFounderObtained();
        plan.getCrisprAttempt().getAssay().setNumHdrG0Mutants(1);

        testInstance.process(plan);

        verify(
            planStateSetter,
            times(1)).setStatusByName(any(Plan.class),
            eq(CrisprProductionPlanEvent.updateToFounderObtained.getEndState().getInternalName()));
    }

    private Plan buildPlanReadyToMoveToFounderObtained()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.EmbryosObtained.getInternalName())
            .build();
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        Assay assay = new Assay();
        crisprAttempt.setAssay(assay);
        plan.setCrisprAttempt(crisprAttempt);
        plan.setEvent(CrisprProductionPlanEvent.updateToFounderObtained);
        return plan;
    }
}