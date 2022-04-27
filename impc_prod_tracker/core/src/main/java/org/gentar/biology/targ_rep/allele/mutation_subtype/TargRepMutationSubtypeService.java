package org.gentar.biology.targ_rep.allele.mutation_subtype;

/**
 * TargRepMutationSubtypeService.
 */
public interface TargRepMutationSubtypeService {
    /**
     * Get a {@link TargRepMutationSubtype} object identified by the given name.
     *
     * @return TargRepMutationSubtype identified by name. Null if not found.
     */
    TargRepMutationSubtype getTargRepMutationSubtypeByName(String name);
}
