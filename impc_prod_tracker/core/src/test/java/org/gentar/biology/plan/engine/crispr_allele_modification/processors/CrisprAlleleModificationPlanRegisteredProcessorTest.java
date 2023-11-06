package org.gentar.biology.plan.engine.crispr_allele_modification.processors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.mockdata.MockData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.TransitionEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class CrisprAlleleModificationPlanRegisteredProcessorTest {

    private CrisprAlleleModificationPlanRegisteredProcessor testInstance;

    @Mock
    private PlanStatusManager planStatusManager;

    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new CrisprAlleleModificationPlanRegisteredProcessor(planStateSetter, planStatusManager);
    }

    @Test
    void evaluateTransitionWhenTwoDifferentStepsNeeded() {



            TransitionEvaluation transitionEvaluation =
                testInstance.evaluateTransition(processEventMockData(),
                    planMockData());


        assertEquals(transitionEvaluation.getNote(),"Trying to execute action [updateToMouseAlleleModificationRegistered] but also modifying data that causes a change in status. Please do this in two different steps.");




    }
    @Test
    void evaluateTransitioneIsCellAlleleModificationAttemptNotExists() {

        TransitionEvaluation transitionEvaluation =
            testInstance.evaluateTransition(processEventMockData(),
                MockData.planMockData());

        assertEquals(transitionEvaluation.getNote(),"The plan does not have a Crispr allele modification attempt yet");
    }

    private static ProcessEvent processEventMockData() {
        ProcessEvent processEvent = new ProcessEvent() {
            @Override
            public Class<? extends Processor> getNextStepProcessor() {
                return null;
            }

            @Override
            public String getName() {
                return "updateToMouseAlleleModificationRegistered";
            }

            @Override
            public String getDescription() {
                return "Update to mouse allele modification registered.";
            }

            @Override
            public ProcessState getInitialState() {
                return new ProcessState() {
                    @Override
                    public String getName() {
                        return "PlanCreated";
                    }

                    @Override
                    public String getInternalName() {
                        return "Plan Created";
                    }
                };
            }

            @Override
            public ProcessState getEndState() {
                return new ProcessState() {
                    @Override
                    public String getName() {
                        return "MouseAlleleModificationRegistered";
                    }

                    @Override
                    public String getInternalName() {
                        return "Mouse Allele Modification Registered";
                    }
                };
            }

            @Override
            public boolean isTriggeredByUser() {
                return true;
            }

            @Override
            public String getTriggerNote() {
                return null;
            }
        };

        return processEvent;
    }

    private static TransitionEvaluation transitionEvaluationMockData() {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(processEventMockData());
        transitionEvaluation.setExecutable(true);
        transitionEvaluation.setNote(null);
        return transitionEvaluation;
    }

    private Plan planMockData(){
        Plan plan = MockData.planMockData();
        plan.setCrisprAlleleModificationAttempt(crisprAlleleModificationAttemptMockData());
        return plan;
    }

    private CrisprAlleleModificationAttempt crisprAlleleModificationAttemptMockData(){
        CrisprAlleleModificationAttempt crisprAlleleModificationAttempt =  new CrisprAlleleModificationAttempt();
        crisprAlleleModificationAttempt.setTatCre(true);
        return crisprAlleleModificationAttempt;
    }

}