package uk.ac.ebi.impc_prod_tracker.web.dto.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PersonRoleWorkUnitDTO
{
    private String workUnitName;
    private String roleName;
}
