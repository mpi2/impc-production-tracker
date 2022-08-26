package org.gentar.biology.mutation;

import static org.gentar.mockdata.MockData.mutationMockData;
import static org.gentar.mockdata.MockData.outcomeMockData;
import static org.junit.jupiter.api.Assertions.*;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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