package org.gentar.biology.project.assignment;

import org.gentar.biology.project.Project;
import org.springframework.stereotype.Component;

/**
 * Class in charge of setting and updating the Assignment Status for a project depending on its
 * conditions.
 */
@Component
public class AssignmentStatusUpdater
{
    private ConflictsChecker conflictsChecker;

    public AssignmentStatusUpdater(ConflictsChecker conflictsChecker)
    {
        this.conflictsChecker = conflictsChecker;
    }

    public AssignmentStatus checkConflict(Project project)
    {
        return conflictsChecker.checkConflict(project);
    }
}
