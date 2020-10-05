package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeneCommonDTO
{
    @JsonProperty("accessionId")
    private String accId;

    private String symbol;

    private String speciesName;
}
