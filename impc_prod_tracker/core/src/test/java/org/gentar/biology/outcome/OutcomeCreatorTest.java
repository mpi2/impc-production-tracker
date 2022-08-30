package org.gentar.biology.outcome;

import static org.gentar.mockdata.MockData.outcomeMockData;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.biology.mutation.MutationService;
import org.gentar.biology.plan.engine.PlanUpdater;
import org.gentar.biology.specimen.engine.SpecimenStateSetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutcomeCreatorTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private HistoryService<Outcome> historyService;
    @Mock
    private ColonyStateSetter colonyStateSetter;
    @Mock
    private SpecimenStateSetter specimenStateSetter;
    @Mock
    private MutationService mutationService;
    @Mock
    private OutcomeValidator outcomeValidator;
    @Mock
    private PlanUpdater planUpdater;

    OutcomeCreator testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new OutcomeCreator(entityManager, historyService,
            colonyStateSetter,
            specimenStateSetter,
            mutationService,
            outcomeValidator,
            planUpdater);
    }

    @Test
    void create() {
        Outcome outcome =
            testInstance.create(outcomeMockData());


        assertEquals(outcome.getId(), 1L);
    }
}