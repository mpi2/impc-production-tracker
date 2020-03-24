package org.gentar.biology.project.engine;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectRepository;
import org.springframework.stereotype.Component;

@Component
public class ProjectUpdater
{
    private HistoryService<Project> historyService;
    private ProjectRepository projectRepository;

    public ProjectUpdater(HistoryService<Project> historyService, ProjectRepository projectRepository)
    {
        this.historyService = historyService;
        this.projectRepository = projectRepository;
    }

    public History updateProject(Project originalProject, Project newProject)
    {
        validatePermissions(newProject);
        changeAssignmentStatusIfNeeded(newProject);
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

    private void changeAssignmentStatusIfNeeded(Project newProject)
    {
    }

    /**
     * Save the changes into the database for the specific project.
     * @param project project being updated.
     */
    private void saveChanges(Project project)
    {
        System.out.println("Saving changes");
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
