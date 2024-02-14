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

        ChangeEntry expectedChangeEntry = buildChangeEntry("property2", "b", "c");

        assertThat("Unexpected number of changes:", changeEntryList.size(), is(1));

        ChangeEntry obtainedChangeEntry = changeEntryList.getFirst();

        validateObtainedChangeEntryIsExpected(expectedChangeEntry, obtainedChangeEntry);
    }

    @Test
    public void testGetChangesWhenDiffSimplePropertiesIgnoringChangedField()
    {
        ChangesDetector<ClassTest> changesDetector =
            new ChangesDetector<>(
                    List.of("property2"),
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
            new ChangesDetector<>(new ArrayList<>(), planMock1, planMock2);

        List<ChangeEntry> changeEntryList = changesDetector.getChanges();

        assertThat("Unexpected number of changes:", changeEntryList.size(), is(24));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "id"), buildChangeEntry("id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "planName"),
            buildChangeEntry("planName", "planMock1", "planMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy.id"),
            buildChangeEntry("privacy.id",
                1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy.privacyName"),
            buildChangeEntry("privacy.privacyName",
                "privacyMock1", "privacyMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.id"),
            buildChangeEntry("status.id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.statusName"),
            buildChangeEntry("status.statusName", "statusMock1", "statusMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus.id"),
            buildChangeEntry("status.subStatus.id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus.subStatusName"),
            buildChangeEntry("status.subStatus.subStatusName", "subStatusMock1", "subStatusMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[1]"),
            buildChangeEntry("workUnitList.[1]", workUnitMock1A, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[1].id"),
            buildChangeEntry("workUnitList.[1].id", 1L, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[1].workUnitName"),
            buildChangeEntry("workUnitList.[1].workUnitName", "workUnitMock1A", null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[1].shortName"),
            buildChangeEntry("workUnitList.[1].shortName", "workUnitMockShort1A", null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[2]"),
            buildChangeEntry("workUnitList.[2]", workUnitMock1B, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[2].id"),
            buildChangeEntry("workUnitList.[2].id", 2L, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[2].workUnitName"),
            buildChangeEntry("workUnitList.[2].workUnitName", "workUnitMock1B", null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[2].shortName"),
            buildChangeEntry("workUnitList.[2].shortName", "workUnitMockShort1B", null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[3]"),
            buildChangeEntry("workUnitList.[3]", null, workUnitMock2A));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[3].id"),
            buildChangeEntry("workUnitList.[3].id", null, 3L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[3].workUnitName"),
            buildChangeEntry("workUnitList.[3].workUnitName", null, "workUnitMock2A"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[3].shortName"),
            buildChangeEntry("workUnitList.[3].shortName", null, "workUnitMockShort2A"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[4]"),
            buildChangeEntry("workUnitList.[4]", null, workUnitMock2B));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[4].id"),
            buildChangeEntry("workUnitList.[4].id", null, 4L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[4].workUnitName"),
            buildChangeEntry("workUnitList.[4].workUnitName", null, "workUnitMock2B"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[4].shortName"),
            buildChangeEntry("workUnitList.[4].shortName", null, "workUnitMockShort2B"));
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
            buildChangeEntry("planName", "planMock1", "planMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy.id"),
            buildChangeEntry("privacy.id",
                1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(
                changeEntryList, "privacy.privacyName"),
            buildChangeEntry("privacy.privacyName",
                "privacyMock1", "privacyMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.id"),
            buildChangeEntry("status.id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.statusName"),
            buildChangeEntry("status.statusName", "statusMock1", "statusMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus.id"),
            buildChangeEntry("status.subStatus.id", 1L, 2L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "status.subStatus.subStatusName"),
            buildChangeEntry("status.subStatus.subStatusName", "subStatusMock1", "subStatusMock2"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[1]"),
            buildChangeEntry("workUnitList.[1]", workUnitMock1A, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[1].id"),
            buildChangeEntry("workUnitList.[1].id", 1L, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[1].workUnitName"),
            buildChangeEntry("workUnitList.[1].workUnitName", "workUnitMock1A", null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[1].shortName"),
            buildChangeEntry("workUnitList.[1].shortName", "workUnitMockShort1A", null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[2]"),
            buildChangeEntry("workUnitList.[2]", workUnitMock1B, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[2].id"),
            buildChangeEntry("workUnitList.[2].id", 2L, null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[2].workUnitName"),
            buildChangeEntry("workUnitList.[2].workUnitName", "workUnitMock1B", null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[2].shortName"),
            buildChangeEntry("workUnitList.[2].shortName", "workUnitMockShort1B", null));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[3]"),
            buildChangeEntry("workUnitList.[3]", null, workUnitMock2A));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[3].id"),
            buildChangeEntry("workUnitList.[3].id", null, 3L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[3].workUnitName"),
            buildChangeEntry("workUnitList.[3].workUnitName", null, "workUnitMock2A"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[3].shortName"),
            buildChangeEntry("workUnitList.[3].shortName", null, "workUnitMockShort2A"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[4]"),
            buildChangeEntry("workUnitList.[4]", null, workUnitMock2B));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[4].id"),
            buildChangeEntry("workUnitList.[4].id", null, 4L));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[4].workUnitName"),
            buildChangeEntry("workUnitList.[4].workUnitName", null, "workUnitMock2B"));

        validateObtainedChangeEntryIsExpected(
            getByPropertyName(changeEntryList, "workUnitList.[4].shortName"),
            buildChangeEntry("workUnitList.[4].shortName", null, "workUnitMockShort2B"));
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

    private ChangeEntry buildChangeEntry(String property, Object oldValue, Object newValue)
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
    public static class WorkUnitMock
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
    public static class PrivacyMock
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
    public static class SubStatusMock
    {
        Long id;
        String subStatusName;
    }

    @Data
    @AllArgsConstructor
    public static class ClassTest
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
        public static class InnerClassTest
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
            public static class InnerInnerClassTest
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