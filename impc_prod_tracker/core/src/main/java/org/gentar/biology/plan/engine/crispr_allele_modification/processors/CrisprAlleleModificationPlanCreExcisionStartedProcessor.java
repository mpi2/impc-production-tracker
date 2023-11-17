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
public class CrisprAlleleModificationPlanCreExcisionStartedProcessor extends AbstractProcessor {

    public CrisprAlleleModificationPlanCreExcisionStartedProcessor(PlanStateSetter planStateSetter)
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
            transitionEvaluation.setNote("A deleter strain or tat Cre needs to be specified, or specify rederivation as the Next Step.");
        } else {
            transitionEvaluation.setNote("Please update again to continue Cre Excision Started state.");
        }
        return transitionEvaluation;
    }

    private boolean identifyDeleterStrainOrTatCre(Plan plan)
    {
        boolean result = false;
        CrisprAlleleModificationAttempt crisprAlleleModificationAttempt = plan.getCrisprAlleleModificationAttempt();
        if (crisprAlleleModificationAttempt != null)
        {
            boolean deleterStrainExists = !(crisprAlleleModificationAttempt.getDeleterStrain() == null);
            result = deleterStrainExists  || (crisprAlleleModificationAttempt.getTatCre()!= null && crisprAlleleModificationAttempt.getTatCre());
        }
        return result;
    }
}
