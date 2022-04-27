package org.gentar.biology.targ_rep.ikmc_project.status;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TargRepIkmcProjectStatusServiceImplTest {

    @Mock
    private TargRepIkmcProjectStatusRepository targRepIkmcProjectStatusRepository;

    private TargRepIkmcProjectStatusServiceImpl testInstance =
        new TargRepIkmcProjectStatusServiceImpl(targRepIkmcProjectStatusRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepIkmcProjectStatusServiceImpl(targRepIkmcProjectStatusRepository);
    }

    @Test
    @DisplayName("getTargRepIkmcProjectStatusByName Should Find By Name")
    void getTargRepIkmcProjectStatusByName() {
        Mockito.when(targRepIkmcProjectStatusRepository
            .findTargRepIkmcProjectStatusByNameIgnoreCase("TestName"))
            .thenReturn(getTargRepIkmcProject());
        TargRepIkmcProjectStatus targRepIkmcProjectStatus =
            testInstance.getTargRepIkmcProjectStatusByName("TestName");
        assertEquals(1L, targRepIkmcProjectStatus.getId());
    }

    private TargRepIkmcProjectStatus getTargRepIkmcProject() {
        TargRepIkmcProjectStatus targRepIkmcProjectStatus = new TargRepIkmcProjectStatus();
        targRepIkmcProjectStatus.setId(1L);
        targRepIkmcProjectStatus.setName("TestName");
        return targRepIkmcProjectStatus;
    }
}