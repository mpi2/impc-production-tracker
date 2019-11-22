package org.gentar.web.dto.person;

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
