package org.gentar.biology.targ_rep.allele.mutation_type;

/**
 * TargRepMutationTypeService.
 */
public interface TargRepMutationTypeService {
    /**
     * Get a {@link TargRepMutationType} object identified by the given name.
     *
     * @return TargRepMutationType identified by name. Null if not found.
     */
    default TargRepMutationType getTargRepMutationTypeByName(String name) {
        return null;
    }
}
