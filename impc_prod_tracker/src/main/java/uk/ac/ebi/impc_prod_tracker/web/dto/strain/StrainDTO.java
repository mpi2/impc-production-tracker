package uk.ac.ebi.impc_prod_tracker.web.dto.strain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StrainDTO
{
    @JsonIgnore
    private Long id;
    private String name;
    @JsonProperty("mgi_accession")
    private String mgiId;
    @JsonProperty("mgi_name")
    private String mgiName;
}
