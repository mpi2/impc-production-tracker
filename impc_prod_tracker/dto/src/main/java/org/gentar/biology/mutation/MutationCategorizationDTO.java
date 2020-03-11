package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MutationCategorizationDTO
{
    private String name;
    private String description;

    @JsonProperty("typeName")
    private String mutationCategorizationTypeName;
}
