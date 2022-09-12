package org.gentar.biology.project;

import static org.gentar.mockdata.MockData.TEST_COMMENT;
import static org.gentar.mockdata.MockData.TEST_INPUT;
import static org.gentar.mockdata.MockData.TPN_000000001;
import static org.gentar.mockdata.MockData.projectSearchDownloadProjectionListMockData;
import static org.gentar.mockdata.MockData.searchResultMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.List;
import org.gentar.biology.ortholog.OrthologService;
import org.gentar.biology.project.engine.ProjectValidator;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadProjectionListDto;
import org.gentar.biology.project.search.SearchResult;
import org.gentar.biology.project.search.Searcher;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ProjectSearchDownloadServiceImplTest {

    @Mock
    ProjectRepository projectRepository;
    @Mock
    private Environment env;
    @Mock
    private ProjectValidator projectValidator;
    @Mock
    private OrthologService orthologService;
    @Mock
    private Searcher searcher;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private ProjectSearchDownloadServiceImpl testInstance;


    @BeforeEach
    void setUp() {
        testInstance =
            new ProjectSearchDownloadServiceImpl(env, projectRepository, projectValidator,
                orthologService, searcher);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void writeReport() {

        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withTpns(List.of(TPN_000000001))
                .withIntentions(List.of("intention"))
                .withWorkUnitNames(List.of("workUnitName"))
                .withWorkGroupNames(List.of("workGroup"))
                .withPrivacies(List.of("privacyName"))
                .withSummaryStatusNames(List.of("summaryStatus"))
                .withConsortiaNames(List.of("consortium"))
                .withGenes(List.of("gene"))
                .withPhenotypingExternalRef(List.of("phenotypingExternalRefs"))
                .withProductionColonyName(List.of("productionColonyNames"))
                .build();

        lenient().when(projectRepository.findAllProjectsForCsvFile(any(
            ProjectSearchDownloadProjectionListDto.class)))
            .thenReturn(projectSearchDownloadProjectionListMockData());

        lenient().when(searcher.execute(any()))
            .thenReturn(List.of(searchResultMockData()));

        assertDoesNotThrow(() ->
            testInstance.writeReport(response, projectFilter)
        );
    }

    @Test
    void writeReportProjectNull() {


            SearchResult searchResult = new SearchResult();
            searchResult.setInput(TEST_INPUT);
            searchResult.setComment(TEST_COMMENT);


        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withTpns(List.of(TPN_000000001))
                .withIntentions(List.of("intention"))
                .withWorkUnitNames(List.of("workUnitName"))
                .withWorkGroupNames(List.of("workGroup"))
                .withPrivacies(List.of("privacyName"))
                .withSummaryStatusNames(List.of("summaryStatus"))
                .withConsortiaNames(List.of("consortium"))
                .withGenes(List.of("gene"))
                .withPhenotypingExternalRef(List.of("phenotypingExternalRefs"))
                .withProductionColonyName(List.of("productionColonyNames"))
                .build();

        lenient().when(projectRepository.findAllProjectsForCsvFile(any(
            ProjectSearchDownloadProjectionListDto.class)))
            .thenReturn(projectSearchDownloadProjectionListMockData());

        lenient().when(searcher.execute(any()))
            .thenReturn(List.of(searchResult));

        assertDoesNotThrow(() ->
            testInstance.writeReport(response, projectFilter)
        );
    }


    @Test
    void writeReportCaches() {
    }


}