package org.gentar.biology.mutation;

import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.categorizarion.MutationCategorizationRepository;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.genetic_type.GeneticMutationTypeRepository;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.mutation.molecular_type.MolecularMutationTypeRepository;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class MutationServiceImpl implements MutationService
{
    private GeneticMutationTypeRepository geneticMutationTypeRepository;
    private MutationCategorizationRepository mutationCategorizationRepository;
    private MolecularMutationTypeRepository molecularMutationTypeRepository;

    public MutationServiceImpl(GeneticMutationTypeRepository geneticMutationTypeRepository, MutationCategorizationRepository mutationCategorizationRepository,
                               MolecularMutationTypeRepository molecularMutationTypeRepository)
    {
        this.geneticMutationTypeRepository = geneticMutationTypeRepository;
        this.mutationCategorizationRepository = mutationCategorizationRepository;
        this.molecularMutationTypeRepository = molecularMutationTypeRepository;
    }

    public GeneticMutationType getGeneticMutationTypeByName(String geneticMutationTypeName)
    {
        return geneticMutationTypeRepository.findFirstByNameIgnoreCase(geneticMutationTypeName);
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

    public MolecularMutationType getMolecularMutationTypeByName(String name)
    {
        return molecularMutationTypeRepository.findByNameIgnoreCase(name);
    }

    public MolecularMutationType getMolecularMutationTypeByNameFailingWhenNull(String name)
    {
        MolecularMutationType molecularMutationType = getMolecularMutationTypeByName(name);
        if (molecularMutationType == null)
        {
            throw new UserOperationFailedException("Molecular mutation type name '" + name + "' does not exist.");
        }
        return molecularMutationType;
    }
}
