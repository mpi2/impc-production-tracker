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
import org.gentar.security.AuthorizationHeaderReader;
import org.gentar.security.authentication.AAPService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PersonController {
    private final PersonService personService;
    private final PersonMapper personMapper;
    private final PersonRequestProcessor personRequestProcessor;
    private final AuthorizationHeaderReader authorizationHeaderReader;
    private final AAPService aapService;

    public PersonController(
            PersonService personService,
            PersonMapper personMapper,
            PersonRequestProcessor personRequestProcessor,
            AuthorizationHeaderReader authorizationHeaderReader,
            AAPService aapService) {
        this.personService = personService;
        this.personMapper = personMapper;
        this.personRequestProcessor = personRequestProcessor;
        this.authorizationHeaderReader = authorizationHeaderReader;
        this.aapService = aapService;
    }

    /**
     * Return a list of users that the logged user can see. If the logged user is a manager, they
     * can see all the users that can be managed by them. If the logged user is an admin they can
     * see all the users in the system.
     *
     * @return List of {@link PersonDTO} with information about the people visible for the current
     * logged user.
     */
    @GetMapping
    public List<PersonDTO> getAllPeople() {
        return personMapper.toDtos(personService.getAllPeople());
    }

    @GetMapping(value = {"/{email}"})
    public PersonDTO findByEmail(@PathVariable("email") String email) {
        Person person = personService.getPersonByEmail(email);
        if (person == null) {
            throw new UserOperationFailedException(
                    "User with email " + email + " not found.");
        }
        return personMapper.toDto(person);
    }

    @GetMapping(value = {"/currentPerson"})
    public PersonDTO getCurrentPerson() {
        return personMapper.toDto(personService.getLoggedPerson());
    }


    /**
     * Creates a person in the system.
     * @param personCreationDTO Request with data of the user to be created.
     * @return {@link Person} entity created
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'MANAGE_USERS')")
    public PersonDTO createPerson(
            @RequestBody PersonCreationDTO personCreationDTO, HttpServletRequest request) throws JsonProcessingException
    {
        String token = authorizationHeaderReader.getAuthorizationToken(request);
        Person personToBeCreated = personMapper.personCreationDTOtoEntity(personCreationDTO);
        return personMapper.toDto(personService.createPerson(personToBeCreated, token));
    }


    /**
     * Allows to change information for the current user logged into the system.
     *
     * @param personUpdateDTO Information to update.
     * @return Updated person.
     */
    @PutMapping
    public PersonDTO updateOwnPerson(@RequestBody PersonUpdateDTO personUpdateDTO)
            throws JsonProcessingException {
        Person personToBeUpdated =
                personRequestProcessor.getOwnPersonToUpdate(
                        personService.getLoggedPerson(), personUpdateDTO);
        Person person = personService.updateOwnPerson(personToBeUpdated);
        return personMapper.toDto(person);
    }

    @PutMapping(value = {"/resetPassword"})
    public void resetPerson()
            throws JsonProcessingException {
        String email = personService.getLoggedPerson().getEmail();
        personService.resetPassword(email);

    }


    @PutMapping(value = {"/{email}"})
    public PersonDTO updateManagedPerson(
            @PathVariable("email") String email,
            @RequestBody PersonUpdateDTO personUpdateDTO,
            HttpServletRequest request) {
        String token = authorizationHeaderReader.getAuthorizationToken(request);
        Person personToBeUpdated = personRequestProcessor.getPersonManagedToUpdate(email, personUpdateDTO);
        Person person = personService.updateManagedPerson(personToBeUpdated, token);
        return personMapper.toDto(person);
    }


    @PutMapping(value = {"/update_user_auth_id/{email}/{authId}"})
    public void updateManagedPerson(
            @PathVariable("email") String email,
            @PathVariable("authId") String authId) throws JsonProcessingException {
        Person person = personService.getPersonByEmail(email);
        person.setAuthId(authId);
        personService.updateOwnPerson(person);

    }

    @PostMapping(value = {"/requestPasswordReset"})
    public void requestPasswordReset(@RequestBody String email) throws JsonProcessingException {
        aapService.requestPasswordReset(email);
    }
}