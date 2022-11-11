package org.gentar.biology.gene_list;

import static org.gentar.mockdata.MockData.MGI_00000001;
import static org.gentar.mockdata.MockData.geneByListRecordMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Set;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectService;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.gentar.mockdata.MockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectsByGroupOfGenesFinderTest {

    @Mock
    private ProjectService projectService;

    ProjectsByGroupOfGenesFinder testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new ProjectsByGroupOfGenesFinder(projectService);
    }
    @Test
    void findProjectsByGenes() {


        lenient().when(projectService.getProjectsWithoutOrthologs(
            ProjectFilterBuilder.getInstance().withGenes(List.of(MGI_00000001)).build()))
            .thenReturn(List.of(MockData.projectMockData()));

        List<Project> projects = testInstance.findProjectsByGenes(Set.of(geneByListRecordMockData()));

        assertEquals(projects.size(),1);
    }
}