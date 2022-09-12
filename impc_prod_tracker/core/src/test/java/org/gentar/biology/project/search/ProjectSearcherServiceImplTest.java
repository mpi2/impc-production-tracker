package org.gentar.biology.project.search;

import static org.gentar.mockdata.MockData.TPN_000000001;
import static org.gentar.mockdata.MockData.projectMockData;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectService;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ProjectSearcherServiceImplTest {


    @Mock
    private Searcher searcher;
    @Mock
    private ProjectService projectService;

    ProjectSearcherServiceImpl testInstance;


    @BeforeEach
    void setUp() {
        testInstance = new ProjectSearcherServiceImpl(searcher, projectService);
    }

    @Test
    void executeSearch() {

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

        Search search = new Search( "gene", List.of("inputs"), projectFilter);
        SearchReport searchReport =  testInstance.executeSearch(search);

        assertEquals(searchReport.getSearchType(), SearchType.valueOfName("gene"));
    }

    @Test
    void executeSearchPageAble() {

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



        Pageable pageable = PageRequest.of(0, 1);
        final Page<Project> page = new PageImpl<>(Collections.singletonList(projectMockData()));


        Search search = new Search( "gene", List.of("inputs"), projectFilter);
        SearchReport searchReport = testInstance.executeSearch(search, pageable);
        assertEquals(searchReport.getSearchType(), SearchType.valueOfName("gene"));
    }
}