package uk.ac.ebi.impc_prod_tracker.web.dto.location;

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
    private StrainDTO strain;
    private String speciesName;
    private Integer speciesTaxonId;
}
