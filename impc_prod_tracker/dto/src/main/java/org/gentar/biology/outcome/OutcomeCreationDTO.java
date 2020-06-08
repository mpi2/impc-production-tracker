package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.mutation.MutationDTO;
import java.util.List;

@Data
public class OutcomeCreationDTO
{
    private String outcomeTypeName;

    @JsonUnwrapped
    private OutcomeCommonDTO outcomeCommonDTO;

    @JsonProperty("mutations")
    private List<MutationDTO> mutationDTOS;
}
