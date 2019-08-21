package uk.ac.ebi.impc_prod_tracker.common.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.ac.ebi.impc_prod_tracker.conf.security.SystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.data.common.history.detail.HistoryDetail;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HistoryBuilderTest
{
    private HistoryBuilder<PlanMock> testInstance;

    @Mock private SubjectRetriever subjectRetriever;
    @Mock private SystemSubject systemSubject;

    @Before
    public void setUp()
    {
        when(subjectRetriever.getSubject()).thenReturn(systemSubject);
        when(systemSubject.getLogin()).thenReturn("test");
    }

    @Test
    public void testBuildHistoryEntry()
    {
        testInstance = new HistoryBuilder<>(subjectRetriever);
        testInstance.setEntityId(1L);
        testInstance.setEntityName(PlanMock.class.getSimpleName());

        SubStatusMock subStatusMock1 = new SubStatusMock(1L, "subStatusMock1");
        StatusMock statusMock1 = new StatusMock(1L, "statusMock1", subStatusMock1);
        PrivacyMock privacyMock1 = new PrivacyMock(1L, "privacyMock1");
        WorkUnitMock workUnitMock1A = new WorkUnitMock(1L, "workUnitMock1A", "workUnitMockShort1A");
        WorkUnitMock workUnitMock1B = new WorkUnitMock(2L, "workUnitMock1B", "workUnitMockShort1B");
        PlanMock planMock1 = new PlanMock();
        planMock1.setId(1L);
        planMock1.setPlanName("planMock1");
        planMock1.setStatus(statusMock1);
        planMock1.setWorkUnitList(Arrays.asList(workUnitMock1A, workUnitMock1B));
        planMock1.setPrivacy(privacyMock1);

        SubStatusMock subStatusMock2 = new SubStatusMock(2L, "subStatusMock2");
        StatusMock statusMock2 = new StatusMock(2L, "statusMock2", subStatusMock2);
        PrivacyMock privacyMock2 = new PrivacyMock(2L, "privacyMock2");
        WorkUnitMock workUnitMock2A = new WorkUnitMock(3L, "workUnitMock2A", "workUnitMockShort2A");
        WorkUnitMock workUnitMock2B = new WorkUnitMock(4L, "workUnitMock2B", "workUnitMockShort2B");
        PlanMock planMock2 = new PlanMock();
        planMock2.setId(2L);
        planMock2.setPlanName("planMock2");
        planMock2.setStatus(statusMock2);
        planMock2.setWorkUnitList(Arrays.asList(workUnitMock2A, workUnitMock2B));
        planMock2.setPrivacy(privacyMock2);

        History history = testInstance.buildHistoryEntry(planMock1, planMock2);

        assertThat("History record not generated:", history, is(notNullValue()));

        List<HistoryDetail> historyDetails = history.getHistoryDetailSet();
        assertThat("Unexpected number of history details", historyDetails.size(), is(5));

    }

    @Data
    public class PlanMock
    {
        Long id;
        String planName;
        List<WorkUnitMock> workUnitList;
        StatusMock status;
        PrivacyMock privacy;
    }

    @Data
    @AllArgsConstructor
    public class WorkUnitMock
    {
        Long id;
        String workUnitName;
        String shortName;

        public String toString()
        {
            return workUnitName;
        }
    }

    @Data
    @AllArgsConstructor
    public class PrivacyMock
    {
        Long id;
        String privacyName;
    }

    @Data
    @AllArgsConstructor
    public class StatusMock
    {
        Long id;
        String statusName;
        SubStatusMock subStatus;
    }

    @Data
    @AllArgsConstructor
    public class SubStatusMock
    {
        Long id;
        String subStatusName;
    }
}