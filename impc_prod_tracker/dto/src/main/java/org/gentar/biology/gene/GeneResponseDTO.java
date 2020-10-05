package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class GeneResponseDTO
{
    private Long id;

    @JsonUnwrapped
    private GeneCommonDTO geneCommonDTO;

    private String name;

    private String externalLink;
}
