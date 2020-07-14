package org.gentar.organization.person;

import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PersonRequestProcessor
{
    private final PersonService personService;
    private final PersonUpdateMapper personUpdateMapper;

    public PersonRequestProcessor(PersonService personService, PersonUpdateMapper personUpdateMapper)
    {
        this.personService = personService;
        this.personUpdateMapper = personUpdateMapper;
    }

    public Person getOwnPersonToUpdate(Person loggedPerson, PersonUpdateDTO personUpdateDTO)
    {
        Person mappedPerson = personUpdateMapper.toEntity(personUpdateDTO);
        loggedPerson.setName(mappedPerson.getName());
        loggedPerson.setContactable(mappedPerson.getContactable());
        loggedPerson.setPassword(mappedPerson.getPassword());
        return loggedPerson;
    }

    /**
     * Get the person object to update when a manager is going to update a user they manage.
     * @param email Email of the managed user.
     * @param personUpdateDTO Data to update
     * @return Person object with the new data.
     */
    public Person getPersonManagedToUpdate(String email, PersonUpdateDTO personUpdateDTO)
    {
        Person personToUpdate = personService.getPersonByEmail(email);
        Person mappedPerson = personUpdateMapper.toEntity(personUpdateDTO);
        personToUpdate.setName(mappedPerson.getName());
        personToUpdate.setContactable(mappedPerson.getContactable());
        personToUpdate.setIsActive(mappedPerson.getIsActive());
        setRolesWorkUnits(personToUpdate, mappedPerson);
        setRolesConsortia(personToUpdate, mappedPerson);
        return personToUpdate;
    }

    private void setRolesWorkUnits(Person personToUpdate, Person mappedPerson)
    {
        Set<PersonRoleWorkUnit> roleWorkUnits = mappedPerson.getPersonRolesWorkUnits();
        if (roleWorkUnits != null)
        {
            roleWorkUnits.forEach(x -> x.setPerson(personToUpdate));
        }
        personToUpdate.setPersonRolesWorkUnits(roleWorkUnits);
    }

    private void setRolesConsortia(Person personToUpdate, Person mappedPerson)
    {
        Set<PersonRoleConsortium> roleConsortia = mappedPerson.getPersonRolesConsortia();
        if (roleConsortia != null)
        {
            roleConsortia.forEach(x -> x.setPerson(personToUpdate));
        }
        personToUpdate.setPersonRolesConsortia(roleConsortia);
    }
}
