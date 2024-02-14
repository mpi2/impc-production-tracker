package org.gentar.biology.targ_rep.allele;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.mutation.TargRepMutationSubtypeDTO;
import org.gentar.biology.targ_rep.allele.mutation_subtype.TargRepMutationSubtype;
import org.gentar.biology.targ_rep.allele.mutation_subtype.TargRepMutationSubtypeService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

/**
 * TargRepMutationSubtypeMapper.
 */
@Component
public class TargRepMutationSubtypeMapper
    implements Mapper<TargRepMutationSubtype, TargRepMutationSubtypeDTO> {

    private final TargRepMutationSubtypeService targRepMutationSubtypeService;
    private static final String TARG_REP_MUTATION_SUBTYPE_NOT_FOUND_ERROR
        = "targ_rep_mutation_subtype '%s' does not exist.";

    public TargRepMutationSubtypeMapper(
        TargRepMutationSubtypeService targRepMutationSubtypeService) {
        this.targRepMutationSubtypeService = targRepMutationSubtypeService;
    }


    @Override
    public TargRepMutationSubtypeDTO toDto(TargRepMutationSubtype entity) {
        TargRepMutationSubtypeDTO targRepMutationSubtypeDto = new TargRepMutationSubtypeDTO();
        if (entity != null) {
            targRepMutationSubtypeDto.setName(entity.getName());
        }
        return targRepMutationSubtypeDto;
    }

    @Override
    public TargRepMutationSubtype toEntity(TargRepMutationSubtypeDTO dto) {
        String name = dto.getName();
        TargRepMutationSubtype targRepMutationSubtype =
            targRepMutationSubtypeService.getTargRepMutationSubtypeByName(name);
        if (targRepMutationSubtype == null) {
            throw new UserOperationFailedException(
                String.format(TARG_REP_MUTATION_SUBTYPE_NOT_FOUND_ERROR, name));
        }
        return targRepMutationSubtype;
    }
}
