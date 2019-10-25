package uk.ac.ebi.impc_prod_tracker.web.dto.person;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PersonRoleWorkUnitDTO
{
    private Long id;
    private String workUnitName;
    private String roleName;
}
