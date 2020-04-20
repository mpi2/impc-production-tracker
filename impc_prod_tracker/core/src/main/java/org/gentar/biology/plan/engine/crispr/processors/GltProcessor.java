package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

@Component
public class GltProcessor extends AbstractProcessor
{
    public GltProcessor(PlanStateSetter planStateSetter)
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
            result = assay.getNumNhejG0Mutants() > 0
                || assay.getNumDeletionG0Mutants() > 0
                || assay.getNumHrG0Mutants() > 0
                || assay.getNumHdrG0Mutants() > 0;
        }
        return result;
    }
}
