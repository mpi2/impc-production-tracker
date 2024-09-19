package org.gentar.biology.mutation;

import org.gentar.audit.history.HistoryService;
import org.gentar.biology.mutation.sequence.MutationSequenceService;
import org.gentar.biology.sequence.SequenceService;
import org.gentar.exceptions.NotFoundException;
import org.gentar.graphql.GraphQLConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.gentar.mockdata.MockData.MIN_000000001;
import static org.gentar.mockdata.MockData.mutationMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MutationServiceImplTest {

    @Mock
    private MutationRepository mutationRepository;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private MutationSequenceService mutationSequenceService;
    @Mock
    private MutationUpdater mutationUpdater;
    @Mock
    private HistoryService<Mutation> historyService;
    @Mock
    private MutationValidator mutationValidator;
    @Mock
    private GraphQLConsumer graphQLConsumer;


    MutationServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new MutationServiceImpl(
                mutationRepository,
                sequenceService,
                mutationSequenceService,
                mutationUpdater,
                historyService,
                mutationValidator,
                graphQLConsumer
        );
    }

    @Test
    void getById() {
        lenient().when(mutationRepository.findFirstById(1L)).thenReturn(mutationMockData());
        Mutation mutation = testInstance.getById(1L);
        assertEquals(mutation.getId(), 1L);
    }

    @Test
    void getMutationByMinFailsIfNullMutationNull() {

        Exception exception = assertThrows(NotFoundException.class, () -> {
            Mutation mutation = testInstance.getMutationByMinFailsIfNull(MIN_000000001);
        });

        String expectedMessage =
            "Mutation MIN:000000001 does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getMutationByMinFailsIfNull() {
        lenient().when(mutationRepository.findByMin(MIN_000000001)).thenReturn(mutationMockData());
        Mutation mutation = testInstance.getMutationByMinFailsIfNull(MIN_000000001);
        assertEquals(mutation.getId(), 1L);
    }

    @Test
    void create() {
        lenient().when(mutationRepository.save(mutationMockData())).thenReturn(mutationMockData());
        Mutation mutation = testInstance.create(mutationMockData());
        assertEquals(mutation.getId(), 1L);
    }

    @Test
    void update() {
        lenient().when(mutationRepository.findByMin(MIN_000000001)).thenReturn(mutationMockData());
        assertDoesNotThrow(() ->
            testInstance.update(mutationMockData())
        );

    }

    @Test
    void getHistory() {

        assertDoesNotThrow(() ->
            testInstance.getHistory(mutationMockData())
        );
    }
}