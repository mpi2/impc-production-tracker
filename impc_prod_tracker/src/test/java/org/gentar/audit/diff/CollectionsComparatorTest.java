package org.gentar.audit.diff;

import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CollectionsComparatorTest
{
    ClassA classJustCreated1;
    ClassA classJustCreated2;
    ClassA class1;
    ClassA class2;
    ClassA class3;
    ClassA class4;

    @Before
    public void setup()
    {
        classJustCreated1 = new ClassA();
        classJustCreated1.setId(null);
        classJustCreated1.setName("justCreated1");

        classJustCreated2 = new ClassA();
        classJustCreated2.setId(null);
        classJustCreated2.setName("justCreated2");

        class1 = new ClassA();
        class1.setId(1L);
        class1.setName("name1");

        class2 = new ClassA();
        class2.setId(2L);
        class2.setName("name2");

        class3 = new ClassA();
        class3.setId(3L);
        class3.setName("name3");

        class4 = new ClassA();
        class4.setId(4L);
        class4.setName("name4");
    }

    @Test
    public void testCompareEmptySets()
    {
        Set<ClassA> set1 = new HashSet<>();
        Set<ClassA> set2 = new HashSet<>();
        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();

        assertThat("No changes were expected:", changes.isEmpty(), is(true));
    }

    @Test
    public void testCompareSameUnitarySets()
    {
        Set<ClassA> set1 = new HashSet<>();
        set1.add(class1);
        Set<ClassA> set2 = new HashSet<>();
        set2.add(class1);
        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();

        assertThat("No changes were expected:", changes.isEmpty(), is(true));
    }

    @Test
    public void testCompareUnitarySetWithEmptySet()
    {
        Set<ClassA> set1 = new HashSet<>();
        set1.add(class1);
        Set<ClassA> set2 = new HashSet<>();
        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();
        System.out.println(changes);

        assertThat("Exactly one change is expected:", changes.size(), is(1));
        ChangeEntry change = changes.get(0);
        assertThat("Unexpected property name", change.getProperty(), is("collection#1"));
        assertThat("Unexpected old value", change.getOldValue(), is(class1));
        assertThat("Unexpected new value", change.getNewValue(), is(nullValue()));
    }

    @Test
    public void testCompareUnitarySetWithDifferentUnitarySet()
    {
        Set<ClassA> set1 = new HashSet<>();
        set1.add(class1);
        Set<ClassA> set2 = new HashSet<>();
        set2.add(class2);
        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();

        assertThat("Exactly 2 changes are expected:", changes.size(), is(2));

        ChangeEntry change1 = getChange("collection#1", changes);
        assertThat("Unexpected property name", change1.getProperty(), is("collection#1"));
        assertThat("Unexpected old value", change1.getOldValue(), is(class1));
        assertThat("Unexpected new value", change1.getNewValue(), is(nullValue()));

        ChangeEntry change2 = getChange("collection#2", changes);
        assertThat("Unexpected property name", change2.getProperty(), is("collection#2"));
        assertThat("Unexpected old value", change2.getOldValue(), is(nullValue()));
        assertThat("Unexpected new value", change2.getNewValue(), is(class2));
    }

    @Test
    public void testAddedElementToSet()
    {
        Set<ClassA> set1 = new HashSet<>();
        set1.add(class1);
        Set<ClassA> set2 = new HashSet<>();
        set2.add(class1);
        set2.add(class2);
        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();

        assertThat("Exactly 1 change is expected:", changes.size(), is(1));

        ChangeEntry change2 = getChange("collection#2", changes);
        assertThat("Unexpected property name", change2.getProperty(), is("collection#2"));
        assertThat("Unexpected old value", change2.getOldValue(), is(nullValue()));
        assertThat("Unexpected new value", change2.getNewValue(), is(class2));
    }

    @Test
    public void testRemovedElementToSet()
    {
        Set<ClassA> set1 = new HashSet<>();
        set1.add(class1);
        set1.add(class2);
        Set<ClassA> set2 = new HashSet<>();
        set2.add(class1);

        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();

        assertThat("Exactly 1 change is expected:", changes.size(), is(1));

        ChangeEntry change1 = getChange("collection#2", changes);
        assertThat("Unexpected property name", change1.getProperty(), is("collection#2"));
        assertThat("Unexpected old value", change1.getOldValue(), is(class2));
        assertThat("Unexpected new value", change1.getNewValue(), is(nullValue()));
    }

    @Test
    public void testRemovedElementToSetAndAddingNewElement()
    {
        Set<ClassA> set1 = new HashSet<>();
        set1.add(class1);
        set1.add(class2);
        Set<ClassA> set2 = new HashSet<>();
        set2.add(classJustCreated1);
        set2.add(class1);

        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();

        assertThat("Exactly 1 change is expected:", changes.size(), is(2));

        ChangeEntry change1 = getChange("collection#2", changes);
        assertThat("Unexpected property name", change1.getProperty(), is("collection#2"));
        assertThat("Unexpected old value", change1.getOldValue(), is(class2));
        assertThat("Unexpected new value", change1.getNewValue(), is(nullValue()));

        ChangeEntry change2 = getChange("collection#-1", changes);
        assertThat("Unexpected property name", change2.getProperty(), is("collection#-1"));
        assertThat("Unexpected old value", change2.getOldValue(), is(nullValue()));
        assertThat("Unexpected new value", change2.getNewValue(), is(classJustCreated1));
    }

    @Test
    public void testAddedNewElementToSet()
    {
        Set<ClassA> set1 = new HashSet<>();
        set1.add(class1);
        Set<ClassA> set2 = new HashSet<>();
        set2.add(classJustCreated1);
        set2.add(classJustCreated2);
        set2.add(class1);

        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();
        System.out.println(changes);

        assertThat("Exactly 2 change is expected:", changes.size(), is(2));

        ChangeEntry change1 = getChange("collection#-1", changes);
        assertThat("Unexpected property name", change1.getProperty(), is("collection#-1"));
        assertThat("Unexpected old value", change1.getOldValue(), is(nullValue()));
        assertThat("Unexpected new value", change1.getNewValue(), is(classJustCreated1));

        ChangeEntry change2 = getChange("collection#-2", changes);
        assertThat("Unexpected property name", change2.getProperty(), is("collection#-2"));
        assertThat("Unexpected old value", change2.getOldValue(), is(nullValue()));
        assertThat("Unexpected new value", change2.getNewValue(), is(classJustCreated2));
    }

    @Test
    public void testCompareSameUnitarySetsWithElementsChanged()
    {
        Set<ClassA> set1 = new HashSet<>();
        set1.add(class1);
        Set<ClassA> set2 = new HashSet<>();
        ClassA newClass1 = new ClassA();
        newClass1.setId(1L);
        newClass1.setName("name1New");
        set2.add(newClass1);
        CollectionsComparator<ClassA> collectionsComparator =
            new CollectionsComparator<>("collection", set1, set2);

        List<ChangeEntry> changes = collectionsComparator.getChanges();
        System.out.println(changes);

        assertThat("Exactly 1 change is expected:", changes.size(), is(1));
        ChangeEntry change = getChange("collection#1", changes);
        assertThat("Unexpected property name", change.getProperty(), is("collection#1"));
        assertThat("Unexpected old value", change.getOldValue(), is(class1));
        assertThat("Unexpected new value", change.getNewValue(), is(newClass1));
    }

    private ChangeEntry getChange(
        String propertyName, List<ChangeEntry> changeEntries)
    {
        Optional<ChangeEntry> changeEntryOptional =
            changeEntries.stream().filter(x -> x.getProperty().equals(propertyName)).findFirst();
        return changeEntryOptional.orElse(null);
    }

    @Data
    public static class ClassA
    {
        Long id;
        String name;
    }
}