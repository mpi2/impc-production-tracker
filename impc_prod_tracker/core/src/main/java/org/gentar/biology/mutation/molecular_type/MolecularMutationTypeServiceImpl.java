package org.gentar.biology.mutation.molecular_type;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class MolecularMutationTypeServiceImpl implements MolecularMutationTypeService
{
    private MolecularMutationTypeRepository molecularMutationTypeRepository;

    public MolecularMutationTypeServiceImpl(MolecularMutationTypeRepository molecularMutationTypeRepository)
    {
        this.molecularMutationTypeRepository = molecularMutationTypeRepository;
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
