package uk.ac.ebi.impc_prod_tracker.web.dto.person;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PersonRoleConsortiumDTO
{
    private Long id;
    private String consortiumName;
    private String roleName;
}
