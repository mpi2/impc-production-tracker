package org.gentar.biology.targ_rep.allele.mutation_subtype;

import org.springframework.data.repository.CrudRepository;

public interface TargRepMutationSubtypeRepository extends CrudRepository<TargRepMutationSubtype, Long> {
    TargRepMutationSubtype findTargRepMutationSubtypeByNameIgnoreCase(String name);
}
