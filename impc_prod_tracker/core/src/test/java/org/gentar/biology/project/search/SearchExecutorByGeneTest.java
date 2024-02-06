package org.gentar.biology.project.search;

import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.project.ProjectService;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.gentar.mockdata.MockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.gentar.mockdata.MockData.MGI_00000001;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class SearchExecutorByGeneTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private GeneExternalService geneExternalService;

    SearchExecutorByGene testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new SearchExecutorByGene(projectService,geneExternalService);
    }

    @Test
    void findProjects() {


        lenient().when(projectService.getProjects(
            ProjectFilterBuilder.getInstance().withGenes(List.of(MGI_00000001)).build()))
            .thenReturn(List.of(MockData.projectMockData()));

        List<SearchResult> searchResults = testInstance.findProjects(MGI_00000001);

        assertEquals(searchResults.size(),1);
    }

    @Test
    void findProjectsNoResultFound() {

        lenient().when(projectService.getProjects(
            ProjectFilterBuilder.getInstance().withGenes(List.of(MGI_00000001)).build()))
            .thenReturn(List.of(MockData.projectMockData()));

        lenient().when(geneExternalService.getSynonymFromExternalGenes("RSph3b"))
            .thenReturn(MockData.geneMockData());

        List<SearchResult> searchResults = testInstance.findProjects("RSph3b");

        assertEquals(searchResults.size(),1);
    }

    @Test
    void findProjectsNoResultFoundSearchResultEmpty() {

        lenient().when(geneExternalService.getSynonymFromExternalGenes("RSph3b"))
            .thenReturn(MockData.geneMockData());

        List<SearchResult> searchResults = testInstance.findProjects("RSph3b");

        assertEquals(searchResults.size(),1);
    }

    @Test
    void findProjectsNoResultFoundSynonymNotFound() {

        lenient().when(projectService.getProjects(
            ProjectFilterBuilder.getInstance().withGenes(List.of(MGI_00000001)).build()))
            .thenReturn(List.of(MockData.projectMockData()));


        List<SearchResult> searchResults = testInstance.findProjects("RSph3b");

        assertEquals(searchResults.size(),1);
    }

}