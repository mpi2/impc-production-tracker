package uk.ac.ebi.impc_prod_tracker.web.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PersonRoleConsortiumDTO
{
    @JsonProperty("consortium_name")
    private String consortiumName;

    @JsonProperty("role_name")
    private String roleName;
}
