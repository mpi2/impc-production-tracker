package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.project.esCellQc.EsCellQcComment;
import org.gentar.biology.project.esCellQc.EsCellQcCommentService;
import org.springframework.stereotype.Component;

@Component
class EsCellQcCommentMapper implements Mapper<EsCellQcComment, String> {

    private final EsCellQcCommentService esCellQcCommentService;

    public EsCellQcCommentMapper(EsCellQcCommentService esCellQcCommentService)
    {
        this.esCellQcCommentService = esCellQcCommentService;
    }

    @Override
    public String toDto(EsCellQcComment entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    @Override
    public EsCellQcComment toEntity(String name)
    {
        return esCellQcCommentService.getEsCellQcCommentByName(name);
    }

}
