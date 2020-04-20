package org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type;

import org.springframework.stereotype.Component;

@Component
public class MaterialDepositedTypeServiceImpl implements MaterialDepositedTypeService
{
    private MaterialDepositedTypeRepository materialDepositedTypeRepository;

    public MaterialDepositedTypeServiceImpl (MaterialDepositedTypeRepository materialDepositedTypeRepository)
    {
        this.materialDepositedTypeRepository = materialDepositedTypeRepository;
    }

    public MaterialDepositedType getMaterialDepositedTypeByName (String name)
    {
        return materialDepositedTypeRepository.findByNameIgnoreCase(name);
    }
}
