/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.security.permissions;

import org.gentar.basic_data.entity_values.EntitiesValuesByUserService;
import org.gentar.basic_data.entity_values.EntityValues;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PermissionController
{
    private final PermissionService permissionService;
    private final EntitiesValuesByUserService entitiesValuesByUserService;

    public PermissionController(
        PermissionService permissionService, EntitiesValuesByUserService entitiesValuesByUserService)
    {

        this.permissionService = permissionService;
        this.entitiesValuesByUserService = entitiesValuesByUserService;
    }

    @GetMapping(value = {"/permissions"})
    public List<ActionPermission> getConfiguration()
    {
        return permissionService.getPermissionsLoggedUser();
    }

    @GetMapping(value = {"/permissionByActionOnResource"})
    public boolean getConfiguration(
        @RequestParam String action, @RequestParam String resourceId)
    {
        return permissionService.getPermissionByActionOnResource(action, resourceId);
    }

    @GetMapping(value = {"/listsByManagerUser"})
    public List<EntityValues> getManagedEntities()
    {
        return entitiesValuesByUserService.getListsByManagerUser();
    }
}
