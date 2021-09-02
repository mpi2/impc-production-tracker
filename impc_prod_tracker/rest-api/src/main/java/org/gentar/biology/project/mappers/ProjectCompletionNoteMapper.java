package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.project.completionNote.ProjectCompletionNote;
import org.gentar.biology.project.completionNote.ProjectCompletionNoteService;
import org.springframework.stereotype.Component;

@Component
public class ProjectCompletionNoteMapper implements Mapper<ProjectCompletionNote, String>
{
    private final ProjectCompletionNoteService projectCompletionNoteService;

    public ProjectCompletionNoteMapper(ProjectCompletionNoteService projectCompletionNoteService)
    {
        this.projectCompletionNoteService = projectCompletionNoteService;
    }

    @Override
    public String toDto(ProjectCompletionNote entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getNote();
        }
        return name;
    }

    @Override
    public ProjectCompletionNote toEntity(String note)
    {
        return projectCompletionNoteService.getProjectCompletionNoteByNoteIfNull(note);
    }

}
