package org.gentar.biology.targ_rep.allele.mutation_subtype;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * TargRepMutationSubtypeServiceImpl.
 */
@Component
public class TargRepMutationSubtypeServiceImpl implements TargRepMutationSubtypeService {

    private final TargRepMutationSubtypeRepository targRepMutationSubtypeRepository;

    public TargRepMutationSubtypeServiceImpl(
        TargRepMutationSubtypeRepository targRepMutationSubtypeRepository) {
        this.targRepMutationSubtypeRepository = targRepMutationSubtypeRepository;
    }

    @Override
    @Cacheable("targRepMutationSubtypeNames")
    public TargRepMutationSubtype getTargRepMutationSubtypeByName(String name) {
        return targRepMutationSubtypeRepository.findTargRepMutationSubtypeByNameIgnoreCase(name);
    }
}
