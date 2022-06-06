package org.gentar.biology.targ_rep.pipeline;

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
class TargRepPipelineServiceImplTest {

    @Mock
    private TargRepPipelineRepository pipelineRepository;

    private TargRepPipelineServiceImpl testInstance =
        new TargRepPipelineServiceImpl(pipelineRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepPipelineServiceImpl(pipelineRepository);
    }


    @Test
    @DisplayName("getNotNullTargRepPipelineById Find PipeLine By Id")
    void getNotNullTargRepPipelineById() {
        Mockito.when(pipelineRepository.findTargRepPipelineById(1L))
            .thenReturn(getTargRepPipeline());
        TargRepPipeline targRepPipeline = testInstance.getNotNullTargRepPipelineById(1L);
        assertEquals("KOMP-CSD", targRepPipeline.getName());
    }

    @Test
    @DisplayName("getNotNullTargRepPipelineById Throw Exception If Null")
    void getNotNullTargRepPipelineByIdThrowException() {
        Exception exception = assertThrows(NotFoundException.class, () -> {
            TargRepPipeline targRepPipeline = testInstance.getNotNullTargRepPipelineById(0L);
        });
        String expectedMessage = "A targ rep pipeline with the id [0] does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("getPageableTargRepPipeline Should Find Pageable")
    void getPageableTargRepPipeline() {
        Pageable pageable = PageRequest.of(0, 1);
        final Page<TargRepPipeline> page = new PageImpl<>(Arrays.asList(getTargRepPipeline()));
        Mockito.when(pipelineRepository.findAll(pageable))
            .thenReturn(page);

        Page<TargRepPipeline> targRepPipelinePage =
            testInstance.getPageableTargRepPipeline(pageable);
        assertEquals(1, targRepPipelinePage.getTotalElements());

    }

    private TargRepPipeline getTargRepPipeline() {
        TargRepPipeline targRepPipeline = new TargRepPipeline();
        targRepPipeline.setName("KOMP-CSD");
        targRepPipeline.setDescription("Description");
        targRepPipeline.setGeneTrap(false);
        targRepPipeline.setReportToPublic(true);
        return targRepPipeline;
    }
}