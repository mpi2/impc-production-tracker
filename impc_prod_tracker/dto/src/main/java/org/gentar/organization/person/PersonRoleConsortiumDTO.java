package org.gentar.organization.person;

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
