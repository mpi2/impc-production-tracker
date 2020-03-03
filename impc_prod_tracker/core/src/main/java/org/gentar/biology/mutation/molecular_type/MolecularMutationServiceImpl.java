package org.gentar.biology.mutation.molecular_type;

import org.springframework.stereotype.Component;

@Component
public class MolecularMutationServiceImpl implements MolecularMutationService
{
    private MolecularMutationTypeRepository molecularMutationTypeRepository;

    public MolecularMutationServiceImpl(MolecularMutationTypeRepository molecularMutationTypeRepository)
    {
        this.molecularMutationTypeRepository = molecularMutationTypeRepository;
    }

    public MolecularMutationType getMolecularMutationTypeByName(String name)
    {
        return molecularMutationTypeRepository.findFirstByNameIgnoreCase(name);
    }
}
