package org.gentar.biology.plan.engine.cre_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.cre_allele_modification.CreAlleleModificationAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class CreAlleleModificationPlanCreExcisionCompleteProcessor extends AbstractProcessor {

    public CreAlleleModificationPlanCreExcisionCompleteProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);

        boolean successfulMatingsAndEitherDeleterStrainOrTatCreExists =
                identifySuccessfulMatingsAndDeleterStrainOrTatCre((Plan) data);

        transitionEvaluation.setExecutable(successfulMatingsAndEitherDeleterStrainOrTatCreExists);

        if (!successfulMatingsAndEitherDeleterStrainOrTatCreExists)
        {
            transitionEvaluation.setNote("A deleter strain or tat Cre needs to be specified together with successful matings.");
        }
        return transitionEvaluation;
    }

    private boolean identifySuccessfulMatingsAndDeleterStrainOrTatCre(Plan plan)
    {
        boolean result = false;
        CreAlleleModificationAttempt creAlleleModificationAttempt = plan.getCreAlleleModificationAttempt();
        if (creAlleleModificationAttempt != null)
        {
            int successfulCreMatings =
                    creAlleleModificationAttempt.getNumberOfCreMatingsSuccessful() == null ? 0 :
                            creAlleleModificationAttempt.getNumberOfCreMatingsSuccessful();
            boolean deleterStrainExists = !(creAlleleModificationAttempt.getDeleterStrain() == null);
            result = (successfulCreMatings > 0 && deleterStrainExists) || creAlleleModificationAttempt.getTatCre();
        }
        return result;
    }
}
