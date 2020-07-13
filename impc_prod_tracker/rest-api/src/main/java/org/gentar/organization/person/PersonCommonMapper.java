package org.gentar.organization.person;

import org.gentar.Mapper;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PersonCommonMapper implements Mapper<Person, PersonCommonDTO>
{
    private final PersonRoleWorkUnitMapper personRoleWorkUnitMapper;
    private final PersonRoleConsortiumMapper personRoleConsortiumMapper;

    public PersonCommonMapper(
        PersonRoleWorkUnitMapper personRoleWorkUnitMapper,
        PersonRoleConsortiumMapper personRoleConsortiumMapper)
    {
        this.personRoleWorkUnitMapper = personRoleWorkUnitMapper;
        this.personRoleConsortiumMapper = personRoleConsortiumMapper;
    }

    @Override
    public PersonCommonDTO toDto(Person person)
    {
        PersonCommonDTO personCommonDTO = new PersonCommonDTO();
        personCommonDTO.setName(person.getName());
        personCommonDTO.setContactable(person.getContactable());
        personCommonDTO.setEbiAdmin(person.getEbiAdmin());
        personCommonDTO.setRolesWorkUnitsDtos(
            personRoleWorkUnitMapper.toDtos(person.getPersonRolesWorkUnits()));
        personCommonDTO.setRolesConsortiaDtos(
            personRoleConsortiumMapper.toDtos(person.getPersonRolesConsortia()));
        return personCommonDTO;
    }

    @Override
    public Person toEntity(PersonCommonDTO personCommonDTO)
    {
        Person person = new Person();
        person.setName(personCommonDTO.getName());
        person.setIsActive(true);
        person.setEbiAdmin(personCommonDTO.getEbiAdmin());
        person.setContactable(personCommonDTO.getContactable());
        setRolesWorkUnits(person, personCommonDTO);
        setRolesConsortia(person, personCommonDTO);
        return person;
    }

    private void setRolesWorkUnits(Person person, PersonCommonDTO personCommonDTO)
    {
        List<PersonRoleWorkUnitDTO> roleWorkUnitDTOS = personCommonDTO.getRolesWorkUnitsDtos();
        Set<PersonRoleWorkUnit> roleWorkUnitDTOSet =
            new HashSet<>(personRoleWorkUnitMapper.toEntities(roleWorkUnitDTOS));
        roleWorkUnitDTOSet.forEach(x -> x.setPerson(person));
        person.setPersonRolesWorkUnits(roleWorkUnitDTOSet);
    }

    private void setRolesConsortia(Person person, PersonCommonDTO personCommonDTO)
    {
        List<PersonRoleConsortiumDTO> roleConsortiaDTOs = personCommonDTO.getRolesConsortiaDtos();
        Set<PersonRoleConsortium> roleConsortiumSet =
            new HashSet<>(personRoleConsortiumMapper.toEntities(roleConsortiaDTOs));
        roleConsortiumSet.forEach(x -> x.setPerson(person));
        person.setPersonRolesConsortia(roleConsortiumSet);
    }
}
