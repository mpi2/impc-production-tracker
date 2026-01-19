package org.gentar.biology.ortholog;

import org.gentar.biology.project.projection.dto.ProjectSearchDownloadOrthologDto;
import org.gentar.graphql.GraphQLConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.gentar.mockdata.MockData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrthologServiceImplTest {

    @Mock
    private GraphQLConsumer graphQLConsumer;
    @Mock
    private JSONToOrthologsMapper jsonToOrthologsMapper;

    OrthologServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new OrthologServiceImpl();
    }

    @Test
    void getOrthologsByAccIdsAccIdNull() {

        Map<String, List<Ortholog>> orthologs =
            testInstance.formatOrthologs(null);

        assertEquals(0, orthologs.size());

    }



    @Test
    void getOrthologs() {

        List<ProjectSearchDownloadOrthologDto> projectSearchDownloadOrthologDtoList =
            testInstance.getOrthologs(List.of(MGI_00000001));

        assertEquals(projectSearchDownloadOrthologDtoList.size(), 0);
    }

    @Test
    void calculateBestSearchDownloadOrthologs() {


        List<ProjectSearchDownloadOrthologDto> projectSearchDownloadOrthologDtoList =
            testInstance.calculateBestSearchDownloadOrthologs(
                List.of(projectSearchDownloadOrthologDtoMockData(),
                    projectSearchDownloadOrthologDtoMockData()));

        assertEquals(projectSearchDownloadOrthologDtoList.size(), 1);
    }

    @Test
    void calculateBestOrthologs() {

        List<Ortholog> orthologs =
            testInstance.calculateBestOrthologs(
                List.of(orthologMockData(),
                    orthologMockData()));

        assertEquals(orthologs.size(), 2);
    }

}