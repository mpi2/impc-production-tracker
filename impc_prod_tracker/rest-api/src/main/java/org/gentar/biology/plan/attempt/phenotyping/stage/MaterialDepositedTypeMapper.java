package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedType;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedTypeService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class MaterialDepositedTypeMapper implements Mapper<MaterialDepositedType, String>
{
    private final MaterialDepositedTypeService materialDepositedTypeService;

    private static final String MATERIAL_DEPOSITED_TYPE_NOT_FOUND_ERROR = "Material deposited type '%s' does not exist.";

    public MaterialDepositedTypeMapper(MaterialDepositedTypeService materialDepositedTypeService)
    {
        this.materialDepositedTypeService = materialDepositedTypeService;
    }

    @Override
    public String toDto(MaterialDepositedType entity) {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    @Override
    public MaterialDepositedType toEntity(String name) {
        MaterialDepositedType materialDepositedType = materialDepositedTypeService.getMaterialDepositedTypeByName(name);

        if (materialDepositedType == null)
        {
            throw new UserOperationFailedException(MATERIAL_DEPOSITED_TYPE_NOT_FOUND_ERROR);
        }

        return materialDepositedType;
    }
}
