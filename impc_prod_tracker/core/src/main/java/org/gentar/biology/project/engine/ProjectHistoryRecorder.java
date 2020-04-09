package org.gentar.biology.project.engine;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.Project_;
import org.gentar.biology.project.assignment.AssignmentStatus_;
import org.springframework.stereotype.Component;

/**
 * Class in charge of interacting with the history service to create records in the history table
 * due to changes in a project.
 */
@Component
public class ProjectHistoryRecorder
{
    private HistoryService<Project> historyService;

    public ProjectHistoryRecorder(HistoryService<Project> historyService)
    {
        this.historyService = historyService;
    }

    public void saveProjectChangesInHistory(Project originalProject, Project newProject)
    {
        History history =
            historyService.detectTrackOfChanges(
                originalProject, newProject, originalProject.getId());
        history = historyService.filterDetailsInNestedEntity(
            history, Project_.ASSIGNMENT_STATUS, AssignmentStatus_.NAME);
        history = historyService.filterDetailsInNestedEntity(
            history, Project_.SUMMARY_STATUS, AssignmentStatus_.NAME);
        historyService.saveTrackOfChanges(history);
    }
}
