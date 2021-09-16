package org.gentar.biology.plan.engine.es_cell_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class EsCellAlleleModificationPlanRegisteredProcessor extends AbstractProcessor {

    public EsCellAlleleModificationPlanRegisteredProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
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
        return transitionEvaluation;
    }

    private boolean esCellAlleleModificationAttemptExists(Plan plan)
    {
        return plan.getEsCellAlleleModificationAttempt() != null;
    }
}
