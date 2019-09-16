package uk.ac.ebi.impc_prod_tracker.web.dto.gene;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GeneDTO
{
    @JsonProperty("accession_id_value")
    private String idListValue;
    private String symbol;
    @JsonProperty("species_name")
    private String speciesName;
}
