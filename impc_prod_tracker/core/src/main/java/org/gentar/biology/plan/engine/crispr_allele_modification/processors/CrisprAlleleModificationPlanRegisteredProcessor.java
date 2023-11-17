package org.gentar.biology.plan.engine.crispr_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanStatusManager;

import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class CrisprAlleleModificationPlanRegisteredProcessor extends AbstractProcessor {

    private static final String USER_ACTION_PRESENT_ERROR_MESSAGE =
        "Trying to execute action [updateToMouseAlleleModificationRegistered] but also modifying data that causes a change in status. " +
            "Please do this in two different steps.";

    private final PlanStatusManager planStatusManager;

    public CrisprAlleleModificationPlanRegisteredProcessor(PlanStateSetter planStateSetter,
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
        boolean crisprAlleleModificationAttemptExists = crisprAlleleModificationAttemptExists((Plan) data);
        transitionEvaluation.setExecutable(crisprAlleleModificationAttemptExists);

        if (!crisprAlleleModificationAttemptExists)
        {
            transitionEvaluation.setNote(
                "The plan does not have a Crispr allele modification attempt yet");
        }
        else if (doesTwoDifferentStepsNeeded((Plan) data))
        {
            transitionEvaluation.setNote(USER_ACTION_PRESENT_ERROR_MESSAGE);
        }



        return transitionEvaluation;
    }

    private boolean crisprAlleleModificationAttemptExists(Plan plan)
    {
        return plan.getCrisprAlleleModificationAttempt() != null;
    }

    private boolean doesTwoDifferentStepsNeeded(Plan plan) {
        CrisprAlleleModificationAttempt crisprAlleleModificationAttempt =
            plan.getCrisprAlleleModificationAttempt();

            int successfulCreMatings =
                crisprAlleleModificationAttempt.getNumberOfCreMatingsSuccessful() == null ? 0 :
                    crisprAlleleModificationAttempt.getNumberOfCreMatingsSuccessful();

            boolean deleterStrainExists =
                !(crisprAlleleModificationAttempt.getDeleterStrain() == null);

            return successfulCreMatings > 0 || deleterStrainExists ||
                (crisprAlleleModificationAttempt.getTatCre() != null &&
                    crisprAlleleModificationAttempt.getTatCre());

    }
}
