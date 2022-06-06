package org.gentar.biology.targ_rep.mutation;

import org.springframework.data.repository.CrudRepository;

/**
 * TargRepEsCellMutationRepository.
 */
public interface TargRepEsCellMutationRepository
    extends CrudRepository<TargRepEsCellMutation, Long> {

    TargRepEsCellMutation findByEsCellId(Long esCellId);
}
