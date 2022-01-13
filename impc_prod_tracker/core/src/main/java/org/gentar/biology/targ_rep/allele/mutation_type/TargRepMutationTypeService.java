package org.gentar.biology.targ_rep.allele.mutation_type;

public interface TargRepMutationTypeService {
    /**
     * Get a {@link TargRepMutationType} object with the information of the species identified by the given name.
     * @param name
     * @return TargRepMutationType identified by name. Null if not found.
     */
    TargRepMutationType getTargRepMutationTypeByName(String name);
}
