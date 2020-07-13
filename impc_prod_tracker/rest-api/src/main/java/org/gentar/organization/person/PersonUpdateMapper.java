package org.gentar.organization.person;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PersonUpdateMapper implements Mapper<Person, PersonUpdateDTO>
{
    private final PersonCommonMapper personCommonMapper;

    public PersonUpdateMapper(PersonCommonMapper personCommonMapper)
    {
        this.personCommonMapper = personCommonMapper;
    }

    @Override
    public PersonUpdateDTO toDto(Person entity)
    {
        return null;
    }

    @Override
    public Person toEntity(PersonUpdateDTO personUpdateDTO)
    {
        Person person = new Person();
        if (personUpdateDTO.getPersonCommonDTO() != null)
        {
            person = personCommonMapper.toEntity(personUpdateDTO.getPersonCommonDTO());
        }
        return person;
    }
}
