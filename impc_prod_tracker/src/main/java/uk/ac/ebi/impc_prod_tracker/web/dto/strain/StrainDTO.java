package uk.ac.ebi.impc_prod_tracker.web.dto.strain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StrainDTO
{
    private Long id;

    private String name;

    @JsonProperty("mgiStrainAccId")
    private String mgiStrainAccId;
}
