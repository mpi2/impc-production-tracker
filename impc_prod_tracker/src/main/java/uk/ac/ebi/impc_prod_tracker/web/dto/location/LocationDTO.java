package uk.ac.ebi.impc_prod_tracker.web.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.strain.StrainDTO;

@Data
@RequiredArgsConstructor
public class LocationDTO
{
    private String chr;
    private Long start;
    private Long stop;
    private String strand;
    private String genomeBuild;

    @JsonProperty("strainAttributes")
    private StrainDTO strainAttributes;

    private String speciesName;
    private Integer speciesTaxonId;


}
