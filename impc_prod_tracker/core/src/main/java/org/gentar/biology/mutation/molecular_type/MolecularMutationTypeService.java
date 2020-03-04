package org.gentar.biology.mutation.molecular_type;

public interface MolecularMutationTypeService
{
    MolecularMutationType getMolecularMutationTypeByName(String name);

    MolecularMutationType getMolecularMutationTypeByNameFailingWhenNull(String name);
}
