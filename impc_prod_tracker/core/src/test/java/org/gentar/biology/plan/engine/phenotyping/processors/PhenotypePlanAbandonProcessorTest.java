package org.gentar.biology.plan.engine.phenotyping.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.engine.phenotyping.PhenotypePlanEvent;
import org.gentar.biology.plan.engine.phenotyping.PhenotypePlanState;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PhenotypePlanAbandonProcessorTest {

    private PhenotypePlanAbandonProcessor testInstance;

    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypePlanAbandonProcessor(planStateSetter);
    }

    @Test
    public void testAbandonWhenNoPhenotypingStagesAndNoPhenotypingAttempt()
    {
        Plan plan = PlanBuilder.getInstance()
                .withStatus(PhenotypePlanState.PlanCreated.getInternalName())
                .build();

        plan.setProcessDataEvent(PhenotypePlanEvent.abortPhenotypingPlan);
        testInstance.process(plan);

        verify(planStateSetter, times(1)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testAbandonWhenNoPhenotypingStagesButPhenotypingAttempt()
    {
        Plan plan = PlanBuilder.getInstance()
                .withStatus(PhenotypePlanState.PlanCreated.getInternalName())
                .build();

        PhenotypingAttempt phenotypingAttempt = new PhenotypingAttempt();
        plan.setPhenotypingAttempt(phenotypingAttempt);

        plan.setProcessDataEvent(PhenotypePlanEvent.abortPhenotypingPlan);
        testInstance.process(plan);

        verify(planStateSetter, times(1)).setStatusByName(any(Plan.class), any(String.class));
    }

    @Test
    public void testAbandonWhenPhenotypingStages()
    {
        Plan plan = PlanBuilder.getInstance()
                .withStatus(PhenotypePlanState.PlanCreated.getInternalName())
                .build();

        Set<PhenotypingStage> phenotypingStages = new HashSet<>();
        PhenotypingStage phenotypingStage1 = buildPhenotypingStage(true);
        PhenotypingStage phenotypingStage2 = buildPhenotypingStage(true);
        phenotypingStages.add(phenotypingStage1);
        phenotypingStages.add(phenotypingStage2);
        PhenotypingAttempt phenotypingAttempt = new PhenotypingAttempt();
        phenotypingAttempt.setPhenotypingStages(phenotypingStages);
        plan.setPhenotypingAttempt(phenotypingAttempt);

        plan.setProcessDataEvent(PhenotypePlanEvent.abortPhenotypingPlan);

        UserOperationFailedException thrown = assertThrows(UserOperationFailedException.class,
                () -> testInstance.process(plan), "Exception not thrown");
        assertThat("Not expected message", thrown.getMessage(), is("Transition cannot be executed The plan already has phenotyping stages. Please abort the plan."));

        verify(planStateSetter, times(0)).setStatusByName(any(Plan.class), any(String.class));
    }

    private PhenotypingStage buildPhenotypingStage(boolean isAborted)
    {
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        Status status = new Status();
        status.setIsAbortionStatus(isAborted);
        phenotypingStage.setProcessDataStatus(status);
        return phenotypingStage;
    }

}
