package org.gentar.biology.location;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LocationDTO
{
    private Long id;
    private String chr;
    private Long start;
    private Long stop;
    private String strand;
    private String genomeBuild;
    private String strainName;
    private String speciesName;
}
