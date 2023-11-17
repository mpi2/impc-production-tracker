package org.gentar.biology.plan.engine.crispr_allele_modification.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanQueryHelper;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class CrisprAlleleModificationPlanRevertToCreExcisionCompleteProcessor extends AbstractProcessor {

    public CrisprAlleleModificationPlanRevertToCreExcisionCompleteProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);

        boolean genotypeConfirmedColoniesDoNotExist = !identifyGenotypeConfirmedColonies((Plan) data);

        transitionEvaluation.setExecutable(genotypeConfirmedColoniesDoNotExist);

        if (!genotypeConfirmedColoniesDoNotExist)
        {
            transitionEvaluation.setNote("A genotyped confirmed colony is associated with the plan.");
        }
        return transitionEvaluation;
    }

    private boolean identifyGenotypeConfirmedColonies(Plan plan)
    {
        return PlanQueryHelper.areAnyColoniesByPlanGenotypeConfirmed(plan);
    }

}
