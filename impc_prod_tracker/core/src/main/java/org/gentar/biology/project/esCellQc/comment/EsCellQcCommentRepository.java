package org.gentar.biology.project.esCellQc.comment;

import org.springframework.data.repository.CrudRepository;

public interface EsCellQcCommentRepository extends CrudRepository<EsCellQcComment, Long>
{
    EsCellQcComment findFirstByNameIgnoreCase(String name);
}
