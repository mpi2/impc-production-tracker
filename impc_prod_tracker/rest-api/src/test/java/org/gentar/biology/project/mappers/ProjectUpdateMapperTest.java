package org.gentar.biology.project.mappers;

import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectCommonDataDTO;
import org.gentar.biology.project.ProjectUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectUpdateMapperTest
{
    private ProjectUpdateMapper testInstance;

    @Mock
    private ProjectCommonDataMapper projectCommonDataMapper;

    @Mock
    private ProjectCompletionNoteMapper projectCompletionNoteMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new ProjectUpdateMapper(projectCommonDataMapper, projectCompletionNoteMapper);
    }

    @Test
    void toDto()
    {
        Project project = new Project();
        testInstance.toDto(project);

        verify(projectCommonDataMapper, times(1)).toDto(project);
    }

    @Test
    void toEntity()
    {
        ProjectUpdateDTO projectUpdateDTO = new ProjectUpdateDTO();
        ProjectCommonDataDTO projectCommonDataDTO = new ProjectCommonDataDTO();
        projectUpdateDTO.setProjectCommonDataDTO(projectCommonDataDTO);
        when(projectCommonDataMapper.toEntity(projectCommonDataDTO)).thenReturn(new Project());

        testInstance.toEntity(projectUpdateDTO);
        verify(projectCommonDataMapper, times(1)).toEntity(projectCommonDataDTO);
    }
}
