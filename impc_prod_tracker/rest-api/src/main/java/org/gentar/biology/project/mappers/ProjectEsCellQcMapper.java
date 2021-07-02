package org.gentar.biology.project.mappers;

import org.apache.logging.log4j.util.Strings;
import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.project.esCellQc.EsCellQcComment;
import org.gentar.biology.project.esCellQc.ProjectEsCellQc;
import org.gentar.biology.project.project_es_cell_qc.ProjectEsCellQcDTO;
import org.springframework.stereotype.Component;

@Component
public class ProjectEsCellQcMapper implements Mapper<ProjectEsCellQc, ProjectEsCellQcDTO> {

    private final EntityMapper entityMapper;
    private final EsCellQcCommentMapper esCellQcCommentMapper;

    public ProjectEsCellQcMapper(EntityMapper entityMapper,
                                 EsCellQcCommentMapper esCellQcCommentMapper) {
        this.entityMapper = entityMapper;
        this.esCellQcCommentMapper = esCellQcCommentMapper;
    }

    @Override
    public ProjectEsCellQcDTO toDto(ProjectEsCellQc projectEsCellQc) {
        ProjectEsCellQcDTO projectEsCellQcDTO = null;
        if (projectEsCellQc != null)
        {
            projectEsCellQcDTO = entityMapper.toTarget(projectEsCellQc, ProjectEsCellQcDTO.class);
            if (projectEsCellQc.getEsCellQcComment() != null)
            {
                projectEsCellQcDTO.setEsCellQcComment(projectEsCellQc.getEsCellQcComment().getName());
            }

        }
        return projectEsCellQcDTO;

    }

    @Override
    public ProjectEsCellQc toEntity(ProjectEsCellQcDTO projectEsCellQcDTO) {
        ProjectEsCellQc projectEsCellQc = entityMapper.toTarget(projectEsCellQcDTO, ProjectEsCellQc.class);

        if (Strings.isBlank(projectEsCellQcDTO.getCompletionComment())) {
            projectEsCellQc.setCompletionComment(null);
        } else {
            projectEsCellQc.setCompletionComment(projectEsCellQcDTO.getCompletionComment());
        }

        if (Strings.isBlank(projectEsCellQcDTO.getCompletionNote())) {
            projectEsCellQc.setCompletionNote(null);
        } else {
            projectEsCellQc.setCompletionNote(projectEsCellQcDTO.getCompletionNote());
        }
        // entry in DTO needs validation against an enum
        setEsCellQcCommentToEntity(projectEsCellQc, projectEsCellQcDTO);

        return projectEsCellQc;

    }

    private void setEsCellQcCommentToEntity(ProjectEsCellQc projectEsCellQc, ProjectEsCellQcDTO projectEsCellQcDTO) {
        EsCellQcComment esCellQcComment = esCellQcCommentMapper.toEntity(projectEsCellQcDTO.getEsCellQcComment());
        projectEsCellQc.setEsCellQcComment(esCellQcComment);
    }
}
