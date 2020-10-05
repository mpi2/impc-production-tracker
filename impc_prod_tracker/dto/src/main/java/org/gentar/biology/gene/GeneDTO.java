package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Deprecated
@Data
@RequiredArgsConstructor
public class GeneDTO
{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonProperty("accessionId")
    private String accId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    private String symbol;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String speciesName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String externalLink;
}
