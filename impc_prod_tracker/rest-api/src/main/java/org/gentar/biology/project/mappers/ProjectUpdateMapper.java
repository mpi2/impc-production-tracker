package org.gentar.biology.project.mappers;

import org.apache.logging.log4j.util.Strings;
import org.gentar.Mapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectUpdateDTO;
import org.gentar.biology.project.completionNote.ProjectCompletionNote;
import org.springframework.stereotype.Component;

@Component
public class ProjectUpdateMapper implements Mapper<Project, ProjectUpdateDTO>
{
    private ProjectCommonDataMapper projectCommonDataMapper;
    private ProjectCompletionNoteMapper projectCompletionNoteMapper;

    public ProjectUpdateMapper(ProjectCommonDataMapper projectCommonDataMapper,
                               ProjectCompletionNoteMapper projectCompletionNoteMapper)
    {
        this.projectCommonDataMapper = projectCommonDataMapper;
        this.projectCompletionNoteMapper = projectCompletionNoteMapper;
    }

    @Override
    public ProjectUpdateDTO toDto(Project project)
    {
        ProjectUpdateDTO projectUpdateDTO = new ProjectUpdateDTO();
        projectUpdateDTO.setProjectCommonDataDTO(projectCommonDataMapper.toDto(project));
        projectUpdateDTO.setCompletionComment(project.getCompletionComment());
        if (project.getCompletionNote() != null) {
            projectUpdateDTO.setCompletionNote(project.getCompletionNote().getNote());
        }
        projectUpdateDTO.setTpn(project.getTpn());
        return projectUpdateDTO;
    }

    @Override
    public Project toEntity(ProjectUpdateDTO projectUpdateDTO)
    {
        Project project = new Project();
        if (projectUpdateDTO != null)
        {
            project = projectCommonDataMapper.toEntity(projectUpdateDTO.getProjectCommonDataDTO());
            project.setCompletionComment(projectUpdateDTO.getCompletionComment());
            if (projectUpdateDTO.getCompletionNote() != null) {
                ProjectCompletionNote completionNote = projectCompletionNoteMapper.toEntity(projectUpdateDTO.getCompletionNote());
                project.setCompletionNote(completionNote);
            }
            project.setTpn(projectUpdateDTO.getTpn());
        }
        return project;
    }
}
