package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.species.SpeciesDTO;

@Data
@RequiredArgsConstructor
public class GeneDTO
{
    private Long id;

    @JsonProperty("accessionId")
    private String accId;

    private String name;
    private String symbol;

    @JsonProperty("species")
    private SpeciesDTO speciesName;

    private String externalLink;
}
