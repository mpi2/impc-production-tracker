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
package org.gentar.web.mapping.assignment_status;

import org.gentar.service.biology.assignment_status.AssignmentStatusService;
import org.springframework.stereotype.Component;
import org.gentar.biology.assignment_status.AssignmentStatus;

@Component
public class AssignmentStatusMapper
{
    private AssignmentStatusService assignmentStatusService;

    public AssignmentStatusMapper(AssignmentStatusService assignmentStatusService)
    {
        this.assignmentStatusService = assignmentStatusService;
    }

    public AssignmentStatus toEntity(String name)
    {
        return assignmentStatusService.getAssignmentStatusByName(name);
    }
}
