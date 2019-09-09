package uk.ac.ebi.impc_prod_tracker.web.dto.species;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SpeciesDTO {

    private String name;

    @JsonProperty("taxon_id")
    private Integer taxonId;
}
