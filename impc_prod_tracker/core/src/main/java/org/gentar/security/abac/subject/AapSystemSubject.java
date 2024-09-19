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

package org.gentar.security.abac.subject;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.person.associations.FluentPersonRoleConsortiumList;
import org.gentar.organization.person.associations.FluentPersonRoleWorkUnitList;
import org.gentar.organization.work_unit.WorkUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.person.Person;
import org.gentar.organization.person.PersonRepository;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import org.gentar.organization.work_unit.WorkUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Implementation of SystemSubject where most of the user information is taken from a token (jwt). Additional
 * information needs to be loaded from the database.
 *
 * @author Mauricio Martinez
 */
@Component
@NoArgsConstructor
public class AapSystemSubject implements SystemSubject {
    private String login;
    private String name;
    @Setter
    private String userRefId;
    private String email;
    private WorkUnit workUnit;
    private PersonRepository personRepository;
    private Person person = new Person();
    private boolean isEbiAdmin;
    private List<PersonRoleWorkUnit> roleWorkUnits;
    private List<PersonRoleConsortium> roleConsortia;
    private WorkUnitService workUnitService;

    @Value("${cda_user_key}")
    private String CDA_USER_VALUE;

    @Value("${dcc_user_key}")
    private String DCC_USER_VALUE;

    private final static String CDA_USER_KEY = "cda_user_key";

    private final static String DCC_USER_KEY = "dcc_user_key";

    private final static String NOT_USER_INFORMATION_MESSAGE =
            "There is not associated information in the system for " +
                    "the user [%s].";
    private final static String NULL_AUTH_ID_MESSAGE =
            "AuthId cannot be null. The jwt token may not have" +
                    "all the needed information.";
    private final static String NOT_USER_INFORMATION_DEBUG_MESSAGE =
            "The user [%s] with reference id [%s] was successfully logged in but there is no related information for them " +
                    "in the system. Please contact an administrator.";

    @Autowired
    public AapSystemSubject(PersonRepository personRepository, WorkUnitService workUnitService) {
        this.personRepository = personRepository;
        this.workUnitService = workUnitService;
    }

    public AapSystemSubject(
            PersonRepository personRepository, WorkUnitService workUnitService, Person person) {
        this.personRepository = personRepository;
        this.workUnitService = workUnitService;
        this.person = person;
        setPersonData(person);
    }

    /**
     * Simple constructor that sets the minimal information for a user.
     *
     */
    public AapSystemSubject(String login) {
        this.login = login;
    }

    private void loadPersonInformation(String authId) {
        if (authId == null) {
            throw new UserOperationFailedException(NULL_AUTH_ID_MESSAGE);
        }
        this.person = personRepository.findPersonByAuthIdEquals(authId);
        if (person == null) {
            throw new UserOperationFailedException(
                    String.format(NOT_USER_INFORMATION_MESSAGE, login),
                    String.format(NOT_USER_INFORMATION_DEBUG_MESSAGE, login, authId));
        } else {
            setPersonData(person);
        }
    }

    private void setPersonData(Person person) {
        isEbiAdmin = person.getEbiAdmin() != null && person.getEbiAdmin();
        roleWorkUnits = new ArrayList<>(person.getPersonRolesWorkUnits());
        roleConsortia = new ArrayList<>(person.getPersonRolesConsortia());
    }

    @Override
    public Person getPerson() {
        return person;
    }

    @Override
    public String getLogin() {
        return person.getEmail();
    }

    @Override
    public String getName() {
        return person.getName();
    }

    @Override
    public String getUserRefId() {
        return person.getAuthId();
    }

    @Override
    public String getEmail() {
        return person.getEmail();
    }

    @Override
    public List<PersonRoleWorkUnit> getRoleWorkUnits() {
        return roleWorkUnits;
    }

    @Override
    public List<PersonRoleConsortium> getRoleConsortia() {
        return roleConsortia;
    }

    @Override
    public Boolean isAdmin() {
        return isEbiAdmin;
    }

    @Override
    public boolean belongsToConsortia(Collection<Consortium> consortia) {
        return personBelongsToConsortia(consortia) || workUnitBelongsToConsortia(consortia);
    }

    private boolean personBelongsToConsortia(Collection<Consortium> consortia) {
        boolean result = false;
        if (roleConsortia != null) {
            result = roleConsortia.stream().anyMatch(x -> consortia.contains(x.getConsortium()));
        }
        return result;
    }

    private boolean workUnitBelongsToConsortia(Collection<Consortium> consortia) {
        boolean result = false;

        if (roleWorkUnits != null) {
            List<Consortium> consortiaByWorkUnits = new ArrayList<>();
            roleWorkUnits
                    .forEach(x -> consortiaByWorkUnits.addAll(getConsortiaByWorkUnit(x.getWorkUnit())));
            result = consortiaByWorkUnits.stream().anyMatch(consortia::contains);
        }
        return result;
    }

    private Set<Consortium> getConsortiaByWorkUnit(WorkUnit workUnit) {
        Set<Consortium> result = Collections.emptySet();
        WorkUnit workUnitWithConsortia =
                workUnitService.getWorkUnitWithConsortia(workUnit.getId());
        if (workUnitWithConsortia != null) {
            result = workUnitWithConsortia.getConsortia();
        }
        return result;
    }

    @Override
    public List<WorkUnit> getRelatedWorkUnits() {
        List<WorkUnit> relatedWorkUnits = new ArrayList<>();
        if (roleWorkUnits != null) {
            roleWorkUnits.forEach(x -> relatedWorkUnits.add(x.getWorkUnit()));
        }
        return relatedWorkUnits;
    }

    @Override
    public boolean belongsToAnyWorkUnit(Collection<WorkUnit> workUnits) {
        boolean result = false;
        if (workUnits != null) {
            result = getRelatedWorkUnits().stream().anyMatch(workUnits::contains);
        }
        return result;
    }

    /**
     *  After Sanger closed Harwel took over the sanger projects
     *  this methode will allow Harwell to update WTSI projects
     */
    @Override
    public boolean belongsToWTSIWorkUnit(Collection<WorkUnit> workUnits) {

        boolean result = false;
        if (workUnits != null) {
            result = workUnits.stream().anyMatch(x -> x.getName().equals("WTSI")) &&
                    getRelatedWorkUnits().stream().anyMatch(x -> x.getName().equals("Harwell"));
        }
        return result;



    }

    @Override
    public List<String> getRelatedRolesNames() {
        List<String> relatedRolesNames = new ArrayList<>();
        roleWorkUnits.forEach(x -> relatedRolesNames.add(x.getRole().getName()));
        roleConsortia.forEach(x -> relatedRolesNames.add(x.getRole().getName()));
        return relatedRolesNames;
    }

    @Override
    public FluentPersonRoleWorkUnitList getFluentRoleWorkUnits() {
        return new FluentPersonRoleWorkUnitList(roleWorkUnits);
    }

    @Override
    public FluentPersonRoleConsortiumList getFluentRoleConsortia() {
        return new FluentPersonRoleConsortiumList(roleConsortia);
    }

    @Override
    public boolean managesAnyWorkUnit(Collection<WorkUnit> workUnits) {
        return new FluentPersonRoleWorkUnitList(roleWorkUnits)
                .whereUserHasRole("manager")
                .getWorkUnits().stream()
                .anyMatch(workUnits::contains);
    }

    @Override
    public boolean managesAnyConsortia(Collection<Consortium> consortia) {
        return getFluentRoleConsortia()
                .whereUserHasRole("manager")
                .getConsortia().stream()
                .anyMatch(consortia::contains);
    }

    @Override
    public FluentPersonRoleConsortiumList whereUserHasRole(String role) {
        return getFluentRoleConsortia().whereUserHasRole(role);
    }

    @Override
    public boolean isUserByKey(String key) {
        boolean result = false;
        String configuredLogin = null;
        if (key.equals(CDA_USER_KEY)) {
            configuredLogin = CDA_USER_VALUE;
        } else if (key.equals(DCC_USER_KEY)) {
            configuredLogin = DCC_USER_VALUE;
        }

        if (configuredLogin != null) {
            result = person.getEmail().equals(configuredLogin);
        }

        return result;
    }

    public List<WorkUnit> getRelatedWorkUnitsByUserMail(String email) {
        List<WorkUnit> workUnits = new ArrayList<>();
        Person person = personRepository.findPersonByEmail(email);
        if (person != null) {
            Set<PersonRoleWorkUnit> personRoleWorkUnits = person.getPersonRolesWorkUnits();
            if (personRoleWorkUnits != null) {
                personRoleWorkUnits.forEach(x -> workUnits.add(x.getWorkUnit()));
            }
        }
        return workUnits;
    }

    public SystemSubject buildSystemSubjectByPerson(Person person) {
        loadPersonInformation(person.getAuthId());
        return this;
    }

}