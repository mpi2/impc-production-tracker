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
public class EsCellAlleleModificationPlanCreExcisionStartedProcessor extends AbstractProcessor {

    public EsCellAlleleModificationPlanCreExcisionStartedProcessor(PlanStateSetter planStateSetter)
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
        EsCellAlleleModificationAttempt esCellAlleleModificationAttempt = plan.getEsCellAlleleModificationAttempt();
        if (esCellAlleleModificationAttempt != null)
        {
            boolean deleterStrainExists = !(esCellAlleleModificationAttempt.getDeleterStrain() == null);
            result = deleterStrainExists || esCellAlleleModificationAttempt.getTatCre();
        }
        return result;
    }
}
