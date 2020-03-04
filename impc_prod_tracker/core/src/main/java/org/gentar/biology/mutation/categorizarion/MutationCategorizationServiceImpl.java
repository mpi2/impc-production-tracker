package org.gentar.biology.mutation.categorizarion;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class MutationCategorizationServiceImpl implements MutationCategorizationService
{
    private MutationCategorizationRepository mutationCategorizationRepository;

    public MutationCategorizationServiceImpl(MutationCategorizationRepository mutationCategorizationRepository)
    {
        this.mutationCategorizationRepository = mutationCategorizationRepository;
    }

    public MutationCategorization getMutationCategorizationByNameAndType(String name, String type)
    {
        return mutationCategorizationRepository.findByNameAndTypeIgnoreCase(name, type);
    }

    public MutationCategorization getMutationCategorizationByNameAndTypeFailingWhenNull(String name, String type)
    {
        MutationCategorization mutationCategorization = getMutationCategorizationByNameAndType(name, type);
        if (mutationCategorization == null)
        {
            throw new UserOperationFailedException("Mutation Categorization name '" + name + "' or type '" + type + "' do not exist.");
        }
        return mutationCategorization;
    }
}
