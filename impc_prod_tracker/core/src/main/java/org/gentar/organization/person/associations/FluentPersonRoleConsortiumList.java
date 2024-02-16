package org.gentar.organization.person.associations;

import lombok.Getter;
import org.gentar.organization.consortium.Consortium;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FluentPersonRoleConsortiumList
{
    private final List<PersonRoleConsortium> personRoleConsortia;
    @Getter
    private List<PersonRoleConsortium> personRoleConsortiaFiltered;

    public FluentPersonRoleConsortiumList(List<PersonRoleConsortium> personRoleConsortia)
    {
        if (personRoleConsortia != null)
        {
            this.personRoleConsortia = personRoleConsortia;
            this.personRoleConsortiaFiltered = personRoleConsortia;
        }
        else
        {
            this.personRoleConsortia = new ArrayList<>();
            this.personRoleConsortiaFiltered = new ArrayList<>();
        }
    }

    public FluentPersonRoleConsortiumList clearFilters()
    {
        personRoleConsortiaFiltered = personRoleConsortia;
        return this;
    }

    public FluentPersonRoleConsortiumList whereUserHasRole(String roleName)
    {
        personRoleConsortiaFiltered = personRoleConsortiaFiltered.stream()
            .filter(x -> roleName.equalsIgnoreCase(x.getRole().getName()))
            .collect(Collectors.toList());
        return this;
    }

    public List<Consortium> getConsortia()
    {
        List<Consortium> consortia = new ArrayList<>();
        if (personRoleConsortiaFiltered != null)
        {
            consortia = personRoleConsortiaFiltered.stream()
                .map(PersonRoleConsortium::getConsortium)
                .collect(Collectors.toList());
        }
        return consortia;
    }

    public List<String> toConsortiaNames()
    {
        return getConsortia().stream()
            .map(Consortium::getName).collect(Collectors.toList());
    }

}
