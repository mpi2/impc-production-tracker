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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.subject.SubjectRetriever;
import org.gentar.security.abac.subject.SystemSubject;
import org.springframework.stereotype.Component;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.authentication.AAPService;
import org.gentar.security.permissions.PermissionService;
import org.gentar.organization.management.ManagementService;

import java.util.List;

/**
 * Service to manage the creation of an user in the system.
 */

@Component
public class PersonServiceImpl implements PersonService
{
    private PersonRepository personRepository;
    private AAPService aapService;
    private SubjectRetriever subjectRetriever;
    private ManagementService managementService;
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public static final String PERSON_ALREADY_EXISTS_ERROR =
        "The user with email [%s] already exists in the system.";

    public PersonServiceImpl(
        PersonRepository personRepository,
        AAPService aapService,
        SubjectRetriever subjectRetriever,
        ManagementService managementService,
        ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.personRepository = personRepository;
        this.aapService = aapService;
        this.subjectRetriever = subjectRetriever;
        this.managementService = managementService;
        this.policyEnforcement = policyEnforcement;
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
                people = managementService.getManagedPeople(systemSubject);
            }
        }

        return people;
    }

    @Override
    public Person getLoggedPerson()
    {
        Person person = null;
        SystemSubject systemSubject = subjectRetriever.getSubject();
        if (systemSubject != null)
        {
            person = systemSubject.getPerson();
        }

        return person;
    }

    @Override
    public Person getPersonByEmail(String email)
    {
        return personRepository.findPersonByEmail(email);
    }

    @Override
    public Person createPerson(Person person, String token) throws JsonProcessingException
    {
        validatePersonNotExists(person);
        String authId = aapService.createUser(person, token);
        person.setAuthId(authId);
        personRepository.save(person);
        return person;
    }

    @Override
    public Person updateManagedPerson(Person person, String token)
    {
        validatePermissions(PermissionService.UPDATE_USER, person);
        personRepository.save(person);
        return person;
    }

    private void validatePermissions(String updateUser, Person person)
    {
        if (!policyEnforcement.hasPermission(person, updateUser))
        {
            throw new UserOperationFailedException(
                "You don't have permissions to execute the action on this user.");
        }
    }

    private void validatePersonNotExists(Person person)
    {
        String email = person.getEmail();
        if (personRepository.findPersonByEmail(email) != null)
        {
            throw new UserOperationFailedException(String.format(PERSON_ALREADY_EXISTS_ERROR, email));
        }
    }

}
