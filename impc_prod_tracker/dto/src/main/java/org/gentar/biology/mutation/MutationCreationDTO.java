package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.gene.GeneCreationDTO;
import java.util.List;

@Data
public class MutationCreationDTO
{
    @JsonUnwrapped
    private MutationCommonDTO mutationCommonDTO;

    @JsonProperty("genes")
    private List<GeneCreationDTO> geneCreationDTOS;
}
