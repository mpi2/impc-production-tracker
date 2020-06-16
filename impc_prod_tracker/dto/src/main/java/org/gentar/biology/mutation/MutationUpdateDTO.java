package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class MutationUpdateDTO
{
    @JsonUnwrapped
    private MutationCommonDTO mutationCommonDTO;
}
