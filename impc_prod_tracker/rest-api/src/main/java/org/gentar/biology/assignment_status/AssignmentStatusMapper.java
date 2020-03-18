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
package org.gentar.biology.assignment_status;

import org.gentar.Mapper;
import org.gentar.biology.project.assignment_status.AssignmentStatusService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.assignment_status.AssignmentStatus;


@Component
public class AssignmentStatusMapper implements Mapper<AssignmentStatus, String>
{
    private AssignmentStatusService assignmentStatusService;

    private static final String ASSIGNMENT_STATUS_NOT_FOUND_ERROR = "Assignment status '%s' does not exist.";

    public AssignmentStatusMapper(AssignmentStatusService assignmentStatusService)
    {
        this.assignmentStatusService = assignmentStatusService;
    }

    @Override
    public String toDto(AssignmentStatus entity) {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    public AssignmentStatus toEntity(String name)
    {
        AssignmentStatus assignmentStatus = assignmentStatusService.getAssignmentStatusByName(name);

        if (assignmentStatus == null)
        {
            throw new UserOperationFailedException(String.format(ASSIGNMENT_STATUS_NOT_FOUND_ERROR, assignmentStatus));
        }

        return assignmentStatus;
    }
}
