package org.gentar.biology.targ_rep.targeting_vector;

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
class TargRepTargetingVectorServiceImplTest {


    @Mock
    private TargRepTargetingVectorRepository targetingVectorRepository;

    private TargRepTargetingVectorServiceImpl testInstance =
        new TargRepTargetingVectorServiceImpl(targetingVectorRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepTargetingVectorServiceImpl(targetingVectorRepository);
    }

    @Test
    @DisplayName("getNotNullTargRepTargetingVectorById Find Targeting Vector By Id")
    void getNotNullTargRepTargetingVectorById() {
        Mockito.when(targetingVectorRepository.findTargRepTargetingVectorById(1L))
            .thenReturn(targRepTargetingVector());
        TargRepTargetingVector targRepTargetingVector =
            testInstance.getNotNullTargRepTargetingVectorById(1L);
        assertEquals("PG00138_Y_5_B05", targRepTargetingVector.getName());
    }

    @Test
    @DisplayName("getNotNullTargRepTargetingVectorById Find Targeting Vector By Id Throe Exception")
    void getNotNullTargRepTargetingVectorByIdThrowException() {
        Exception exception = assertThrows(NotFoundException.class, () -> {
            TargRepTargetingVector targRepTargetingVector =
                testInstance.getNotNullTargRepTargetingVectorById(0L);
        });
        String expectedMessage = "A targ rep Targeting Vector with the id [0] does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("getPageableTargRepTargetingVector Find Pageable")
    void getPageableTargRepTargetingVector() {
        Pageable pageable = PageRequest.of(0, 1);
        final Page<TargRepTargetingVector> page =
            new PageImpl<>(Arrays.asList(targRepTargetingVector()));
        Mockito.when(targetingVectorRepository.findAll(pageable))
            .thenReturn(page);

        Page<TargRepTargetingVector> targRepTargetingVectors =
            testInstance.getPageableTargRepTargetingVector(pageable);
        assertEquals(1, targRepTargetingVectors.getTotalElements());
    }

    private TargRepTargetingVector targRepTargetingVector() {
        TargRepTargetingVector targRepTargetingVector = new TargRepTargetingVector();
        targRepTargetingVector.setId(1L);
        targRepTargetingVector.setName("PG00138_Y_5_B05");
        return targRepTargetingVector;
    }
}