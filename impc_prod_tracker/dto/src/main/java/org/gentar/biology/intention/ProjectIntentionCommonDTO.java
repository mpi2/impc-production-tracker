package org.gentar.biology.intention;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.mutation.MutationCategorizationDTO;
import java.util.List;

@Data
public class ProjectIntentionCommonDTO
{
    private String molecularMutationTypeName;

    @JsonProperty("mutationCategorizations")
    private List<MutationCategorizationDTO> mutationCategorizationDTOS;
}
