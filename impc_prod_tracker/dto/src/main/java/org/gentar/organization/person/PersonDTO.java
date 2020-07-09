/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.organization.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class PersonDTO
{
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String email;
    private Boolean contactable;
    private List<PersonRoleWorkUnitDTO> rolesWorkUnits;
    private List<PersonRoleConsortiumDTO> rolesConsortia;
    @JsonProperty("isAdmin")
    private boolean ebiAdmin;
    private List<ActionPermissionDTO> actionPermissions;
}
