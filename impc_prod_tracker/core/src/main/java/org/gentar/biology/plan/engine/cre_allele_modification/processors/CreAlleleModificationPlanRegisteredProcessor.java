package org.gentar.biology.plan.engine.cre_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class CreAlleleModificationPlanRegisteredProcessor extends AbstractProcessor {

    public CreAlleleModificationPlanRegisteredProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean creAlleleModificationAttemptExists = creAlleleModificationAttemptExists((Plan) data);
        transitionEvaluation.setExecutable(creAlleleModificationAttemptExists);
        if (!creAlleleModificationAttemptExists)
        {
            transitionEvaluation.setNote(
                    "The plan does not have a Cre allele modification attempt yet");
        }
        return transitionEvaluation;
    }

    private boolean creAlleleModificationAttemptExists(Plan plan)
    {
        return plan.getCreAlleleModificationAttempt() != null;
    }
}
