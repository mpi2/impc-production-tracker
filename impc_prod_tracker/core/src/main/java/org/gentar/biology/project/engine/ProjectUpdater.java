package org.gentar.biology.project.engine;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectRepository;
import org.gentar.biology.project.assignment.AssignmentStatusUpdater;
import org.gentar.biology.project.summary_status.ProjectSummaryStatusUpdater;
import org.springframework.stereotype.Component;

@Component
public class ProjectUpdater
{
    private HistoryService<Project> historyService;
    private ProjectRepository projectRepository;
    private AssignmentStatusUpdater assignmentStatusUpdater;
    private ProjectSummaryStatusUpdater projectSummaryStatusUpdater;

    public ProjectUpdater(
        HistoryService<Project> historyService,
        ProjectRepository projectRepository,
        AssignmentStatusUpdater assignmentStatusUpdater,
        ProjectSummaryStatusUpdater projectSummaryStatusUpdater)
    {
        this.historyService = historyService;
        this.projectRepository = projectRepository;
        this.assignmentStatusUpdater = assignmentStatusUpdater;
        this.projectSummaryStatusUpdater = projectSummaryStatusUpdater;
    }

    public History updateProject(Project originalProject, Project newProject)
    {
        validatePermissions(newProject);
        History history =
            historyService.detectTrackOfChanges(originalProject, newProject, originalProject.getId());
        saveChanges(newProject);
        saveTrackOfChanges(history);
        return history;
    }

    private void validatePermissions(Project newProject)
    {
        //TODO: add validations of permission
    }

    /**
     * This method is to be called after a plan is updated in the system.
     * Things that may need to be updated in the project because a change in one of its plans:
     *  - Assignment Status.
     *  - Assignment Statuses in other projects conflicting with the project (having same intention).
     *  - Summary status.
     * @param project Project to be checked.
     */
    public void updateProjectWhenPlanUpdated(Project project)
    {
        assignmentStatusUpdater.inactivateOrActivateProjectIfNeeded(project);
        assignmentStatusUpdater.updateConflictingProjects(project);
        projectSummaryStatusUpdater.calculateSummaryStatus(project);
    }

    /**
     * Save the changes into the database for the specific project.
     * @param project project being updated.
     */
    private void saveChanges(Project project)
    {
        projectRepository.save(project);
    }

    /**
     * Stores the track of the changes.
     * @param history
     */
    private void saveTrackOfChanges(History history)
    {
        historyService.saveTrackOfChanges(history);
    }
}
