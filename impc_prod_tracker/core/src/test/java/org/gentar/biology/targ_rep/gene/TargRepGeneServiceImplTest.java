package org.gentar.biology.targ_rep.gene;

import static org.junit.jupiter.api.Assertions.*;

import org.gentar.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TargRepGeneServiceImplTest {

    @Mock
    private TargRepGeneRepository targRepGeneRepository;

    private TargRepGeneServiceImpl testInstance =
        new TargRepGeneServiceImpl(targRepGeneRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepGeneServiceImpl(targRepGeneRepository);
    }


    @Test
    @DisplayName("getNotNullTargRepGenBankFileById Should Find By Id")
    void getNotNullTargRepGenBankFileById() {
        Mockito.when(targRepGeneRepository.findBySymbol("TestSymbol")).thenReturn(getTargRepGene());
        TargRepGene targRepGene = testInstance.getGeneBySymbolFailIfNull("TestSymbol");
        assertEquals("TestType", targRepGene.getType());
    }


    @Test
    @DisplayName("getGeneBySymbolFailIfNull Should Throw Exception If not Found")
    void getGeneBySymbolFailIfNull() {
        Exception exception = assertThrows(NotFoundException.class, () -> {
            TargRepGene targRepGene = testInstance.getGeneBySymbolFailIfNull("TestSymbol");
        });

        String expectedMessage = "Gene with accession id or symbol [TestSymbol] does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private TargRepGene getTargRepGene() {
        TargRepGene targRepGene = new TargRepGene();
        targRepGene.setId(44909L);
        targRepGene.setType("TestType");
        targRepGene.setSymbol("TestSymbol");
        return targRepGene;
    }
}