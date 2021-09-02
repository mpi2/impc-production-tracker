package org.gentar.biology.project.mappers;

import org.apache.logging.log4j.util.Strings;
import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.project.esCellQc.centre_pipeline.EsCellCentrePipeline;
import org.gentar.biology.project.esCellQc.comment.EsCellQcComment;
import org.gentar.biology.project.esCellQc.ProjectEsCellQc;
import org.gentar.biology.project.project_es_cell_qc.ProjectEsCellQcDTO;
import org.springframework.stereotype.Component;

@Component
public class ProjectEsCellQcMapper implements Mapper<ProjectEsCellQc, ProjectEsCellQcDTO> {

    private final EntityMapper entityMapper;
    private final EsCellQcCommentMapper esCellQcCommentMapper;
    private final EsCellReceivedFromMapper esCellReceivedFromMapper;

    public ProjectEsCellQcMapper(EntityMapper entityMapper,
                                 EsCellQcCommentMapper esCellQcCommentMapper,
                                 EsCellReceivedFromMapper esCellReceivedFromMapper)
    {
        this.entityMapper = entityMapper;
        this.esCellQcCommentMapper = esCellQcCommentMapper;
        this.esCellReceivedFromMapper = esCellReceivedFromMapper;
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
            if (projectEsCellQc.getEsCellsReceivedFrom() != null)
            {
                projectEsCellQcDTO.setEsCellsReceivedFromName(projectEsCellQc.getEsCellsReceivedFrom().getName());
            }
        }
        return projectEsCellQcDTO;

    }

    @Override
    public ProjectEsCellQc toEntity(ProjectEsCellQcDTO projectEsCellQcDTO)
    {
        ProjectEsCellQc projectEsCellQc = entityMapper.toTarget(projectEsCellQcDTO, ProjectEsCellQc.class);

        // TODO entry in DTO needs validation against an enum
        setEsCellQcCommentToEntity(projectEsCellQc, projectEsCellQcDTO);
        setEsCellReceivedFromEntity(projectEsCellQc, projectEsCellQcDTO);

        return projectEsCellQc;
    }

    private void setEsCellReceivedFromEntity(ProjectEsCellQc projectEsCellQc, ProjectEsCellQcDTO projectEsCellQcDTO)
    {
        EsCellCentrePipeline esCellReceivedFrom = esCellReceivedFromMapper.toEntity(projectEsCellQcDTO.getEsCellsReceivedFromName());
        projectEsCellQc.setEsCellsReceivedFrom(esCellReceivedFrom);
    }

    private void setEsCellQcCommentToEntity(ProjectEsCellQc projectEsCellQc, ProjectEsCellQcDTO projectEsCellQcDTO) {
        EsCellQcComment esCellQcComment = esCellQcCommentMapper.toEntity(projectEsCellQcDTO.getEsCellQcComment());
        projectEsCellQc.setEsCellQcComment(esCellQcComment);
    }
}
