package uk.ac.ebi.impc_prod_tracker.web.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data
@RequiredArgsConstructor
public class AuthenticationResponseDTO
{
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("roles_work_units")
    private List<PersonRoleWorkUnitDTO> rolesWorkUnits;

    @JsonProperty("roles_consortia")
    private List<PersonRoleConsortiumDTO> rolesConsortia;
}

