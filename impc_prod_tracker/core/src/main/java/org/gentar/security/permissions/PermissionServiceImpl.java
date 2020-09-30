/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.security.permissions;

import org.gentar.organization.person.Person;
import org.gentar.organization.person.PersonRepository;
import org.gentar.organization.person.PersonService;
import org.gentar.organization.work_unit.WorkUnitService;
import org.gentar.security.abac.subject.AapSystemSubject;
import org.gentar.security.abac.subject.SystemSubject;
import org.springframework.stereotype.Component;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.project.ProjectService;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissionServiceImpl implements PermissionService
{
    private static final String MANAGE_USERS_KEY = "manageUsers";
    private static final String EXECUTE_MANAGER_TASKS_KEY = "executeManagerTasks";

    private static final String UPDATE_PLAN = "canUpdatePlan";
    private static final String UPDATE_PROJECT = "canUpdateProject";
    private static final String MANAGE_GENE_LISTS = "canManageGeneLists";

    private final ContextAwarePolicyEnforcement policyEnforcement;
    private final PlanService planService;
    private final ProjectService projectService;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final WorkUnitService workUnitService;

    public PermissionServiceImpl(
        ContextAwarePolicyEnforcement policyEnforcement,
        PlanService planService,
        ProjectService projectService,
        PersonService personService,
        PersonRepository personRepository,
        WorkUnitService workUnitService)
    {
        this.policyEnforcement = policyEnforcement;
        this.planService = planService;
        this.projectService = projectService;
        this.personService = personService;
        this.personRepository = personRepository;
        this.workUnitService = workUnitService;
    }

    @Override
    public List<ActionPermission> getPermissionsLoggedUser()
    {
        List<ActionPermission> actionPermissions = new ArrayList<>();
        actionPermissions.add(getManageUsersPermissionLoggedUser());
        actionPermissions.add(getExecutingManagementTasksLoggedUser());
        actionPermissions.add(getManageGeneListsLoggedUser());
        return actionPermissions;
    }

    @Override
    public List<ActionPermission> getPermissionsByUser(String userEmail)
    {
        Person user = personService.getPersonByEmail(userEmail);
        SystemSubject personAsSystemSubject
            = new AapSystemSubject(personRepository, workUnitService, user);

        List<ActionPermission> actionPermissions = new ArrayList<>();
        actionPermissions.add(getManageUsersPermissionByUser(personAsSystemSubject));
        actionPermissions.add(getExecutingManagementTasksByUser(personAsSystemSubject));
        actionPermissions.add(getManageGeneListsByUser(personAsSystemSubject));
        return actionPermissions;
    }

    private ActionPermission getManageUsersPermissionLoggedUser()
    {
        ActionPermission actionPermission = new ActionPermission();
        actionPermission.setActionName(MANAGE_USERS_KEY);
        actionPermission.setValue(policyEnforcement.hasPermission(null, Actions.MANAGE_USERS_ACTION));
        return actionPermission;
    }

    private ActionPermission getExecutingManagementTasksLoggedUser()
    {
        ActionPermission actionPermission = new ActionPermission();
        actionPermission.setActionName(EXECUTE_MANAGER_TASKS_KEY);
        actionPermission.setValue(
            policyEnforcement.hasPermission(null, Actions.EXECUTE_MANAGER_TASKS_ACTION));
        return actionPermission;
    }

    private ActionPermission getManageGeneListsLoggedUser()
    {
        ActionPermission actionPermission = new ActionPermission();
        actionPermission.setActionName(MANAGE_GENE_LISTS);
        actionPermission.setValue(
            policyEnforcement.hasPermission(null, Actions.MANAGE_GENE_LIST_ACTION));
        return actionPermission;
    }

    private ActionPermission getManageUsersPermissionByUser(SystemSubject user)
    {
        ActionPermission actionPermission = new ActionPermission();
        actionPermission.setActionName(MANAGE_USERS_KEY);
        actionPermission.setValue(policyEnforcement.hasPermission(user, null, Actions.MANAGE_USERS_ACTION));
        return actionPermission;
    }

    private ActionPermission getExecutingManagementTasksByUser(SystemSubject user)
    {
        ActionPermission actionPermission = new ActionPermission();
        actionPermission.setActionName(EXECUTE_MANAGER_TASKS_KEY);
        actionPermission.setValue(
            policyEnforcement.hasPermission(user, null, Actions.EXECUTE_MANAGER_TASKS_ACTION));
        return actionPermission;
    }

    private ActionPermission getManageGeneListsByUser(SystemSubject user)
    {
        ActionPermission actionPermission = new ActionPermission();
        actionPermission.setActionName(MANAGE_GENE_LISTS);
        actionPermission.setValue(
            policyEnforcement.hasPermission(user, null, Actions.MANAGE_GENE_LIST_ACTION));
        return actionPermission;
    }

    @Override
    public boolean getPermissionByActionOnResource(String action, String resourceId)
    {
        Object resource = null;
        String actionInPolicySystem = null;
        boolean hasPermission;
        switch (action)
        {
            case UPDATE_PLAN:
                resource = planService.getPlanByPinWithoutCheckPermissions(resourceId);
                actionInPolicySystem = Actions.UPDATE_PLAN_ACTION;
                break;

            case UPDATE_PROJECT:
                resource = projectService.getProjectByPinWithoutCheckPermissions(resourceId);
                actionInPolicySystem = Actions.UPDATE_PROJECT_ACTION;
                break;

            default:
        }

        if (resource == null)
        {
            hasPermission = false;
        }
        else
        {
            hasPermission = policyEnforcement.hasPermission(resource, actionInPolicySystem);
        }

        return hasPermission;
    }
}
