package org.gentar.biology.plan.engine.es_cell_allele_modification.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.status.Status;
import org.gentar.mockdata.MockData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.TransitionEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EsCellAlleleModificationPlanRevertToCreExcisionCompleteProcessorTest {

    private EsCellAlleleModificationPlanRevertToCreExcisionCompleteProcessor testInstance;

    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new EsCellAlleleModificationPlanRevertToCreExcisionCompleteProcessor(planStateSetter);
    }

    @Test
    void evaluateTransitionWhenExecutable() {
        TransitionEvaluation transitionEvaluation =
            testInstance.evaluateTransition(processEventMockData(),
                MockData.planMockData());

        assertEquals(transitionEvaluation.isExecutable(),transitionEvaluationMockData().isExecutable());


    }

    @Test
    void evaluateTransitionWhenNotExecutable() {

        TransitionEvaluation transitionEvaluation =
            testInstance.evaluateTransition(processEventMockData(),
                notAbortedPlanMockData());

        assertEquals(transitionEvaluation.getNote(),
            "A genotyped confirmed colony is associated with the plan.");

    }

    private static ProcessEvent processEventMockData() {

        return new ProcessEvent() {
            @Override
            public Class<? extends Processor> getNextStepProcessor() {
                return null;
            }

            @Override
            public String getName() {
                return "revertToCreExcisionComplete";
            }

            @Override
            public String getDescription() {
                return "Update to Mouse Allele Modification Genotype Confirmed.";
            }

            @Override
            public ProcessState getInitialState() {
                return new ProcessState() {
                    @Override
                    public String getName() {
                        return "CreExcisionComplete";
                    }

                    @Override
                    public String getInternalName() {
                        return "Cre Excision Complete";
                    }
                };
            }

            @Override
            public ProcessState getEndState() {
                return new ProcessState() {
                    @Override
                    public String getName() {
                        return "CreExcisionStarted";
                    }

                    @Override
                    public String getInternalName() {
                        return "Cre Excision Started";
                    }
                };
            }

            @Override
            public boolean isTriggeredByUser() {
                return false;
            }

            @Override
            public String getTriggerNote() {
                return null;
            }
        };
    }

    private static TransitionEvaluation transitionEvaluationMockData() {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(processEventMockData());
        transitionEvaluation.setExecutable(true);
        transitionEvaluation.setNote(null);
        return transitionEvaluation;
    }


    private Plan notAbortedPlanMockData (){
        Plan plan =MockData.planMockData();
        plan.setOutcomes(Set.of(notAbortedOutcomeMockData()));
        return plan;
    }

    private Outcome notAbortedOutcomeMockData(){
        Outcome outcome = MockData.outcomeMockData();
        outcome.setColony(notAbortedColonyMockData());
        return outcome;
    }
    private Colony notAbortedColonyMockData (){
        Colony colony = MockData.colonyMockData();
        colony.setProcessDataStatus(notAbortedProcessDataMockData());
        return colony;
    }

    private Status notAbortedProcessDataMockData() {
        Status status = MockData.statusMockData();
        status.setIsAbortionStatus(false);
        status.setName(ColonyState.GenotypeConfirmed.getInternalName());
        return status;
    }
}