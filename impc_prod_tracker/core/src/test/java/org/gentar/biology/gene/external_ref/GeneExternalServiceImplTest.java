package org.gentar.biology.gene.external_ref;

import static org.gentar.mockdata.MockData.MGI_00000001;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Map;
import org.gentar.biology.gene.Gene;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.graphql.GraphQLConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeneExternalServiceImplTest {


    private static final String MOUSE_GENE_JSON_STRING =
        "{\"mouse_gene\":[{\"mgi_gene_acc_id\":\"MGI:2446166\",\"symbol\":\"Marveld2\",\"name\":\"MARVEL (membrane-associating) domain containing 2\",\"type\":\"Gene\",\"genome_build\":\"GRCm39\",\"entrez_gene_acc_id\":218518,\"ncbi_chromosome\":\"13\",\"ncbi_start\":100732465,\"ncbi_stop\":100753479,\"ncbi_strand\":\"-\",\"ensembl_gene_acc_id\":\"ENSMUSG00000021636\",\"ensembl_chromosome\":\"13\",\"ensembl_start\":100732465,\"ensembl_stop\":100753479,\"ensembl_strand\":\"-\",\"mgi_cm\":\"53.23\",\"mgi_chromosome\":\"13\",\"mgi_start\":100732465,\"mgi_stop\":100753479,\"mgi_strand\":\"-\"}]}";
    private static final String MGI_2446166 = "MGI:2446166";

    @Mock
    private GraphQLConsumer graphQLConsumer;

    GeneExternalServiceImpl testInstance;


    @BeforeEach
    void setUp() {
        testInstance = new GeneExternalServiceImpl(graphQLConsumer);
    }

    @Test
    void getGeneFromExternalDataBySymbolOrAccIdNoResultFound() {

        lenient().when(graphQLConsumer.executeQuery(Mockito.anyString())).thenReturn("");

        Exception exception = assertThrows(SystemOperationFailedException.class, () -> {
            testInstance.getGeneFromExternalDataBySymbolOrAccId(MGI_00000001);
        });

        String expectedMessage =
            "An unexpected error has occurred in the system. Please contact the administrator to check the details in the logs.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void getGeneFromExternalDataBySymbolOrAccId() {

        lenient().when(graphQLConsumer.executeQuery(Mockito.anyString())).thenReturn(MOUSE_GENE_JSON_STRING);

        Gene gene = testInstance.getGeneFromExternalDataBySymbolOrAccId(MGI_2446166);

        assertEquals(gene.getAccId(), MGI_2446166);


    }

    @Test
    void getAccIdsBySymbolsBulk() {

        lenient().when(graphQLConsumer.executeQuery(Mockito.anyString())).thenReturn(MOUSE_GENE_JSON_STRING);

        Map<String, String> accIdsBySymbolsBulk =
            testInstance.getAccIdsBySymbolsBulk(List.of(MGI_2446166));

        assertEquals(accIdsBySymbolsBulk.get("marveld2"), MGI_2446166);
    }

    @Test
    void getAccIdsByMarkerSymbols() {

        lenient().when(graphQLConsumer.executeQuery(Mockito.anyString())).thenReturn(MOUSE_GENE_JSON_STRING);

        Map<String, String> accIdsBySymbolsBulk =
            testInstance.getAccIdsByMarkerSymbols(List.of("Marveld2"));

        assertEquals(accIdsBySymbolsBulk.get("Marveld2"), MGI_2446166);
    }

    @Test
    void getSymbolsByAccessionIdsBulk() {

        lenient().when(graphQLConsumer.executeQuery(Mockito.anyString())).thenReturn(MOUSE_GENE_JSON_STRING);
        Map<String, String> accIdsBySymbolsBulk =
            testInstance.getSymbolsByAccessionIdsBulk(List.of(MGI_2446166));

        assertEquals(accIdsBySymbolsBulk.get(MGI_2446166), "Marveld2");
    }


    @Test
    void getGenesFromExternalDataBySymbolOrAccId() {

        lenient().when(graphQLConsumer.executeQuery(Mockito.anyString())).thenReturn(
            MOUSE_GENE_JSON_STRING);

        List<Gene> genes = testInstance.getGenesFromExternalDataBySymbolOrAccId(MGI_2446166);

        assertEquals(genes.get(0).getSymbol(), "Marveld2");
    }

    @Test
    void getSynonymFromExternalGenes() {

        lenient().when(graphQLConsumer.executeQuery(Mockito.anyString())).thenReturn(
            MOUSE_GENE_JSON_STRING);

        Gene genes = testInstance.getSynonymFromExternalGenes(MGI_2446166);

        assertEquals(genes.getSymbol(), "Marveld2");

    }
}