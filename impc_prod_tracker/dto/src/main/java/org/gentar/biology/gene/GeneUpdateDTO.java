package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class GeneUpdateDTO
{
    private Long id;

    @JsonUnwrapped
    private GeneCommonDTO geneCommonDTO;
}
