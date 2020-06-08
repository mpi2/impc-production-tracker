package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class MutationCreationDTO
{
    @JsonUnwrapped
    private MutationCommonDTO mutationCommonDTO;
}
