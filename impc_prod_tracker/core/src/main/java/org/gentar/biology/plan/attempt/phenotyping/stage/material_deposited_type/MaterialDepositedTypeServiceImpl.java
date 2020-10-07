package org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type;

import org.springframework.stereotype.Component;

@Component
public class MaterialDepositedTypeServiceImpl implements MaterialDepositedTypeService
{
    private final MaterialDepositedTypeRepository materialDepositedTypeRepository;

    public MaterialDepositedTypeServiceImpl (MaterialDepositedTypeRepository materialDepositedTypeRepository)
    {
        this.materialDepositedTypeRepository = materialDepositedTypeRepository;
    }

    @Override
    public MaterialDepositedType getMaterialDepositedTypeByName (String name)
    {
        return materialDepositedTypeRepository.findByNameIgnoreCase(name);
    }
}
