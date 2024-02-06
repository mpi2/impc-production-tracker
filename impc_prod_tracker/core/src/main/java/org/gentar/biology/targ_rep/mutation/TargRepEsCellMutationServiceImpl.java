package org.gentar.biology.targ_rep.mutation;

import org.springframework.stereotype.Component;


/**
 * TargRepEsCellServiceImpl.
 */
@Component
public class TargRepEsCellMutationServiceImpl implements TargRepEsCellMutationService {

    private final TargRepEsCellMutationRepository mutationRepository;

    public TargRepEsCellMutationServiceImpl(TargRepEsCellMutationRepository mutationRepository) {
        this.mutationRepository = mutationRepository;
    }

    @Override
    public TargRepEsCellMutation getTargRepEsCellMutationByEsCellId(Long esCellId) {
        return mutationRepository.findByEsCellId(esCellId);
    }
}
