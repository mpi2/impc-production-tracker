package uk.ac.ebi.impc_prod_tracker.common.history;

import org.junit.Test;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangeEntry;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangesDetector;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PropertyMapGrouperTest
{
    private PropertyMapGrouper testInstance = new PropertyMapGrouper();

    @Test
    public void test()
    {
        List<ChangeEntry> changes = buildChangeList();
        Map<String, Map<String, ChangeEntry>> groupedProps = testInstance.getGroupedChanges(changes);

        assertThat("Map size:", groupedProps.size(), is(3));

        assertThat("Map content:", groupedProps.get("root"), is(notNullValue()));
        assertThat("Map content:", groupedProps.get("root").size(), is(1));
        assertThat("Map content:", groupedProps.get("root").get("tpn"), is(notNullValue()));

        assertThat("Map content:", groupedProps.get("type"), is(notNullValue()));
        assertThat("Map size:", groupedProps.get("type").size(), is(3));
        assertThat("Map content:", groupedProps.get("type").get("type"), is(notNullValue()));
        assertThat("Map content:", groupedProps.get("type").get("type.name"), is(notNullValue()));
        assertThat("Map content:", groupedProps.get("type").get("type.id"), is(notNullValue()));

        assertThat("Map content:", groupedProps.get("type.subtype"), is(notNullValue()));
        assertThat("Map size:", groupedProps.get("type.subtype").size(), is(3));
        assertThat("Map content:", groupedProps.get("type.subtype").get("type.subtype"), is(notNullValue()));
        assertThat("Map content:", groupedProps.get("type.subtype").get("type.subtype.name"), is(notNullValue()));
        assertThat("Map content:", groupedProps.get("type.subtype").get("type.subtype.id"), is(notNullValue()));
    }

    @Test
    public void testWithCalculatedData()
    {
        Plan plan1 = new Plan();
        plan1.setId(1L);
        plan1.setPin("pin1");
        Plan plan2 = new Plan();
        plan2.setId(1L);
        plan2.setPin("pin2");

        WorkUnit workUnit1 = new WorkUnit("name1");
        workUnit1.setId(1L);
        plan1.setWorkUnit(workUnit1);
        WorkUnit workUnit2 = new WorkUnit("name2");
        workUnit2.setId(1L);
        plan2.setWorkUnit(workUnit2);
        ChangesDetector<Plan> changesDetector = new ChangesDetector<>(Arrays.asList(), plan1, plan2);
        List<ChangeEntry> changeEntries = changesDetector.getChanges();
        changesDetector.print();
        Map<String, Map<String, ChangeEntry>> groupedProps =
            testInstance.getGroupedChanges(changeEntries);
        System.out.println(groupedProps);
        testInstance.print();
    }

    private List<ChangeEntry> buildChangeList()
    {
        List<ChangeEntry> changes = new ArrayList<>();

        ChangeEntry ch1 = new ChangeEntry();
        ch1.setProperty("tpn");
        ch1.setType(String.class);
        ch1.setOldValue("tp1");
        ch1.setNewValue("tp2");

        ChangeEntry ch2 = new ChangeEntry();
        ch2.setProperty("type");
        ch2.setType(Type.class);
        ch2.setOldValue("S1");
        ch2.setNewValue(null);

        ChangeEntry ch3 = new ChangeEntry();
        ch3.setProperty("type.id");
        ch3.setType(Long.class);
        ch3.setOldValue(1);
        ch3.setNewValue(3);

        ChangeEntry ch4 = new ChangeEntry();
        ch4.setProperty("type.name");
        ch4.setType(String.class);
        ch4.setOldValue("S1");
        ch4.setNewValue(null);

        ChangeEntry ch5 = new ChangeEntry();
        ch5.setProperty("type.subtype");
        ch5.setType(SubType.class);
        ch5.setOldValue("S1");
        ch5.setNewValue(null);

        ChangeEntry ch6 = new ChangeEntry();
        ch6.setProperty("type.subtype.id");
        ch6.setType(Long.class);
        ch6.setOldValue(1);
        ch6.setNewValue(2);

        ChangeEntry ch7 = new ChangeEntry();
        ch7.setProperty("type.subtype.name");
        ch7.setType(String.class);
        ch7.setOldValue("S12");
        ch7.setNewValue(null);

        changes.add(ch1);
        changes.add(ch2);
        changes.add(ch3);
        changes.add(ch4);
        changes.add(ch5);
        changes.add(ch6);
        changes.add(ch7);

        return changes;
    }

    private class Type
    {
        private Long id;
        private String name;
        private SubType subtype;
    }

    private class SubType
    {
        private Long id;
        private String name;
    }
}