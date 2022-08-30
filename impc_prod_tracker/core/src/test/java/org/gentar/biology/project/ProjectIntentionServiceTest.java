package org.gentar.biology.project;

import static org.gentar.mockdata.MockData.projectMockData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProjectIntentionServiceTest {

    @Mock
    private ProjectQueryHelper projectQueryHelper;
    @Mock
    private ProjectRepository projectRepository;

    ProjectIntentionService testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new ProjectIntentionService(projectQueryHelper, projectRepository);

    }


    @Test
    void getProjectsWithSameGeneIntention() {
        lenient().when(projectQueryHelper.getAccIdsByProject(Mockito.any(Project.class)))
            .thenReturn(List.of("MGI_00000001"));
        lenient().when(projectRepository.findAll(Mockito.any(Specification.class)))
            .thenReturn(Collections.singletonList(projectMockData()));
        List<Project> projects = testInstance.getProjectsWithSameGeneIntention(projectMockData());
        assertEquals(projects.size(),0);
    }

}