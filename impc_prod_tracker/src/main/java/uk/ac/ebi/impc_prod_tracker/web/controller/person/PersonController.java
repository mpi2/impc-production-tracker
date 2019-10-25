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
package uk.ac.ebi.impc_prod_tracker.web.controller.person;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.service.organization.PersonService;
import uk.ac.ebi.impc_prod_tracker.web.dto.person.PersonCreationDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.person.PersonDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.person.PersonMapper;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PersonController
{
    private PersonService personService;
    private PersonMapper personMapper;

    public PersonController(PersonService personService, PersonMapper personMapper)
    {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    /**
     * Return a list of users that the logged user can see. If the logged user is a manager, they
     * can see all the users that can be managed by them. If the logged user is an admin they can
     * see all the users in the system.
     * @return List of {@link PersonDTO} with information about the people visible for the current
     * logged user.
     */
    @GetMapping(value = {"/people"})
    public List<PersonDTO> getAllPeople()
    {
        return personMapper.toDtos(personService.getAllPeople());
    }

    /**
     * Creates a person in the system.
     * @param personCreationDTO Request with data of the user to be created.
     * @return {@link Person} entity created
     */
    @PostMapping(value = {"/people"})
    @PreAuthorize("hasPermission(null, 'CREATE_USER')")
    public PersonDTO createPerson(PersonCreationDTO personCreationDTO)
    {
        Person personToBeCreated = personMapper.personCreationDTOtoEntity(personCreationDTO);
        return personMapper.toDto(personService.createPerson(personToBeCreated));
    }
}
