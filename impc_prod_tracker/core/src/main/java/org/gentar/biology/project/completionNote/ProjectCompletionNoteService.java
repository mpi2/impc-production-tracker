package org.gentar.biology.project.completionNote;

public interface ProjectCompletionNoteService
{
    ProjectCompletionNote getProjectCompletionNoteByNote(String name);
    ProjectCompletionNote getProjectCompletionNoteByNoteIfNull(String name);
}
