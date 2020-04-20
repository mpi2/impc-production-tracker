package org.gentar.statemachine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanState;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractProcessorTest
{
    private TestClass testInstance;

    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new TestClass(planStateSetter);
    }

    @Test
    public void testExceptionWhenNull()
    {
        SystemOperationFailedException thrown = assertThrows(SystemOperationFailedException.class,
            () -> testInstance.process(null),
            "Exception not thrown");
        assertThat(
            "Not expected message",
            thrown.getExceptionDetail(),
            is("Entity is null"));
    }

    @Test
    public void testNoStatusChangeWhenEventIsNull()
    {
        Plan plan = new Plan();
        plan.setEvent(null);

        testInstance.process(plan);

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testNoStatusChangeWhenNoConditionsMet()
    {
        Plan plan = new Plan();
        plan.setEvent(null);
        testInstance = spy(testInstance);
        when(testInstance.canExecuteTransition(any(Plan.class))).thenReturn(false);

        testInstance.process(plan);

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testExceptionWhenIncorrectInitialStatus()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.EmbryosObtained.getInternalName())
            .build();
        plan.setEvent(CrisprProductionPlanEvent.changeToInProgress);

        SystemOperationFailedException thrown = assertThrows(SystemOperationFailedException.class,
            () -> testInstance.process(plan),
            "Exception not thrown");
        assertThat(
            "Not expected message",
            thrown.getExceptionDetail(),
            is("Transition [changeToInProgress] cannot be executed because it goes from status " +
                "[Plan Created] to [Attempt In Progress] but current status in the entity [Plan]" +
                " is [Embryos Obtained]"));
    }

    @Test
    public void testStatusChangesWhenConditionsMet()
    {
        Plan plan = PlanBuilder.getInstance()
            .withStatus(CrisprProductionPlanState.PlanCreated.getInternalName())
            .build();
        plan.setEvent(CrisprProductionPlanEvent.changeToInProgress);

        testInstance.process(plan);

        verify(
            planStateSetter,
            times(1)).setStatusByName(any(Plan.class),
            eq(CrisprProductionPlanEvent.changeToInProgress.getEndState().getInternalName()));
    }

    static class TestClass extends AbstractProcessor
    {

        public TestClass(PlanStateSetter planStateSetter)
        {
            super(planStateSetter);
        }

        @Override
        protected boolean canExecuteTransition(ProcessData entity)
        {
            return true;
        }
    }
}