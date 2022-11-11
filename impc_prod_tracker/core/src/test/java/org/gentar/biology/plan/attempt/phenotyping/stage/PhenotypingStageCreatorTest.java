package org.gentar.biology.plan.attempt.phenotyping.stage;

import static org.gentar.mockdata.MockData.PSN_000000001;
import static org.gentar.mockdata.MockData.phenotypingStageMockData;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.checkerframework.checker.units.qual.C;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.engine.PlanUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PhenotypingStageCreatorTest {


    @Mock
    private EntityManager entityManager;
    @Mock
    private HistoryService<PhenotypingStage> historyService;
    @Mock
    private PhenotypingStageStateSetter phenotypingStageStateSetter;
    @Mock
    private PhenotypingStageValidator phenotypingStageValidator;
    @Mock
    private PlanUpdater planUpdater;


    PhenotypingStageCreator testInstance;


    @BeforeEach
    void setUp() {

        testInstance = new PhenotypingStageCreator(entityManager,
            historyService,
            phenotypingStageStateSetter,
            phenotypingStageValidator,
            planUpdater);
    }


    @Test
    void create() {

        PhenotypingStage phenotypingStage=testInstance.create(phenotypingStageMockData());

        assertEquals(phenotypingStage.getPsn(),PSN_000000001);
    }
}