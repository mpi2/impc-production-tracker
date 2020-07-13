package org.gentar.organization.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class PersonCommonDTO
{
    private String name;
    private Boolean contactable;
    @JsonProperty("isAdmin")
    private Boolean ebiAdmin;
    private Boolean isActive;
    @JsonProperty("rolesWorkUnits")
    private List<PersonRoleWorkUnitDTO> rolesWorkUnitsDtos;
    @JsonProperty("rolesConsortia")
    private List<PersonRoleConsortiumDTO> rolesConsortiaDtos;
}
