package org.gentar.organization.person;

import org.gentar.organization.person_role_work_unit.PersonRoleWorkUnit;
import org.gentar.organization.role.Role;
import org.gentar.organization.work_unit.WorkUnit;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FluentPersonRoleWorkUnitListTest
{
    @Test
    public void testWhereUserHasRoleEmpty()
    {
        List<PersonRoleWorkUnit> personRoleWorkUnits = new ArrayList<>();
        FluentPersonRoleWorkUnitList fluentPersonRoleWorkUnitList =
            new FluentPersonRoleWorkUnitList(personRoleWorkUnits);
        fluentPersonRoleWorkUnitList = fluentPersonRoleWorkUnitList.whereUserHasRole("");
        assertThat(
            "Should be empty",
            fluentPersonRoleWorkUnitList.getPersonRoleWorkUnitsFiltered(),
            is(Collections.emptyList()));
    }

    @Test
    public void testWhereUserHasRole()
    {
        List<PersonRoleWorkUnit> personRoleWorkUnits = buildPersonRoleWorkUnits();
        FluentPersonRoleWorkUnitList fluentPersonRoleWorkUnitList =
            new FluentPersonRoleWorkUnitList(personRoleWorkUnits);
        fluentPersonRoleWorkUnitList = fluentPersonRoleWorkUnitList
            .whereUserHasRole("role1");

        assertThat(
            "Should not be empty",
            fluentPersonRoleWorkUnitList.getPersonRoleWorkUnitsFiltered().size(),
            is(1));
        PersonRoleWorkUnit personRoleWorkUnit1 =
            fluentPersonRoleWorkUnitList.getPersonRoleWorkUnitsFiltered().get(0);
        assertThat(
            "",
            personRoleWorkUnit1.getPerson().getId(),
            is(1L));
        assertThat(
            "",
            personRoleWorkUnit1.getRole().getName(),
            is("role1"));
        assertThat(
            "",
            personRoleWorkUnit1.getWorkUnit().getName(),
            is("workUnit1"));
    }

    @Test
    public void testWhereUserHasRoleRepeatedFilter()
    {
        List<PersonRoleWorkUnit> personRoleWorkUnits = buildPersonRoleWorkUnits();
        FluentPersonRoleWorkUnitList fluentPersonRoleWorkUnitList =
            new FluentPersonRoleWorkUnitList(personRoleWorkUnits);
        fluentPersonRoleWorkUnitList = fluentPersonRoleWorkUnitList
            .whereUserHasRole("role1").whereUserHasRole("role1");

        assertThat(
            "Should not be empty",
            fluentPersonRoleWorkUnitList.getPersonRoleWorkUnitsFiltered().size(),
            is(1));
        PersonRoleWorkUnit personRoleWorkUnit1 =
            fluentPersonRoleWorkUnitList.getPersonRoleWorkUnitsFiltered().get(0);
        assertThat(
            "",
            personRoleWorkUnit1.getPerson().getId(),
            is(1L));
        assertThat(
            "",
            personRoleWorkUnit1.getRole().getName(),
            is("role1"));
        assertThat(
            "",
            personRoleWorkUnit1.getWorkUnit().getName(),
            is("workUnit1"));
    }

    @Test
    public void testToWorkUnitsNames()
    {
        List<PersonRoleWorkUnit> personRoleWorkUnits = buildPersonRoleWorkUnits();
        FluentPersonRoleWorkUnitList fluentPersonRoleWorkUnitList =
            new FluentPersonRoleWorkUnitList(personRoleWorkUnits);
        List<String> namesList = fluentPersonRoleWorkUnitList.toWorkUnitsNames();
        System.out.println(namesList);
    }

    private List<PersonRoleWorkUnit> buildPersonRoleWorkUnits()
    {
        Person person1 = new Person();
        person1.setId(1L);
        WorkUnit workUnit1 = new WorkUnit();
        workUnit1.setId(1L);
        workUnit1.setName("workUnit1");
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("role1");
        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("role2");
        PersonRoleWorkUnit personRoleWorkUnit1 = new PersonRoleWorkUnit();
        personRoleWorkUnit1.setPerson(person1);
        personRoleWorkUnit1.setRole(role1);
        personRoleWorkUnit1.setWorkUnit(workUnit1);
        PersonRoleWorkUnit personRoleWorkUnit2 = new PersonRoleWorkUnit();
        personRoleWorkUnit2.setPerson(person1);
        personRoleWorkUnit2.setRole(role2);
        personRoleWorkUnit2.setWorkUnit(workUnit1);
        List<PersonRoleWorkUnit> personRoleWorkUnits = new ArrayList<>();
        personRoleWorkUnits.add(personRoleWorkUnit1);
        personRoleWorkUnits.add(personRoleWorkUnit2);
        return personRoleWorkUnits;
    }
}