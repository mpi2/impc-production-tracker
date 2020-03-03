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
package org.gentar.organization.management;

import org.gentar.security.abac.subject.SystemSubject;
import org.springframework.stereotype.Component;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.person.Person;
import org.gentar.organization.work_unit.WorkUnit;
import java.util.List;

@Component
public interface ManagementService
{
    List<WorkUnit> getManagedWorkUnits(SystemSubject systemSubject);

    List<Consortium> getManagedConsortia(SystemSubject systemSubject);

    /**
     * Gets the people (with 'general' role) associated to work units or consortia that the user manages.
     * @param systemSubject The user executing the request.
     * @return List of {@link Person} managed by @systemSubject.
     */
    List<Person> getManagedPeople(SystemSubject systemSubject);
}
