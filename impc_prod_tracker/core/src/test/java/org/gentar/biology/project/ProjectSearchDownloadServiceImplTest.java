package org.gentar.biology.project;

import static org.gentar.mockdata.MockData.TPN_000000001;
import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.projectMockData;
import static org.gentar.mockdata.MockData.projectSearchDownloadProjectionListMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.gentar.biology.ortholog.OrthologService;
import org.gentar.biology.project.engine.ProjectValidator;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadProjectionListDto;
import org.gentar.biology.project.search.Searcher;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellRepository;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellServiceImpl;
import org.gentar.biology.targ_rep.production_qc.TargRepProductionQcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
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
                .withProductionColonyName(List.of("colonyName"))
                .build();

        lenient().when(projectRepository.findAllProjectsForCsvFile(Mockito.any(
            ProjectSearchDownloadProjectionListDto.class)))
            .thenReturn(projectSearchDownloadProjectionListMockData());

        assertDoesNotThrow(() ->
            testInstance.writeReport(response, projectFilter)
        );
    }

    @Test
    void writeReportCaches() {
    }
}