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
package org.gentar.organization.person.associations;

import lombok.Getter;
import org.gentar.organization.work_unit.WorkUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FluentPersonRoleWorkUnitList
{
    private final List<PersonRoleWorkUnit> personRoleWorkUnits;

    @Getter
    private List<PersonRoleWorkUnit> personRoleWorkUnitsFiltered;

    public FluentPersonRoleWorkUnitList(List<PersonRoleWorkUnit> personRoleWorkUnits)
    {
        if (personRoleWorkUnits != null)
        {
            this.personRoleWorkUnits = personRoleWorkUnits;
            this.personRoleWorkUnitsFiltered = personRoleWorkUnits;
        }
        else
        {
            this.personRoleWorkUnits = new ArrayList<>();
            this.personRoleWorkUnitsFiltered = new ArrayList<>();
        }

    }

    public FluentPersonRoleWorkUnitList clearFilters()
    {
        personRoleWorkUnitsFiltered = personRoleWorkUnits;
        return this;
    }

    public FluentPersonRoleWorkUnitList whereUserHasRole(String roleName)
    {
        personRoleWorkUnitsFiltered = personRoleWorkUnitsFiltered.stream()
            .filter(x -> roleName.equalsIgnoreCase(x.getRole().getName()))
            .collect(Collectors.toList());
        return this;
    }

    public List<WorkUnit> getWorkUnits()
    {
        List<WorkUnit> workUnits = new ArrayList<>();
        if (personRoleWorkUnitsFiltered != null)
        {
            workUnits = personRoleWorkUnitsFiltered.stream()
                .map(PersonRoleWorkUnit::getWorkUnit)
                .collect(Collectors.toList());
        }
        return workUnits;
    }

    public List<String> toWorkUnitsNames()
    {
        return getWorkUnits().stream()
            .map(WorkUnit::getName).collect(Collectors.toList());
    }
}
