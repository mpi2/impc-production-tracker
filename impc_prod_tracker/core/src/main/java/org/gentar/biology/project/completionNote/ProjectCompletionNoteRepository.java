package org.gentar.biology.project.completionNote;

import org.springframework.data.repository.CrudRepository;

public interface ProjectCompletionNoteRepository extends CrudRepository<ProjectCompletionNote, Long>
{
    ProjectCompletionNote findFirstByNoteIgnoreCase(String note);
}
