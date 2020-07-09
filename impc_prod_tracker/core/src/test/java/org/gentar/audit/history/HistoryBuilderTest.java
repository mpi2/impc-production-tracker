package org.gentar.audit.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.gentar.audit.diff.ChangeType;
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

        History history = testInstance.buildHistoryEntry(planMock1, planMock2, 1L);

        assertThat("History record not generated:", history, is(notNullValue()));

        List<HistoryDetail> historyDetails = history.getHistoryDetailSet();

        assertThat("Unexpected number of history details", historyDetails.size(), is(16));

        HistoryDetail historyDetail1 = getHistoryDetails("planName", historyDetails);
        assertThat(historyDetail1.getOldValue(), is("planMock1"));
        assertThat(historyDetail1.getNewValue(), is("planMock2"));
        assertThat(historyDetail1.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail1.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail1.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail1.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail2 = getHistoryDetails("privacy.privacyName", historyDetails);
        assertThat(historyDetail2.getOldValue(), is("privacyMock1"));
        assertThat(historyDetail2.getNewValue(), is("privacyMock2"));
        assertThat(historyDetail2.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail2.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail2.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail2.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail3 = getHistoryDetails("status.statusName", historyDetails);
        assertThat(historyDetail3.getOldValue(), is("statusMock1"));
        assertThat(historyDetail3.getNewValue(), is("statusMock2"));
        assertThat(historyDetail3.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail3.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail3.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail3.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail4 = getHistoryDetails("status.subStatus.subStatusName", historyDetails);
        assertThat(historyDetail4.getOldValue(), is("subStatusMock1"));
        assertThat(historyDetail4.getNewValue(), is("subStatusMock2"));
        assertThat(historyDetail4.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail4.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail4.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail4.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail5 = getHistoryDetails("workUnitList.[1]", historyDetails);
        assertThat(historyDetail5.getOldValue(), is("workUnitMock1A"));
        assertThat(historyDetail5.getNewValue(), is(nullValue()));
        assertThat(historyDetail5.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail5.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail5.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail5.getNote(), is(ChangeType.REMOVED.getLabel()));

        HistoryDetail historyDetail6 = getHistoryDetails("workUnitList.[1].shortName", historyDetails);
        assertThat(historyDetail6.getOldValue(), is("workUnitMockShort1A"));
        assertThat(historyDetail6.getNewValue(), is(nullValue()));
        assertThat(historyDetail6.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail6.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail6.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail6.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail7 = getHistoryDetails("workUnitList.[1].workUnitName", historyDetails);
        assertThat(historyDetail7.getOldValue(), is("workUnitMock1A"));
        assertThat(historyDetail7.getNewValue(), is(nullValue()));
        assertThat(historyDetail7.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail7.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail7.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail7.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail8 = getHistoryDetails("workUnitList.[2]", historyDetails);
        assertThat(historyDetail8.getOldValue(), is("workUnitMock1B"));
        assertThat(historyDetail8.getNewValue(), is(nullValue()));
        assertThat(historyDetail8.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail8.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail8.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail8.getNote(), is(ChangeType.REMOVED.getLabel()));

        HistoryDetail historyDetail9 = getHistoryDetails("workUnitList.[2].shortName", historyDetails);
        assertThat(historyDetail9.getOldValue(), is("workUnitMockShort1B"));
        assertThat(historyDetail9.getNewValue(), is(nullValue()));
        assertThat(historyDetail9.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail9.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail9.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail9.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail10 = getHistoryDetails("workUnitList.[2].workUnitName", historyDetails);
        assertThat(historyDetail10.getOldValue(), is("workUnitMock1B"));
        assertThat(historyDetail10.getNewValue(), is(nullValue()));
        assertThat(historyDetail10.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail10.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail10.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail10.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail11 = getHistoryDetails("workUnitList.[3]", historyDetails);
        assertThat(historyDetail11.getOldValue(), is(nullValue()));
        assertThat(historyDetail11.getNewValue(), is("workUnitMock2A"));
        assertThat(historyDetail11.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail11.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail11.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail11.getNote(), is(ChangeType.ADDED.getLabel()));

        HistoryDetail historyDetail12 = getHistoryDetails("workUnitList.[3].shortName", historyDetails);
        assertThat(historyDetail12.getOldValue(), is(nullValue()));
        assertThat(historyDetail12.getNewValue(), is("workUnitMockShort2A"));
        assertThat(historyDetail12.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail12.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail12.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail12.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail13 = getHistoryDetails("workUnitList.[3].workUnitName", historyDetails);
        assertThat(historyDetail13.getOldValue(), is(nullValue()));
        assertThat(historyDetail13.getNewValue(), is("workUnitMock2A"));
        assertThat(historyDetail13.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail13.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail13.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail13.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail14 = getHistoryDetails("workUnitList.[4]", historyDetails);
        assertThat(historyDetail14.getOldValue(), is(nullValue()));
        assertThat(historyDetail14.getNewValue(), is("workUnitMock2B"));
        assertThat(historyDetail14.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail14.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail14.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail14.getNote(), is(ChangeType.ADDED.getLabel()));

        HistoryDetail historyDetail15 = getHistoryDetails("workUnitList.[4].shortName", historyDetails);
        assertThat(historyDetail15.getOldValue(), is(nullValue()));
        assertThat(historyDetail15.getNewValue(), is("workUnitMockShort2B"));
        assertThat(historyDetail15.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail15.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail15.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail15.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));

        HistoryDetail historyDetail16 = getHistoryDetails("workUnitList.[4].workUnitName", historyDetails);
        assertThat(historyDetail16.getOldValue(), is(nullValue()));
        assertThat(historyDetail16.getNewValue(), is("workUnitMock2B"));
        assertThat(historyDetail16.getReferenceEntity(), is(nullValue()));
        assertThat(historyDetail16.getOldValueEntityId(), is(nullValue()));
        assertThat(historyDetail16.getNewValueEntityId(), is(nullValue()));
        assertThat(historyDetail16.getNote(), is(ChangeType.CHANGED_FIELD.getLabel()));
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