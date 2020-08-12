package org.gentar.biology.mutation.symbolConstructor;

import org.gentar.biology.mutation.MutationRepository;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.springframework.stereotype.Component;

@Component
public class AlleleSymbolHandler
{
    private final MutationRepository mutationRepository;

    public AlleleSymbolHandler(MutationRepository mutationRepository)
    {
        this.mutationRepository = mutationRepository;
    }

    public AlleleSymbolConstructor getAlleleSymbolConstructor(Plan plan)
    {
        AlleleSymbolConstructor alleleSymbolConstructor = null;
        AttemptTypesName attemptTypesName =
            AttemptTypesName.valueOfLabel(plan.getAttemptType().getName());
        if (attemptTypesName.equals(AttemptTypesName.CRISPR))
        {
            alleleSymbolConstructor = new CrisprAlleleSymbolConstructor(mutationRepository);
        }

        return alleleSymbolConstructor;
    }
}
