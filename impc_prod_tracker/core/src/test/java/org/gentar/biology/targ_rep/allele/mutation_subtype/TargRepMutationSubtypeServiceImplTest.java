package org.gentar.biology.targ_rep.allele.mutation_subtype;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TargRepMutationSubtypeServiceImplTest {

    @Mock
    private TargRepMutationSubtypeRepository targRepMutationSubtypeRepository;

    private TargRepMutationSubtypeServiceImpl testInstance =
        new TargRepMutationSubtypeServiceImpl(targRepMutationSubtypeRepository);

    @BeforeEach
    void setUp() {
        testInstance = new TargRepMutationSubtypeServiceImpl(targRepMutationSubtypeRepository);
    }

    @Test
    @DisplayName("getTargRepMutationSubtypeByName Should find by name")
    void getTargRepMutationSubtypeByName() {
        Mockito
            .when(targRepMutationSubtypeRepository
                .findTargRepMutationSubtypeByNameIgnoreCase("TestName"))
            .thenReturn(getTargRepMutationSubtype());
        TargRepMutationSubtype targRepMutationSubtype =
            testInstance.getTargRepMutationSubtypeByName("TestName");
        assertEquals("TestCode", targRepMutationSubtype.getCode());
    }

    private TargRepMutationSubtype getTargRepMutationSubtype() {
        TargRepMutationSubtype targRepMutationSubtype = new TargRepMutationSubtype();
        targRepMutationSubtype.setId(1L);
        targRepMutationSubtype.setCode("TestCode");
        targRepMutationSubtype.setName("TestName");
        return targRepMutationSubtype;
    }
}