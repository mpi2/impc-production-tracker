package org.gentar.biology.project.engine;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectRepository;
import org.gentar.biology.project.assignment.AssignmentStatusUpdater;
import org.springframework.stereotype.Component;

@Component
public class ProjectUpdater
{
    private HistoryService<Project> historyService;
    private ProjectRepository projectRepository;
    private AssignmentStatusUpdater assignmentStatusUpdater;

    public ProjectUpdater(
        HistoryService<Project> historyService,
        ProjectRepository projectRepository,
        AssignmentStatusUpdater assignmentStatusUpdater)
    {
        this.historyService = historyService;
        this.projectRepository = projectRepository;
        this.assignmentStatusUpdater = assignmentStatusUpdater;
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

    public void changeAssignmentStatusIfNeeded(Project project)
    {
        assignmentStatusUpdater.inactivateOrActivateProjectIfNeeded(project);
        assignmentStatusUpdater.updateConflictingProjects(project);
    }

    public void updateConflictingProjects(Project project)
    {
        assignmentStatusUpdater.updateConflictingProjects(project);
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
