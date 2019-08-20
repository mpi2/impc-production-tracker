package uk.ac.ebi.impc_prod_tracker.web.dto.strain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StrainDTO
{
    @JsonIgnore
    private Long id;
    String name;
    String mgiAccessionId;
    String typeName;
}
