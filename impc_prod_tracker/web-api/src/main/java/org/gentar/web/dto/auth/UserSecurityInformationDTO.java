package org.gentar.web.dto.auth;

import lombok.Data;
import org.gentar.web.dto.person.PersonRoleConsortiumDTO;
import org.gentar.web.dto.person.PersonRoleWorkUnitDTO;

import java.util.List;

@Data
public class UserSecurityInformationDTO
{
    private String userName;
    private List<PersonRoleWorkUnitDTO> rolesWorkUnits;
    private List<PersonRoleConsortiumDTO> rolesConsortia;
    private boolean isAdmin;
}
