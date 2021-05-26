package org.gentar.biology.plan.engine.cre_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.cre_allele_modification.CreAlleleModificationAttempt;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class CreAlleleModificationPlanCreExcisionStartedProcessor extends AbstractProcessor {

    public CreAlleleModificationPlanCreExcisionStartedProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean deleterStrainOrTatCreExists = identifyDeleterStrainOrTatCre((Plan) data);
        transitionEvaluation.setExecutable(deleterStrainOrTatCreExists);
        if (!deleterStrainOrTatCreExists)
        {
            transitionEvaluation.setNote("A deleter strain or tat Cre needs to be specified.");
        }
        return transitionEvaluation;
    }

    private boolean identifyDeleterStrainOrTatCre(Plan plan)
    {
        boolean result = false;
        CreAlleleModificationAttempt creAlleleModificationAttempt = plan.getCreAlleleModificationAttempt();
        if (creAlleleModificationAttempt != null)
        {
            boolean deleterStrainExists = !(creAlleleModificationAttempt.getDeleterStrain() == null);
            result = deleterStrainExists || creAlleleModificationAttempt.getTatCre();
        }
        return result;
    }
}
