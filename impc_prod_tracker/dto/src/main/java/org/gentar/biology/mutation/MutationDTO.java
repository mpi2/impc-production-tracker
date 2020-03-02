package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.gene.ProjectIntentionGeneDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
public class MutationDTO
{
    @JsonIgnore
    private Long id;
    private String alleleSymbol;
    private String mgiAlleleId;
    private String geneticMutationTypeName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("mutationGene")
    private List<ProjectIntentionGeneDTO> projectIntentionGeneDTOS;
}
