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


    @JsonProperty("genome_build")
    private String genomeBuild;

    @JsonProperty("strain_attributes")
    private StrainDTO strainAttributes;

    @JsonProperty("species_name")
    private String speciesName;

    @JsonProperty("species_taxon_id")
    private Integer speciesTaxonId;


}
