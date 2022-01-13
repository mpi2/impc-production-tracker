package org.gentar.biology.targ_rep.allele.mutation_type;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TargRepMutationTypeServiceImpl implements TargRepMutationTypeService{

    private final TargRepMutationTypeRepository targRepMutationTypeRepository;

    public TargRepMutationTypeServiceImpl(TargRepMutationTypeRepository targRepMutationTypeRepository) {
        this.targRepMutationTypeRepository = targRepMutationTypeRepository;
    }

    @Override
    @Cacheable("targRepMutationTypeNames")
    public TargRepMutationType getTargRepMutationTypeByName(String name)
    {
        return targRepMutationTypeRepository.findTargRepMutationTypeByNameIgnoreCase(name);
    }
}
