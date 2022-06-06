package org.gentar.biology.targ_rep.allele.mutation_type;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TargRepMutationTypeServiceImplTest {

    @Mock
    private TargRepMutationTypeRepository targRepMutationTypeRepository;

    private TargRepMutationTypeServiceImpl testInstance =
        new TargRepMutationTypeServiceImpl(targRepMutationTypeRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepMutationTypeServiceImpl(targRepMutationTypeRepository);
    }

    @Test
    @DisplayName("getTargRepMutationTypeByName Should find by name")
    void getTargRepMutationTypeByName() {
        Mockito
            .when(targRepMutationTypeRepository
                .findTargRepMutationTypeByNameIgnoreCase("TestName"))
            .thenReturn(getTargRepMutationSubtype());
        TargRepMutationType targRepMutationType =
            testInstance.getTargRepMutationTypeByName("TestName");
        assertEquals("TestCode", targRepMutationType.getCode());
    }

    private TargRepMutationType getTargRepMutationSubtype() {
        TargRepMutationType targRepMutationType = new TargRepMutationType();
        targRepMutationType.setId(1L);
        targRepMutationType.setCode("TestCode");
        targRepMutationType.setName("TestName");
        return targRepMutationType;
    }
}