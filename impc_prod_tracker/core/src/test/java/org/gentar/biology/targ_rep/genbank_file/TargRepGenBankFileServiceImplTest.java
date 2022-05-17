package org.gentar.biology.targ_rep.genbank_file;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
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

@ExtendWith(MockitoExtension.class)
class TargRepGenBankFileServiceImplTest {


    @Mock
    private TargRepGenBankFileRepository targRepGenBankFileRepository;

    private TargRepGenBankFileServiceImpl testInstance =
        new TargRepGenBankFileServiceImpl(targRepGenBankFileRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepGenBankFileServiceImpl(targRepGenBankFileRepository);
    }

    @Test
    @DisplayName("getNotNullTargRepGenBankFileById Should Find By Id")
    void getNotNullTargRepGenBankFileById() {
        Mockito.when(targRepGenBankFileRepository.findTargRepGenBankFileById(1L))
            .thenReturn(getTargRepGenbankFile());
        TargRepGenbankFile targRepGenbankFile = testInstance.getNotNullTargRepGenBankFileById(1L);
        assertEquals("TestType", targRepGenbankFile.getType());
    }

    @Test
    @DisplayName("getNotNullTargRepGenBankFileByIdNotNow Throw Exception If Null")
    void getNotNullTargRepGenBankFileByIdNotNow() {

        Exception exception = assertThrows(NotFoundException.class, () -> {
            TargRepGenbankFile targRepGenbankFile =
                testInstance.getNotNullTargRepGenBankFileById(0L);
        });

        String expectedMessage = "A targ rep GeneBankFile with the id [0] does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("getPageableTargRepGenBankFile Should Find Pageable")
    void getPageableTargRepGenBankFile() {
        Pageable pageable = PageRequest.of(0, 1);
        final Page<TargRepGenbankFile> page =
            new PageImpl<>(Arrays.asList(getTargRepGenbankFile()));
        Mockito.when(targRepGenBankFileRepository.findAll(pageable))
            .thenReturn(page);

        Page<TargRepGenbankFile> targRepGenbankFiles =
            testInstance.getPageableTargRepGenBankFile(pageable);
        assertEquals(1, targRepGenbankFiles.getTotalElements());
    }

    private TargRepGenbankFile getTargRepGenbankFile() {
        TargRepGenbankFile targRepGenbankFile = new TargRepGenbankFile();
        targRepGenbankFile.setId(44909L);
        targRepGenbankFile.setType("TestType");
        return targRepGenbankFile;
    }
}