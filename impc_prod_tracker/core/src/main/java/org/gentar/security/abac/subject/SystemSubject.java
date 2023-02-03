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
package org.gentar.security.abac.subject;

import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.person.associations.FluentPersonRoleConsortiumList;
import org.gentar.organization.person.associations.FluentPersonRoleWorkUnitList;
import org.gentar.organization.person.Person;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import org.gentar.organization.work_unit.WorkUnit;
import java.util.Collection;
import java.util.List;

/**
 * Information a subject in the system should have.
 * @author Mauricio Martinez
 */
public interface SystemSubject
{
    Person getPerson();

    String getLogin();

    String getName();

    String getUserRefId();

    String getEmail();

    List<PersonRoleWorkUnit> getRoleWorkUnits();

    List<PersonRoleConsortium> getRoleConsortia();

    Boolean isAdmin();

    boolean belongsToConsortia(Collection<Consortium> consortia);

    List<WorkUnit> getRelatedWorkUnits();

    boolean belongsToAnyWorkUnit(Collection<WorkUnit> workUnits);

    boolean belongsToWTSIWorkUnit(Collection<WorkUnit> workUnits);

    List<String> getRelatedRolesNames();

    FluentPersonRoleWorkUnitList getFluentRoleWorkUnits();

    FluentPersonRoleConsortiumList getFluentRoleConsortia();

    boolean managesAnyWorkUnit(Collection<WorkUnit> workUnits);

    boolean managesAnyConsortia(Collection<Consortium> consortia);

    FluentPersonRoleConsortiumList whereUserHasRole(String role);

    /**
     * Checks if the subject is the same as the one that is mapped to the key parameter in a
     * property file.
     * @param key The key in properties file that maps to a user in GenTaR.
     * @return True if the current user is the user mapped by the 'key' parameter.
     */
    boolean isUserByKey(String key);
}
