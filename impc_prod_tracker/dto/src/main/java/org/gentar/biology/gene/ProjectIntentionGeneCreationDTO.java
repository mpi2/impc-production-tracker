package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectIntentionGeneCreationDTO
{
    @JsonProperty("gene")
    private GeneCreationDTO geneDTO;
}
