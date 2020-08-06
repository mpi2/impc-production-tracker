package org.gentar.biology.mutation.symbolCalculator;

import org.gentar.biology.mutation.Mutation;

/**
 * Classes implementing this interface must produce an allele symbol taking into account different
 * attributes in the mutation
 */
public interface AlleleSymbolCalculator
{
    String calculateAlleleSymbol(Mutation mutation);
}