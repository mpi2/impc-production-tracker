package org.gentar.biology.plan.engine.es_cell_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanStatusManager;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class EsCellAlleleModificationPlanRegisteredProcessor extends AbstractProcessor {

    private static final String USER_ACTION_PRESENT_ERROR_MESSAGE =
        "Trying to execute action [updateToMouseAlleleModificationRegistered] but also modifying data that causes a change in status. " +
            "Please do this in two different steps.";

    private final PlanStatusManager planStatusManager;

    public EsCellAlleleModificationPlanRegisteredProcessor(PlanStateSetter planStateSetter,
                                                           PlanStatusManager planStatusManager)
    {
        super(planStateSetter);
        this.planStatusManager = planStatusManager;
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean esCellAlleleModificationAttemptExists = esCellAlleleModificationAttemptExists((Plan) data);
        transitionEvaluation.setExecutable(esCellAlleleModificationAttemptExists);

        if (!esCellAlleleModificationAttemptExists)
        {
            transitionEvaluation.setNote(
                "The plan does not have a ES Cell allele modification attempt yet");
        }
        else if (doesTwoDifferentStepsNeeded((Plan) data))
        {
            transitionEvaluation.setNote(USER_ACTION_PRESENT_ERROR_MESSAGE);
        }



        return transitionEvaluation;
    }

    private boolean esCellAlleleModificationAttemptExists(Plan plan)
    {
        return plan.getEsCellAlleleModificationAttempt() != null;
    }

    private boolean doesTwoDifferentStepsNeeded(Plan plan) {
        EsCellAlleleModificationAttempt esCellAlleleModificationAttempt =
            plan.getEsCellAlleleModificationAttempt();

            int successfulCreMatings =
                esCellAlleleModificationAttempt.getNumberOfCreMatingsSuccessful() == null ? 0 :
                    esCellAlleleModificationAttempt.getNumberOfCreMatingsSuccessful();

            boolean deleterStrainExists =
                !(esCellAlleleModificationAttempt.getDeleterStrain() == null);

            return successfulCreMatings > 0 || deleterStrainExists ||
                (esCellAlleleModificationAttempt.getTatCre() != null &&
                    esCellAlleleModificationAttempt.getTatCre());

    }
}
