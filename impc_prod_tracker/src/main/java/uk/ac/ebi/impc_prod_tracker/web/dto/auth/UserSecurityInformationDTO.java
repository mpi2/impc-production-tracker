package uk.ac.ebi.impc_prod_tracker.web.dto.auth;

import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.web.dto.person.PersonRoleConsortiumDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.person.PersonRoleWorkUnitDTO;

import java.util.List;

@Data
public class UserSecurityInformationDTO
{
    private String userName;
    private List<PersonRoleWorkUnitDTO> rolesWorkUnits;
    private List<PersonRoleConsortiumDTO> rolesConsortia;
    private boolean isAdmin;
}
