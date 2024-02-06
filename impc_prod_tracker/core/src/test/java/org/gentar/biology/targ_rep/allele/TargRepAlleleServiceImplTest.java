package org.gentar.biology.targ_rep.allele;

import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.gentar.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TargRepAlleleServiceImplTest {

    @Mock
    private TargRepAlleleRepository alleleRepository;

    private TargRepAlleleServiceImpl testInstance = new TargRepAlleleServiceImpl(alleleRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepAlleleServiceImpl(alleleRepository);
    }


    @Test
    @DisplayName("getTargRepAllelesByGeneFailIfNull Should Throw Exception")
    void getTargRepAllelesByGeneFailIfNull() {

        TargRepGene gene = getTargRepGene(1L, "TGS");
        Exception exception = assertThrows(NotFoundException.class, () -> {
            List<TargRepAllele> targRepAlleles =
                testInstance.getTargRepAllelesByGeneFailIfNull(gene);
        });

        String expectedMessage = "There are not Allele available for [" + gene.getSymbol()
            + "] marker_symbol does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    @DisplayName("getTargRepAllelesByGeneFailIfNull Should Find Allele")
    void getTargRepAllelesByGeneNotFailIfNotNullWith() {

        TargRepGene gene = getTargRepGene(44909L, "Spanxn4");

        Mockito.when(alleleRepository.findByGene(gene))
            .thenReturn(List.of(getTargRepAllele()));
        List<TargRepAllele> targRepAllele = testInstance.getTargRepAllelesByGeneFailIfNull(gene);
        assertEquals("GRCm38", targRepAllele.getFirst().getAssembly());

    }

    @Test
    @DisplayName("getNotNullTargRepAlelleById Should Find Allele For Given Id")
    void getNotNullTargRepAlelleById() {

        Mockito.when(alleleRepository.findTargRepAlleleById(44909L)).thenReturn(getTargRepAllele());
        TargRepAllele targRepAlleles = testInstance.getNotNullTargRepAlelleById(44909L);
        assertEquals("GRCm38", targRepAlleles.getAssembly());
    }

    @Test
    @DisplayName("getNotNullTargRepAlelleById Should Throw Exception If not Found")
    void getNotNullTargRepAlelleByIdThrowException() {

        Exception exception = assertThrows(NotFoundException.class, () -> {
            TargRepAllele targRepAlleles = testInstance.getNotNullTargRepAlelleById(44909L);
        });

        String expectedMessage = "A targ rep allele with the id [44909] does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    @DisplayName("getPageableTargRepAllele Should Find Pageable")
    void getPageableTargRepAllele() {
        Pageable pageable = PageRequest.of(0, 1);
        final Page<TargRepAllele> page = new PageImpl<>(List.of(getTargRepAllele()));
        Mockito.when(alleleRepository.findAll(pageable))
            .thenReturn(page);

        Page<TargRepAllele> targRepAlleles =
            testInstance.getPageableTargRepAllele(pageable);
        assertEquals(1, targRepAlleles.getTotalElements());
    }

    private TargRepAllele getTargRepAllele() {
        TargRepAllele targRepAllele = new TargRepAllele();
        targRepAllele.setId(44909L);
        targRepAllele.setAssembly("GRCm38");
        targRepAllele.setChromosome("9");
        targRepAllele.setCassette("L1L2_Bact_P");
        targRepAllele.setBackbone("pACYC184");
        return targRepAllele;
    }

    private TargRepGene getTargRepGene(Long id, String symbol) {
        TargRepGene gene = new TargRepGene();
        gene.setId(id);
        gene.setSymbol(symbol);
        return gene;
    }
}