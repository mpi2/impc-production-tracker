package org.gentar.biology.project.assignment;

import org.gentar.biology.project.Project;

/**
 * Utility class to get information about the assignment status of a project.
 */
public class AssignmentStatusChecker
{
    /**
     * Checks if a project has the assignment status specified by the assignmentStatusName.
     * @param project Project to be checked.
     * @param assignmentStatusName The name of the assignment status we want to validate.
     * @return If the project has the assignment status or not.
     */
    public static boolean projectHasStatusByName(Project project, String assignmentStatusName)
    {
        boolean result = false;
        AssignmentStatus assignmentStatus = project.getAssignmentStatus();
        if (assignmentStatus != null)
        {
            result = assignmentStatusName.equals(assignmentStatus.getName());
        }
        return result;
    }

    /**
     * Checks if a project has the "Inactive" assignment Status. This is a specific use of
     * projectHasStatusByName(Project project, String assignmentStatusName).
     * @param project Project to be checked.
     * @return If the project has the assignment status "Inactive" or not.
     */
    public static boolean projectIsInactive(Project project)
    {
        return projectHasStatusByName(project, AssignmentStatusNames.INACTIVE_STATUS_NAME);
    }
}
