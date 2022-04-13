package org.gentar.biology.targ_rep.ikmc_project;

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
class TargRepIkmcProjectServiceImplTest {

    @Mock
    private TargRepIkmcProjectRepository targRepIkmcProjectRepository;

    private TargRepIkmcProjectServiceImpl testInstance =
        new TargRepIkmcProjectServiceImpl(targRepIkmcProjectRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepIkmcProjectServiceImpl(targRepIkmcProjectRepository);
    }

    @Test
    @DisplayName("getTargRepIkmcProjectStatusByName Should Find By Id")
    void getNotNullTargRepIkmcProjectById() {
        Mockito.when(targRepIkmcProjectRepository.findTargRepIkmcProjectById(1L))
            .thenReturn(getTargRepIkmcProject());
        TargRepIkmcProject targRepIkmcProject = testInstance.getNotNullTargRepIkmcProjectById(1L);
        assertEquals(1L, targRepIkmcProject.getId());
    }

    @Test
    @DisplayName("getNotNullTargRepIkmcProjectByIdThrowException Throw Exception If Null")
    void getNotNullTargRepIkmcProjectByIdThrowException() {
        Exception exception = assertThrows(NotFoundException.class, () -> {
            TargRepIkmcProject targRepIkmcProject =
                testInstance.getNotNullTargRepIkmcProjectById(1L);
        });

        String expectedMessage = "An IKMC project with the id [1] does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private TargRepIkmcProject getTargRepIkmcProject() {
        TargRepIkmcProject targRepIkmcProject = new TargRepIkmcProject();
        targRepIkmcProject.setId(1L);
        targRepIkmcProject.setName("TestName");
        return targRepIkmcProject;
    }
}