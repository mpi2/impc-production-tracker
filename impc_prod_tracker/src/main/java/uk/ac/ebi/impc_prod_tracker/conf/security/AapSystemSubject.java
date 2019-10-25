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
package uk.ac.ebi.impc_prod_tracker.conf.security;

import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_consortium.PersonRoleConsortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_work_unit.PersonRoleWorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.service.organization.WorkUnitService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Implementation of SystemSubject where most of the user information is taken from a token (jwt). Additional
 * information needs to be loaded from the database.
 * @author Mauricio Martinez
 */
@Component
@Data
@NoArgsConstructor
public class AapSystemSubject implements SystemSubject
{
    private String login;
    private String name;
    private String userRefId;
    private String email;
    private WorkUnit workUnit;
    private PersonRepository personRepository;
    private Person person;
    private boolean isEbiAdmin;
    private List<String> domains = new ArrayList<>();
    private List<PersonRoleWorkUnit> roleWorkUnits;
    private List<PersonRoleConsortium> roleConsortia;
    WorkUnitService workUnitService;

    private final static String NOT_USER_INFORMATION_MESSAGE = "There is not associated information in the system for " +
        "the user [%s].";
    private final static String NULL_AUTH_ID_MESSAGE = "AuthId cannot be null. The jwt token may not have" +
        "all the needed information.";
    private final static String NOT_USER_INFORMATION_DEBUG_MESSAGE =
        "The user [%s] with reference id [%s] was successfully logged in but there is no related information for them " +
            "in the system. Please contact an administrator.";
    private static final String TRACKER_MAINTAINER_DOMAIN_NAME = "self.tracker-maintainer";

    @Autowired
    public AapSystemSubject(PersonRepository personRepository, WorkUnitService workUnitService)
    {
        this.personRepository = personRepository;
        this.workUnitService = workUnitService;
    }

    /**
     * Builds a AapSystemSubject object using the information in the claims of an already validated token (jwt).
     * Then complements the information with additional data taken from the database.
     * @param claims Claims in the token with information about the user.
     * @return SystemSubject with the information.
     */
    public AapSystemSubject buildSystemSubjectByClaims(Claims claims)
    {
        login = claims.get("nickname", String.class);
        name = claims.get("name", String.class);
        userRefId = claims.getSubject();
        email = claims.get("email", String.class);
        domains = claims.get("domains", List.class);

        loadPersonInformation(userRefId);

        return this;
    }

    private boolean isMaintainerUser()
    {
        return domains.contains(TRACKER_MAINTAINER_DOMAIN_NAME);
    }

    /**
     * Simple constructor that sets the minimal information for a user.
     * @param login
     */
    public AapSystemSubject(String login)
    {
        this.login = login;
    }

    private void loadPersonInformation(String authId)
    {
        if (authId == null)
        {
            throw new UserOperationFailedException(NULL_AUTH_ID_MESSAGE);
        }
        this.person = personRepository.findPersonByAuthIdEquals(authId);
        if (person == null)
        {
            if (isMaintainerUser())
            {
                isEbiAdmin = true;
                roleWorkUnits = null;
                roleConsortia = null;
            }
            else
            {
                throw new UserOperationFailedException(
                    String.format(NOT_USER_INFORMATION_MESSAGE, login),
                    String.format(NOT_USER_INFORMATION_DEBUG_MESSAGE, login, authId));
            }
        }
        else
        {
            isEbiAdmin = person.getEbiAdmin() == null ? false : person.getEbiAdmin();
            roleWorkUnits = new ArrayList<>(person.getRoleWorkUnits());
            roleConsortia = new ArrayList<>(person.getRoleConsortia());
        }
    }

    @Override
    public List<PersonRoleWorkUnit> getRoleWorkUnits()
    {
        return roleWorkUnits;
    }

    @Override
    public List<PersonRoleConsortium> getRoleConsortia()
    {
        return roleConsortia;
    }

    @Override
    public Boolean isAdmin()
    {
        return isEbiAdmin;
    }

    @Override
    public boolean belongsToConsortia(Collection<Consortium> consortia)
    {
        return personBelongsToConsortia(consortia) || workUnitBelongsToConsortia(consortia);
    }

    private boolean personBelongsToConsortia(Collection<Consortium> consortia)
    {
        boolean result = false;
        if (roleConsortia != null)
        {
            result = roleConsortia.stream().anyMatch(x -> consortia.contains(x.getConsortium()));
        }
        return result;
    }

    private boolean workUnitBelongsToConsortia(Collection<Consortium> consortia)
    {
        boolean result = false;

        if (roleWorkUnits != null)
        {
            List<Consortium> consortiaByWorkUnits = new ArrayList<>();
            roleWorkUnits.forEach(x -> consortiaByWorkUnits.addAll(getConsortiaByWorkUnit(x.getWorkUnit())));
            result = consortiaByWorkUnits.stream().anyMatch(consortia::contains);
        }
        return result;
    }

    private Set<Consortium> getConsortiaByWorkUnit(WorkUnit workUnit)
    {
        Set<Consortium> result = Collections.emptySet();
        WorkUnit workUnitWithConsortia =
            workUnitService.getWorkUnitWithConsortia(workUnit.getId());
        if (workUnitWithConsortia != null)
        {
            result = workUnitWithConsortia.getConsortia();
        }
        return result;
    }

    @Override
    public List<WorkUnit> getRelatedWorkUnits()
    {
        List<WorkUnit> relatedWorkUnits = new ArrayList<>();

        roleWorkUnits.forEach(x -> relatedWorkUnits.add(x.getWorkUnit()));

        return relatedWorkUnits;
    }

    @Override
    public boolean belongsToAnyWorkUnit(Collection<WorkUnit> workUnits)
    {
        return getRelatedWorkUnits().stream().anyMatch(workUnits::contains);
    }

    @Override
    public List<Role> getRelatedRoles()
    {
        List<Role> relatedRoles = new ArrayList<>();
        roleWorkUnits.forEach(x -> relatedRoles.add(x.getRole()));
        roleConsortia.forEach(x -> relatedRoles.add(x.getRole()));
        return relatedRoles;
    }
}
