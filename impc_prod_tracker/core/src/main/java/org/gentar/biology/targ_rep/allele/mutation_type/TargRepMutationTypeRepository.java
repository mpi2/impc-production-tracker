package org.gentar.biology.targ_rep.allele.mutation_type;

import org.springframework.data.repository.CrudRepository;

/**
 * TargRepMutationTypeRepository.
 */
public interface TargRepMutationTypeRepository extends CrudRepository<TargRepMutationType, Long> {

    TargRepMutationType findTargRepMutationTypeByNameIgnoreCase(String name);

}
