package org.gentar.biology.mutation.symbolConstructor;

import org.gentar.biology.mutation.Mutation;

/**
 * Classes implementing this interface must produce an allele symbol taking into account different
 * attributes in the mutation
 */
public interface AlleleSymbolConstructor
{
    String calculateAlleleSymbol(Mutation mutation);
}
