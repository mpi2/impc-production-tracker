package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

@Component
public class FounderObtainedProcessor extends AbstractProcessor
{
    public FounderObtainedProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return mutantsExist((Plan) entity);
    }

    private boolean mutantsExist(Plan plan)
    {
        boolean result = false;
        CrisprAttempt crisprAttempt = plan.getCrisprAttempt();
        Assay assay = crisprAttempt == null ? null : crisprAttempt.getAssay();
        if (assay != null)
        {
            int numNhejG0Mutants =
                assay.getNumNhejG0Mutants() == null ? 0 : assay.getNumNhejG0Mutants();
            int numDeletionG0Mutants =
                assay.getNumDeletionG0Mutants() == null ? 0 : assay.getNumDeletionG0Mutants();
            int numHrG0Mutants = assay.getNumHrG0Mutants() == null ? 0 : assay.getNumHrG0Mutants();
            int numHdrG0Mutants = assay.getNumHdrG0Mutants() == null ? 0 : assay.getNumHdrG0Mutants();
            result = numNhejG0Mutants > 0
                || numDeletionG0Mutants > 0
                || numHrG0Mutants > 0
                || numHdrG0Mutants > 0;
        }
        return result;
    }
}
