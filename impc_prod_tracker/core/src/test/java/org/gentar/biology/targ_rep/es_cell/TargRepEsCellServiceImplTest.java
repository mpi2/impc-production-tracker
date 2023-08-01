package org.gentar.biology.targ_rep.es_cell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.gentar.exceptions.UserOperationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TargRepEsCellServiceImplTest {

    @Mock
    private TargRepEsCellRepository esCellRepository;

    private TargRepEsCellServiceImpl testInstance = new TargRepEsCellServiceImpl(esCellRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepEsCellServiceImpl(esCellRepository);
    }

    @Test
    @DisplayName("getTargRepEsCellById Find Ess Cell By Id")
    void getTargRepEsCellById() {

        TargRepEsCell targRepEsCellResponse = new TargRepEsCell();
        targRepEsCellResponse.setName("EPD0974_4_A06");
        Mockito.when(esCellRepository.findTargRepEsCellById(1L)).thenReturn(targRepEsCellResponse);
        TargRepEsCell targRepEsCell = testInstance.getTargRepEsCellById(1L);
        assertEquals("EPD0974_4_A06", targRepEsCell.getName());
    }

    @Test
    @DisplayName("getTargRepEsCellById Throw Exception If Null")
    void getTargRepEsCellByIdThrowException() {
        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            TargRepEsCell targRepEsCell = testInstance.getTargRepEsCellById(1L);
        });

        String expectedMessage = "Es cell does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    @DisplayName("getTargRepEsCellByNameFailsIfNull Throw Exception If Null")
    void getTargRepEsCellByNameFailsIfNull() {
        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            TargRepEsCell targRepEsCell =
                testInstance.getTargRepEsCellByNameFailsIfNull("EPD0974_4_A06");
        });

        String expectedMessage = "Es cell name [EPD0974_4_A06] does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("getTargRepEsCellByNameFailsIfNull Not Throw Exception Not Null")
    void getTargRepEsCellByNameFailsIfNotNullNotThrow() {
        TargRepEsCell targRepEsCellResponse = new TargRepEsCell();
        targRepEsCellResponse.setName("EPD0974_4_A06");
        Mockito.when(esCellRepository.findTargRepEsCellByName("EPD0974_4_A06"))
            .thenReturn(targRepEsCellResponse);
        TargRepEsCell targRepEsCell =
            testInstance.getTargRepEsCellByNameFailsIfNull("EPD0974_4_A06");
        assertEquals("EPD0974_4_A06", targRepEsCell.getName());
    }

    @Test
    @DisplayName("getTargRepEscellByAlleleFailsIfNull Not Throw Exception If Not Null")
    void getTargRepEscellByAlleleFailsIfNull() {
        TargRepEsCell targRepEsCellResponse = new TargRepEsCell();
        targRepEsCellResponse.setName("EPD0974_4_A06");

        Mockito.when(esCellRepository.findTargRepEsCellByAllele(getTargRepAllele())).thenReturn(
            Collections.singletonList(targRepEsCellResponse));
        List<TargRepEsCell> targRepEsCell =
            testInstance.getTargRepEscellByAlleleFailsIfNull(getTargRepAllele());

        assertEquals("EPD0974_4_A06", targRepEsCell.get(0).getName());
    }

    private TargRepAllele getTargRepAllele() {
        TargRepAllele targRepAllele = new TargRepAllele();
        targRepAllele.setId(44909L);
        targRepAllele.setAssembly("GRCm38");
        targRepAllele.setChromosome("9");
        targRepAllele.setCassette("L1L2_Bact_P");
        targRepAllele.setBackbone("pACYC184");
        targRepAllele.setGene(getTargRepGene(44909L, "Spanxn4"));
        return targRepAllele;
    }

    private TargRepGene getTargRepGene(Long id, String symbol) {
        TargRepGene gene = new TargRepGene();
        gene.setId(id);
        gene.setSymbol(symbol);
        return gene;
    }
}