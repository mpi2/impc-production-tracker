package org.gentar.biology.project.search;

import static org.gentar.mockdata.MockData.TPN_000000001;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.gentar.exceptions.CommonErrorMessages;
import org.gentar.exceptions.UserOperationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class SearcherTest {

    @Mock
    private SearchExecutor searchExecutor;
    @Mock
    private SearchExecutorByGene searchExecutorByGene;

    Searcher testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new Searcher(searchExecutorByGene);
    }

    @Test
    void executeSearchTypeGene() {

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

        Search search = new Search("gene", List.of("inputs"), projectFilter);
        List<SearchResult> searchResults = testInstance.execute(search);

        assertEquals(searchResults.size(), 1);
    }

    @Test
    void executeSearchTypeLocation() {

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

        Search search = new Search("location", List.of("inputs"), projectFilter);


        Exception exception = assertThrows(NotImplementedException.class, () -> {
            List<SearchResult> searchResults = testInstance.execute(search);
        });

        String expectedMessage =
            String.format("Search by location not implemented yet.");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}