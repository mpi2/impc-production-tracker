package org.gentar.biology.plan.engine.crispr_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class CrisprAlleleModificationPlanCreExcisionCompleteProcessor extends AbstractProcessor {

    public CrisprAlleleModificationPlanCreExcisionCompleteProcessor(
        PlanStateSetter planStateSetter) {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data) {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);

        boolean successfulMatingsAndEitherDeleterStrainOrTatCreExists =
            identifySuccessfulMatingsAndDeleterStrainOrTatCre((Plan) data);

        transitionEvaluation.setExecutable(successfulMatingsAndEitherDeleterStrainOrTatCreExists);

        if (!successfulMatingsAndEitherDeleterStrainOrTatCreExists) {
            transitionEvaluation.setNote(
                "Successful matings need to be specified to continue Cre Excision Completed state.");
        } else {
            transitionEvaluation.setNote(
                "Please update again to continue Cre Excision Completed state.");
        }
        return transitionEvaluation;
    }

    private boolean identifySuccessfulMatingsAndDeleterStrainOrTatCre(Plan plan) {
        boolean result = false;
        CrisprAlleleModificationAttempt crisprAlleleModificationAttempt =
            plan.getCrisprAlleleModificationAttempt();
        if (crisprAlleleModificationAttempt != null) {
            int successfulCreMatings =
                crisprAlleleModificationAttempt.getNumberOfCreMatingsSuccessful() == null ? 0 :
                    crisprAlleleModificationAttempt.getNumberOfCreMatingsSuccessful();
            boolean deleterStrainExists =
                !(crisprAlleleModificationAttempt.getDeleterStrain() == null);
            result = (successfulCreMatings > 0 && deleterStrainExists) ||
                (crisprAlleleModificationAttempt.getTatCre() != null &&
                    crisprAlleleModificationAttempt.getTatCre());
        }
        return result;
    }
}
