package org.gentar.biology.project.completionNote;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class ProjectCompletionNoteServiceImpl implements ProjectCompletionNoteService
{
    private final ProjectCompletionNoteRepository projectCompletionNoteRepository;
    private static final String NOTE_NOT_FOUND_ERROR = "Completion note '%s' does not exist.";

    public ProjectCompletionNoteServiceImpl(ProjectCompletionNoteRepository projectCompletionNoteRepository)
    {
        this.projectCompletionNoteRepository = projectCompletionNoteRepository;
    }

    @Override
    public ProjectCompletionNote getProjectCompletionNoteByNote(String name)
    {
        return projectCompletionNoteRepository.findFirstByNoteIgnoreCase(name);
    }

    @Override
    public ProjectCompletionNote getProjectCompletionNoteByNoteIfNull(String name)
    {
        ProjectCompletionNote projectCompletionNote = getProjectCompletionNoteByNote(name);
        if (projectCompletionNote == null)
        {
            throw new UserOperationFailedException(String.format(NOTE_NOT_FOUND_ERROR, name));
        }
        return projectCompletionNote;
    }
}
