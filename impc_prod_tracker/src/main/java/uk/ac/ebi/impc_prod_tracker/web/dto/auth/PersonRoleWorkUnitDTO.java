package uk.ac.ebi.impc_prod_tracker.web.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PersonRoleWorkUnitDTO
{
    @JsonProperty("work_unit_name")
    private String workUnitName;

    @JsonProperty("role_name")
    private String roleName;
}
