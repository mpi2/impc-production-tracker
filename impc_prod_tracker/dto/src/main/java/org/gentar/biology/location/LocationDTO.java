package org.gentar.biology.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.species.SpeciesDTO;
import org.gentar.biology.strain.StrainDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
public class LocationDTO
{
    private String chr;
    private Long start;
    private Long stop;
    private String strand;
    private String genomeBuild;

    @JsonProperty("strain")
    private StrainDTO strainName;

    @JsonProperty("species")
    private SpeciesDTO speciesName;
}
