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
package uk.ac.ebi.impc_prod_tracker.service.organization;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.SystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.conf.security.constants.PersonManagementConstants;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.InstituteRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.RoleRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnitRepository;
import uk.ac.ebi.impc_prod_tracker.domain.login.UserRegisterRequest;

import java.util.List;

/**
 * Service to manage the creation of an user in the system.
 */

@Component
public class PersonServiceImpl implements PersonService
{
    private PersonRepository personRepository;
    private RoleRepository roleRepository;
    private WorkUnitRepository workUnitRepository;
    private InstituteRepository instituteRepository;
    private RestTemplate restTemplate;
    private SubjectRetriever subjectRetriever;

    public static final String PERSON_ALREADY_EXISTS_ERROR =
        "The user with email [%s] already exists in the system.";
    public static final String PERSON_ALREADY_IN_AAP_ERROR = "The user [%s] already exists in the "
        + "Authentication System. Please try with another user name.";
    public static final String WORK_UNIT_NOT_EXIST_IN_THE_SYSTEM
        = "Work Unit [%s] does not exist in the system.";
    public static final String INSTITUTE_NOT_EXIST_IN_THE_SYSTEM
        = "Institute [%s] does not exist in the system.";
    public static final String ROLE_NOT_EXIST_IN_THE_SYSTEM = "Role [%s] does not exist in the system.";

    public PersonServiceImpl(
        PersonRepository personRepository,
        RoleRepository roleRepository,
        WorkUnitRepository workUnitRepository,
        InstituteRepository instituteRepository,
        RestTemplate restTemplate,
        SubjectRetriever subjectRetriever)
    {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.workUnitRepository = workUnitRepository;
        this.instituteRepository = instituteRepository;
        this.restTemplate = restTemplate;
        this.subjectRetriever = subjectRetriever;
    }

    public List<Person> getAllPeople()
    {
        List<Person> people = null;
        SystemSubject systemSubject = subjectRetriever.getSubject();
        if (systemSubject != null)
        {
            if (systemSubject.isAdmin())
            {
                people = personRepository.findAll();
            }
            else
            {
                people = null;
            }
        }

        return people;
    }

    @Override
    public Person createPerson(Person person)
    {
        return null;
    }

    /**
     *
     * @param userRegisterRequest Information with the user to be created.
     * @return {@link Person} object with the created person.
     */
    public Person createPerson(UserRegisterRequest userRegisterRequest)
    {
        String email = userRegisterRequest.getEmail();

        Person person = personRepository.findPersonByEmail(email);
        if (person == null)
        {
            Role role = getRoleFromRequest(userRegisterRequest);
            WorkUnit workUnit = getWorkUnitFromRequest(userRegisterRequest);

            person = new Person(email);
            person.setIsActive(true);
            //TODO: Adjust with several work units and consortia.
//            person.setRole(role);
//            person.setWorkUnit(workUnit);
            String authId = createUserInAuthenticationSystem(userRegisterRequest);
            person.setAuthId(authId);
            personRepository.save(person);

            for (String instituteName : userRegisterRequest.getInstituteName())
            {
                Institute institute = instituteRepository.findInstituteByName(instituteName);
                if (!instituteName.isEmpty() && institute == null)
                {
                    throw new UserOperationFailedException(
                        String.format(INSTITUTE_NOT_EXIST_IN_THE_SYSTEM, instituteName));
                }
                //TODO: Adjust with several work units and consortia.
                instituteRepository.save(institute);
            }
            personRepository.save(person);
        }
        else
        {
            throw new UserOperationFailedException(String.format(PERSON_ALREADY_EXISTS_ERROR, email));
        }

        return person;
    }


    private WorkUnit getWorkUnitFromRequest(UserRegisterRequest userRegisterRequest)
    {
        String workUnitInRequest = userRegisterRequest.getWorkUnitName();
        WorkUnit workUnit = workUnitRepository.findWorkUnitByIlarCode(workUnitInRequest);
        if (!workUnitInRequest.isEmpty() && workUnit == null)
        {
            throw new UserOperationFailedException(
                String.format(WORK_UNIT_NOT_EXIST_IN_THE_SYSTEM, workUnitInRequest));
        }
        return workUnit;
    }

    private Role getRoleFromRequest(UserRegisterRequest userRegisterRequest)
    {
        String roleInRequest = userRegisterRequest.getRoleName();
        Role role = roleRepository.findRoleByName(roleInRequest);
        if (!roleInRequest.isEmpty() && role == null)
        {
            throw new UserOperationFailedException(
                String.format(ROLE_NOT_EXIST_IN_THE_SYSTEM, roleInRequest));
        }
        return role;
    }

    /**
     * Creates a user in the AAP
     * @param userRegisterRequest
     * @return The UID generated for the system.
     */
    private String createUserInAuthenticationSystem(UserRegisterRequest userRegisterRequest)
    {
        LocalAccountInfo localAccountInfo =
            new LocalAccountInfo(
                userRegisterRequest.getName(),
                userRegisterRequest.getPassword(),
                userRegisterRequest.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LocalAccountInfo> requestEntity = new HttpEntity<>(localAccountInfo, headers);
        ResponseEntity<String> response;
        try
        {
            response =
                restTemplate.postForEntity(
                    PersonManagementConstants.LOCAL_AUTHENTICATION_URL,
                    requestEntity,
                    String.class);
        }
        catch(HttpClientErrorException e)
        {
            String message = e.getMessage();
            if (e.getStatusCode().equals(HttpStatus.CONFLICT))
            {
                message = String.format(PERSON_ALREADY_IN_AAP_ERROR, userRegisterRequest.getName());
            }
            throw new UserOperationFailedException(message);
        }
        return response.getBody();
    }

    /**
     * Internal class to represent the payload needed to create the user in the AAP local account.
     * The "name" field is just for display purposes and can be changed in any moment. "username"
     * is the one is used to log into the system, so it cannot be changed and will be assigned with
     * the email, meaning the way to log into the system is using the email and the password.
     */
    static class LocalAccountInfo
    {
        @JsonProperty("userName")
        String userName;

        @JsonProperty("name")
        String name;

        @JsonProperty("password")
        String password;

        @JsonProperty("email")
        String email;

        LocalAccountInfo(String name, String password, String email)
        {
            this.name = name;
            this.password = password;
            this.email = email;
            this.userName = email;
        }
    }

}
