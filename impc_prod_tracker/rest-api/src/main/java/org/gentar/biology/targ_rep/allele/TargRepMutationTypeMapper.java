package org.gentar.biology.targ_rep.allele;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.mutation.TargRepMutationTypeDTO;
import org.gentar.biology.targ_rep.allele.mutation_type.TargRepMutationType;
import org.gentar.biology.targ_rep.allele.mutation_type.TargRepMutationTypeService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

/**
 * TargRepMutationTypeMapper.
 */
@Component
public class TargRepMutationTypeMapper
    implements Mapper<TargRepMutationType, TargRepMutationTypeDTO> {

    private final TargRepMutationTypeService targRepMutationTypeService;
    private static final String TARG_REP_MUTATION_TYPE_NOT_FOUND_ERROR
        = "targ_rep_mutation_subtype '%s' does not exist.";

    public TargRepMutationTypeMapper(TargRepMutationTypeService targRepMutationTypeService) {
        this.targRepMutationTypeService = targRepMutationTypeService;
    }


    @Override
    public TargRepMutationTypeDTO toDto(TargRepMutationType entity) {
        TargRepMutationTypeDTO targRepMutationTypeDto = new TargRepMutationTypeDTO();
        if (entity != null) {
            targRepMutationTypeDto.setName(entity.getName());
        }
        return targRepMutationTypeDto;
    }

    @Override
    public TargRepMutationType toEntity(TargRepMutationTypeDTO dto) {
        String name = dto.getName();
        TargRepMutationType targRepMutationType =
            targRepMutationTypeService.getTargRepMutationTypeByName(name);
        if (targRepMutationType == null) {
            throw new UserOperationFailedException(
                String.format(TARG_REP_MUTATION_TYPE_NOT_FOUND_ERROR, name));
        }
        return targRepMutationType;
    }
}
