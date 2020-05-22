package org.gentar.audit.diff;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ChangesDetectorTest
{
    private final ClassTest class1NoNestedData = new ClassTest("a", "b");
    private final ClassTest class2NoNestedData = new ClassTest("a", "b");
    private final ClassTest class3NoNestedDataDifferent = new ClassTest("a", "c");
    private final ClassTest class1NestedData = new ClassTest("a", "b", 1, 2, 5);
    private final ClassTest class2NestedData = new ClassTest("a", "b", 1, 2, 5);

    @Test
    public void testGetChangesWhenSameSimpleProperties()
    {
        ChangesDetector<ClassTest> changesDetector =
            new ChangesDetector<>(
                new ArrayList<>(),
                class1NoNestedData,
                class2NoNestedData);

        List<ChangeEntry> changeEntryList = changesDetector.getChanges();

        assertThat("", changeEntryList.isEmpty());
    }

    @Test
    public void testGetChangesWhenDiffSimpleProperties()
    {
        ChangesDetector<ClassTest> changesDetector =
            new ChangesDetector<>(
                new ArrayList<>(),
                class1NoNestedData,
                class3NoNestedDataDifferent);

        List<ChangeEntry> changeEntryList = changesDetector.getChanges();

        ChangeEntry expectedChangeEntry = getChangeEntry("property2", "b", "c");

        assertThat("Unexpected number of changes:", changeEntryList.size(), is(1));

        ChangeEntry obtainedChangeEntry = changeEntryList.get(0);

        validateObtainedChangeEntryIsExpected(expectedChangeEntry, obtainedChangeEntry);
    }

    @Test
    public void testGetChangesWhenDiffSimplePropertiesIgnoringChangedField()
    {
        ChangesDetector<ClassTest> changesDetector =
            new ChangesDetector<>(
                Arrays.asList("property2"),
                class1NoNestedData,
                class3NoNestedDataDifferent);

        List<ChangeEntry> changeEntryList = changesDetector.getChanges();

        assertThat("Unexpected number of changes:", changeEntryList.size(), is(0));
    }

    @Test
    public void testGetChangesWhenSameComplexProperties()
    {
        ChangesDetector<ClassTest> changesDetector =
            new ChangesDetector<>(
                new ArrayList<>(),
                class1NestedData,
                class2NestedData);

        List<ChangeEntry> changeEntryList = changesDetector.getChanges();

        assertThat("", changeEntryList.isEmpty());
    }

    @Test
    public void testGetChangesWhenDiffComplexPropertiesDiffId()
    {
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


        ChangesDetector<PlanMock> changesDetector =
            new ChangesDetector<>(
                new ArrayList<>(),
                planMock1,
                planMock2);

        List<ChangeEntry> changeEntryList = changesDetector.getChanges();
        changesDetector.print();

        assertThat("Unexpected number of changes:", changeEntryList.size(), is(16));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "id"), getChangeEntry("id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "planName"),
            getChangeEntry("planName", "planMock1", "planMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy.id"),
            getChangeEntry("privacy.id",
                1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy"),
            getChangeEntry("privacy",
                privacyMock1, privacyMock2));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy.privacyName"),
            getChangeEntry("privacy.privacyName",
                "privacyMock1", "privacyMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status"),
            getChangeEntry("status", statusMock1, statusMock2));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.id"),
            getChangeEntry("status.id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.statusName"),
            getChangeEntry("status.statusName", "statusMock1", "statusMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus"),
            getChangeEntry("status.subStatus", subStatusMock1, subStatusMock2));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus.id"),
            getChangeEntry("status.subStatus.id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus.subStatusName"),
            getChangeEntry("status.subStatus.subStatusName", "subStatusMock1", "subStatusMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList"),
            getChangeEntry("workUnitList", Arrays.asList(workUnitMock1A, workUnitMock1B), Arrays.asList(workUnitMock2A, workUnitMock2B)));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList#1"),
            getChangeEntry("workUnitList#1", workUnitMock1A, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList#2"),
            getChangeEntry("workUnitList#2", workUnitMock1B, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList#3"),
            getChangeEntry("workUnitList#3", null, workUnitMock2A));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList#4"),
            getChangeEntry("workUnitList#4", null, workUnitMock2B));

    }

    @Test
    public void testGetChangesWhenDiffComplexPropertiesSameId()
    {
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
        planMock2.setId(1L);
        planMock2.setPlanName("planMock2");
        planMock2.setStatus(statusMock2);
        planMock2.setWorkUnitList(Arrays.asList(workUnitMock2A, workUnitMock2B));
        planMock2.setPrivacy(privacyMock2);

        ChangesDetector<PlanMock> changesDetector =
            new ChangesDetector<>(
                new ArrayList<>(),
                planMock1,
                planMock2);

        List<ChangeEntry> changeEntryList = changesDetector.getChanges();

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "planName"),
            getChangeEntry("planName", "planMock1", "planMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy.id"),
            getChangeEntry("privacy.id",
                1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy"),
            getChangeEntry("privacy",
                privacyMock1, privacyMock2));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy.privacyName"),
            getChangeEntry("privacy.privacyName",
                "privacyMock1", "privacyMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status"),
            getChangeEntry("status", statusMock1, statusMock2));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.id"),
            getChangeEntry("status.id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.statusName"),
            getChangeEntry("status.statusName", "statusMock1", "statusMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus"),
            getChangeEntry("status.subStatus", subStatusMock1, subStatusMock2));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus.id"),
            getChangeEntry("status.subStatus.id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus.subStatusName"),
            getChangeEntry("status.subStatus.subStatusName", "subStatusMock1", "subStatusMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList#1"),
            getChangeEntry("workUnitList#1", workUnitMock1A, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList#2"),
            getChangeEntry("workUnitList#2", workUnitMock1B, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList#3"),
            getChangeEntry("workUnitList#3", null, workUnitMock2A));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList#4"),
            getChangeEntry("workUnitList#4", null, workUnitMock2B));

    }

    private ChangeEntry getByPropertyName(List<ChangeEntry> changeEntries, String propertyName)
    {
        for (ChangeEntry changeEntry : changeEntries)
        {
            if (changeEntry.getProperty().equals(propertyName))
                return changeEntry;
        }
        return null;
    }

    private ChangeEntry getChangeEntry(String property, Object oldValue, Object newValue)
    {
        ChangeEntry expectedChangeEntry = new ChangeEntry();
        expectedChangeEntry.setProperty(property);
        expectedChangeEntry.setOldValue(oldValue);
        expectedChangeEntry.setNewValue(newValue);

        return expectedChangeEntry;
    }

    private void validateObtainedChangeEntryIsExpected(
        ChangeEntry obtainedChangeEntry, ChangeEntry expectedChangeEntry)
    {
        assertThat(
            "Property:", obtainedChangeEntry.getProperty(), is(expectedChangeEntry.getProperty()));
        assertThat(
            "Old Value", obtainedChangeEntry.getOldValue(), is(expectedChangeEntry.getOldValue()));
        assertThat(
            "New Value", obtainedChangeEntry.getNewValue(), is(expectedChangeEntry.getNewValue()));
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

    @Data
    @AllArgsConstructor
    public class ClassTest
    {
        private String property1;
        private String property2;
        private InnerClassTest property3;

        public ClassTest(String property1, String property2)
        {
            this.property1 = property1;
            this.property2 = property2;
        }

        public ClassTest(String property1, String property2, Integer innerProperty1, Integer innerProperty2)
        {
            this.property1 = property1;
            this.property2 = property2;
            this.property3 = new InnerClassTest(innerProperty1, innerProperty2);
        }

        ClassTest(
            String property1,
            String property2,
            Integer innerProperty1,
            Integer innerProperty2,
            Integer innerInnerProperty1)
        {
            this.property1 = property1;
            this.property2 = property2;
            this.property3 = new InnerClassTest(innerProperty1, innerProperty2, innerInnerProperty1);
        }

        @Data
        public class InnerClassTest
        {
            private Integer innerProperty1;
            private Integer innerProperty2;
            private InnerInnerClassTest innerProperty3;

            public InnerClassTest(Integer innerProperty1, Integer innerProperty2)
            {
                this.innerProperty1 = innerProperty1;
                this.innerProperty2 = innerProperty2;
            }

            public InnerClassTest(
                Integer innerProperty1, Integer innerProperty2, Integer innerInnerProperty1)
            {
                this.innerProperty1 = innerProperty1;
                this.innerProperty2 = innerProperty2;
                this.innerProperty3 = new InnerInnerClassTest(innerInnerProperty1);
            }

            @Data
            public class InnerInnerClassTest
            {
                private Integer innerInnerProperty1;

                public InnerInnerClassTest(Integer innerInnerProperty1)
                {
                    this.innerInnerProperty1 = innerInnerProperty1;
                }
            }
        }
    }
}