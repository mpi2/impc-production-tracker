package org.gentar.audit.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryBuilder;
import org.gentar.audit.history.detail.HistoryDetail;
import org.gentar.security.abac.subject.SubjectRetriever;
import org.gentar.security.abac.subject.SystemSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoryBuilderTest
{
    private HistoryBuilder<PlanMock> testInstance;

    @Mock
    private SubjectRetriever subjectRetriever;
    @Mock private SystemSubject systemSubject;

    @BeforeEach
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
        assertThat("Unexpected number of history details", historyDetails.size(), is(8));

        HistoryDetail planNameHistoryDetail = getHistoryDetails("planName", historyDetails);
        assertThat("Expected plan name change not found", planNameHistoryDetail, is(notNullValue()));
        assertThat("Expected field not found", planNameHistoryDetail.getField(), is("planName"));
        assertThat("Unexpected plan name old value", planNameHistoryDetail.getOldValue(), is("planMock1"));
        assertThat("Unexpected plan name new value", planNameHistoryDetail.getNewValue(), is("planMock2"));

        HistoryDetail privacyNameHistoryDetail = getHistoryDetails("privacy.privacyName", historyDetails);
        assertThat("Expected privacy name change not found", privacyNameHistoryDetail, is(notNullValue()));
        assertThat("Expected field not found", privacyNameHistoryDetail.getField(), is("privacy.privacyName"));
        assertThat(
            "Unexpected privacy name old value", privacyNameHistoryDetail.getOldValue(), is("privacyMock1"));
        assertThat(
            "Unexpected privacy name new value", privacyNameHistoryDetail.getNewValue(), is("privacyMock2"));

        HistoryDetail statusNameHistoryDetail = getHistoryDetails("status.statusName", historyDetails);
        assertThat("Expected status name change not found", statusNameHistoryDetail, is(notNullValue()));
        assertThat("Expected field not found", statusNameHistoryDetail.getField(), is("status.statusName"));
        assertThat(
            "Unexpected status name old value", statusNameHistoryDetail.getOldValue(), is("statusMock1"));
        assertThat(
            "Unexpected status name new value", statusNameHistoryDetail.getNewValue(), is("statusMock2"));

        HistoryDetail workUnitElement1HistoryDetail = getHistoryDetails("workUnitList#1", historyDetails);
        assertThat(
            "Expected work unit element 1 change not found", workUnitElement1HistoryDetail, is(notNullValue()));
        assertThat("Expected field not found", workUnitElement1HistoryDetail.getField(), is("workUnitList#1"));
        assertThat(
            "Unexpected work unit element 1 old value", workUnitElement1HistoryDetail.getOldValue(), is("workUnitMock1A"));
        assertThat(
            "Unexpected work unit element 1 new value", workUnitElement1HistoryDetail.getNewValue(), is(nullValue()));

        HistoryDetail workUnitElement2HistoryDetail = getHistoryDetails("workUnitList#2", historyDetails);
        assertThat("Expected work unit element 2 change not found", workUnitElement2HistoryDetail, is(notNullValue()));
        assertThat(
            "Expected field not found", workUnitElement2HistoryDetail.getField(), is("workUnitList#2"));
        assertThat(
            "Unexpected work unit element 2 old value", workUnitElement2HistoryDetail.getOldValue(), is("workUnitMock1B"));
        assertThat(
            "Unexpected work unit element 2 new value", workUnitElement2HistoryDetail.getNewValue(), is(nullValue()));

        HistoryDetail workUnitElement3HistoryDetail = getHistoryDetails("workUnitList#3", historyDetails);
        assertThat(
            "Expected work unit element 3 change not found", workUnitElement3HistoryDetail, is(notNullValue()));
        assertThat(
            "Expected field not found", workUnitElement3HistoryDetail.getField(), is("workUnitList#3"));
        assertThat(
            "Unexpected work unit element 3 old value", workUnitElement3HistoryDetail.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected work unit element 3 new value", workUnitElement3HistoryDetail.getNewValue(), is("workUnitMock2A"));

        HistoryDetail workUnitElement4HistoryDetail = getHistoryDetails("workUnitList#4", historyDetails);
        assertThat(
            "Expected work unit element 4 change not found", workUnitElement4HistoryDetail, is(notNullValue()));
        assertThat(
            "Expected field not found", workUnitElement4HistoryDetail.getField(), is("workUnitList#4"));
        assertThat(
            "Unexpected work unit element 4 old value", workUnitElement4HistoryDetail.getOldValue(), is(nullValue()));
        assertThat(
            "Unexpected work unit element 4 new value", workUnitElement4HistoryDetail.getNewValue(), is("workUnitMock2B"));

        HistoryDetail workUnitListHistoryDetail = getHistoryDetails("workUnitList", historyDetails);
        System.out.println(workUnitListHistoryDetail);
        assertThat("No change at list level is expected", workUnitListHistoryDetail, is(nullValue()));
    }

    private HistoryDetail getHistoryDetails(
        String field, List<HistoryDetail> historyDetails)
    {
        Optional<HistoryDetail> historyDetail =
            historyDetails.stream().filter(x -> x.getField().equals(field)).findFirst();
        return historyDetail.orElse(null);
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