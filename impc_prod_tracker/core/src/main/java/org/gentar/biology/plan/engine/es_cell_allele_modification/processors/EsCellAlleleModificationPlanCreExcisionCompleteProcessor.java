package org.gentar.biology.plan.engine.es_cell_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class EsCellAlleleModificationPlanCreExcisionCompleteProcessor extends AbstractProcessor {

    public EsCellAlleleModificationPlanCreExcisionCompleteProcessor(
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
        EsCellAlleleModificationAttempt esCellAlleleModificationAttempt =
            plan.getEsCellAlleleModificationAttempt();
        if (esCellAlleleModificationAttempt != null) {
            int successfulCreMatings =
                esCellAlleleModificationAttempt.getNumberOfCreMatingsSuccessful() == null ? 0 :
                    esCellAlleleModificationAttempt.getNumberOfCreMatingsSuccessful();
            boolean deleterStrainExists =
                !(esCellAlleleModificationAttempt.getDeleterStrain() == null);
            result = (successfulCreMatings > 0 && deleterStrainExists) ||
                (esCellAlleleModificationAttempt.getTatCre() != null &&
                    esCellAlleleModificationAttempt.getTatCre());
        }
        return result;
    }
}
