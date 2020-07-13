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
import org.gentar.organization.person.Person;
import java.util.List;

/**
 * Functionality to manage people in the system.
 */
public interface PersonService
{
    /**
     * Return a list of users that the logged user can see. If the logged user is a manager, they
     * can see all the users that can be managed by them. If the logged user is an admin they can
     * see all the users in the system.
     * @return List of {@link Person} with information about the people visible for the current
     * logged user.
     */
    List<Person> getAllPeople();

    /**
     * Returns the {@link Person} object representing the current user.
     * @return
     */
    Person getLoggedPerson();

    /**
     * Get a {@link Person} object with the information of the person identified by the given email.
     * @param email The email.
     * @return The person identified by email. Null if not found.
     */
    Person getPersonByEmail(String email);

    /**
     * Saves a person object in the system.
     * @param person {@link Person} object with the person basic information and their associations
     *                             with work units and consortia.
     * @return Created {@link Person} with an id provided by the system.
     */
    Person createPerson(Person person, String token) throws JsonProcessingException;

    /**
     * Updates the information for a person, as long as the current user has permission to do it.
     * @param person New information. Only some fields will be taken into account to the update.
     * @param token JWT token to communicate with AAP in case of need.
     * @return Updated person.
     */
    Person updateManagedPerson(Person person, String token);
}
