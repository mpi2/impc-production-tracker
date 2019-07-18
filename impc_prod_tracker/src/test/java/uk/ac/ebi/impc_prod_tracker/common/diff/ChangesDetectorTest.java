package uk.ac.ebi.impc_prod_tracker.common.diff;

import lombok.Data;
import org.junit.Test;

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
    private final ClassTest class3NestedDataDifferent = new ClassTest("a", "b", 1, 4, 20);

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
        System.out.println("changeEntryList: "+changeEntryList);

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
    public void testGetChangesWhenDiffComplexProperties()
    {
        ChangesDetector<ClassTest> changesDetector =
            new ChangesDetector<>(
                new ArrayList<>(),
                class1NestedData,
                class3NestedDataDifferent);

        List<ChangeEntry> changeEntryList = changesDetector.getChanges();
        System.out.println("changeEntryList: "+changeEntryList);

        ChangeEntry expectedChangeEntry1 =
            getChangeEntry("property3.innerProperty3.innerInnerProperty1", "5", "20");
        ChangeEntry expectedChangeEntry2 =
            getChangeEntry("property3.innerProperty2", "2", "4");

        assertThat("Unexpected number of changes:", changeEntryList.size(), is(2));

        ChangeEntry obtainedChangeEntry1 = changeEntryList.get(1);
        ChangeEntry obtainedChangeEntry2 = changeEntryList.get(0);

        validateObtainedChangeEntryIsExpected(expectedChangeEntry1, obtainedChangeEntry1);
        validateObtainedChangeEntryIsExpected(expectedChangeEntry2, obtainedChangeEntry2);
    }

    private ChangeEntry getChangeEntry(String property, String oldValue, String newValue)
    {
        ChangeEntry expectedChangeEntry = new ChangeEntry();
        expectedChangeEntry.setProperty(property);
        expectedChangeEntry.setOldValue(oldValue);
        expectedChangeEntry.setNewValue(newValue);

        return expectedChangeEntry;
    }

    private void validateObtainedChangeEntryIsExpected(
        ChangeEntry expectedChangeEntry, ChangeEntry obtainedChangeEntry)
    {
        assertThat(
            "Property:", obtainedChangeEntry.getProperty(), is(expectedChangeEntry.getProperty()));
        assertThat(
            "Old Value", obtainedChangeEntry.getOldValue(), is(expectedChangeEntry.getOldValue()));
        assertThat(
            "New Value", obtainedChangeEntry.getNewValue(), is(expectedChangeEntry.getNewValue()));
    }

    @Data
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