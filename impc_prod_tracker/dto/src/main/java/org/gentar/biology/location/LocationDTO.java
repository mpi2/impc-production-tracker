package org.gentar.biology.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
    private String strainName;

    @JsonProperty("species")
    private String speciesName;
}
