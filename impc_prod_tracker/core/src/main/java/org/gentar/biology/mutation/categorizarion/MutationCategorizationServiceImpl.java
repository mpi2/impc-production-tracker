package org.gentar.biology.mutation.categorizarion;

import lombok.extern.java.Log;
import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationType;
import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationTypeRepository;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class MutationCategorizationServiceImpl implements MutationCategorizationService
{
    private MutationCategorizationRepository mutationCategorizationRepository;
    private MutationCategorizationTypeRepository mutationCategorizationTypeRepository;

    public MutationCategorizationServiceImpl(
            MutationCategorizationRepository mutationCategorizationRepository,
            MutationCategorizationTypeRepository mutationCategorizationTypeRepository)
    {
        this.mutationCategorizationRepository = mutationCategorizationRepository;
        this.mutationCategorizationTypeRepository = mutationCategorizationTypeRepository;
    }

    public MutationCategorization getMutationCategorizationByNameAndType(String name, String type)
    {
        System.out.println("name");
        System.out.println("type");
        return mutationCategorizationRepository.findByNameAndMutationCategorizationTypeNameIgnoreCase(name, type);
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

    public MutationCategorizationType getMutationCategorizationTypeByName(String name)
    {
        return this.mutationCategorizationTypeRepository.findByNameIgnoreCase(name);
    }

    public MutationCategorizationType getMutationCategorizationTypeByNameFailingWhenNull(String name)
    {
        MutationCategorizationType mutationCategorizationType = this.mutationCategorizationTypeRepository.findByNameIgnoreCase(name);
        if (mutationCategorizationType == null)
        {
            throw new UserOperationFailedException("The Mutation Categorization Type: '" + name + "' does not exist.");
        }
        return mutationCategorizationType;
    }
}
