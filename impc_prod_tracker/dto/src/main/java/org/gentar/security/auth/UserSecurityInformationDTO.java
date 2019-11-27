package org.gentar.security.auth;

import lombok.Data;
import org.gentar.organization.person.PersonRoleConsortiumDTO;
import org.gentar.organization.person.PersonRoleWorkUnitDTO;

import java.util.List;

@Data
public class UserSecurityInformationDTO
{
    private String userName;
    private List<PersonRoleWorkUnitDTO> rolesWorkUnits;
    private List<PersonRoleConsortiumDTO> rolesConsortia;
    private boolean isAdmin;
}
