package org.gentar.biology.plan.engine.es_cell_allele_modification.processors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.engine.crispr.processors.AbortFounderObtainedProcessor;
import org.gentar.biology.status.Status;
import org.gentar.biology.targ_rep.ikmc_project.TargRepIkmcProject;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.mockdata.MockData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.TransitionEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class EsCellAlleleModificationPlanRegisteredProcessorTest {

    private EsCellAlleleModificationPlanRegisteredProcessor testInstance;

    @Mock
    private PlanStatusManager planStatusManager;

    @Mock
    private PlanStateSetter planStateSetter;

    @BeforeEach
    void setUp()
    {
        testInstance = new EsCellAlleleModificationPlanRegisteredProcessor(planStateSetter, planStatusManager);
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

        assertEquals(transitionEvaluation.getNote(),"The plan does not have a ES Cell allele modification attempt yet");
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
        plan.setEsCellAlleleModificationAttempt(esCellAlleleModificationAttemptMockData());
        return plan;
    }

    private EsCellAlleleModificationAttempt esCellAlleleModificationAttemptMockData(){
        EsCellAlleleModificationAttempt esCellAlleleModificationAttempt =  new EsCellAlleleModificationAttempt();
        esCellAlleleModificationAttempt.setTatCre(true);
        return esCellAlleleModificationAttempt;
    }

}