package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.gentar.mockdata.MockData.mutationMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class MutationUpdaterTest {

    @Mock
    private MutationRepository mutationRepository;
    @Mock
    private HistoryService<Mutation> historyService;
    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;
    @Mock
    private MutationValidator mutationValidator;

    MutationUpdater testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new MutationUpdater(mutationRepository,
            historyService,
            policyEnforcement,
            mutationValidator);
    }

    @Test
    void update() {

        History exception = assertDoesNotThrow(() ->
            testInstance.update(mutationMockData(),mutationMockData())
        );

        assertNull(exception);
    }
}