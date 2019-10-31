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
package uk.ac.ebi.impc_prod_tracker.common.fluent;

import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_work_unit.PersonRoleWorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FluentPersonRoleWorkUnitList
{
    private List<PersonRoleWorkUnit> personRoleWorkUnits;

    private List<PersonRoleWorkUnit> personRoleWorkUnitsFiltered;

    public List<PersonRoleWorkUnit> getPersonRoleWorkUnitsFiltered()
    {
        return personRoleWorkUnitsFiltered;
    }

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
            .map(x -> x.getName()).collect(Collectors.toList());
    }
}
