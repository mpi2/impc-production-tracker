package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class GeneCreationDTO
{
    @JsonUnwrapped
    private GeneCommonDTO geneCommonDTO;
}
