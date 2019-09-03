package uk.ac.ebi.impc_prod_tracker.common.diff;

import lombok.Data;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PropertyCheckerTest
{

    private ClassA classA = new ClassA("a", LocalDateTime.now());
    @Test
    public void testGetPropertyNamesList()
    {
        List<String> props = PropertyChecker.getPropertyNamesList(classA);

        assertThat("", props.size(), is(4));
        assertThat("", props.contains("p1"), is(true));
        assertThat("", props.contains("p2"), is(true));
        assertThat("", props.contains("p3"), is(true));
        assertThat("", props.contains("p4"), is(true));
    }

    @Test
    public void testGetPropertiesByType()
    {
        List<String> props = PropertyChecker.getPropertiesByType(classA.getClass());
        System.out.println(props);
        assertThat("", props.size(), is(4));
        assertThat("", props.contains("p1"), is(true));
        assertThat("", props.contains("p2"), is(true));
        assertThat("", props.contains("p3"), is(true));
        assertThat("", props.contains("p4"), is(true));
    }



    @Test
    public void testGetPropertyType()
    {
        assertThat("Wrong type", PropertyChecker.getPropertyType(classA, "p1"), is(String.class));
        assertThat(
            "Wrong type", PropertyChecker.getPropertyType(classA, "p2"), is(LocalDateTime.class));
        assertThat("Wrong type", PropertyChecker.getPropertyType(classA, "p3"), is(ClassA.class));
        assertThat("Wrong type", PropertyChecker.getPropertyType(classA, "p4"), is(List.class));
    }

    @Test
    public void testGetValue()
    {
        assertThat("Wrong value", PropertyChecker.getValue("p1", classA), is("a"));
        assertThat("Wrong value", PropertyChecker.getValue("p2", classA), is(classA.p2));
        assertThat("Wrong value", PropertyChecker.getValue("p3", classA), is(nullValue()));
        assertThat("Wrong value", PropertyChecker.getValue("p4", classA), is(nullValue()));
    }

    @Test
    public void testIsCollection()
    {
        assertThat(
            "Wrong value",
            PropertyChecker.isCollection(PropertyChecker.getPropertyType(classA, "p1")),
            is(false));
        assertThat(
            "Wrong value",
            PropertyChecker.isCollection(PropertyChecker.getPropertyType(classA, "p4")),
            is(true));
    }

    @Test
    public void testIsASimpleValue()
    {
        assertThat(
            "Wrong value",
            PropertyChecker.isASimpleValue(PropertyChecker.getPropertyType(classA, "p1")),
            is(true));
        assertThat(
            "Wrong value",
            PropertyChecker.isASimpleValue(PropertyChecker.getPropertyType(classA, "p4")),
            is(false));
    }

    @Data
    public class ClassA
    {
        private String p1;
        private LocalDateTime p2;
        private ClassA p3;
        private List<Integer> p4;

        public ClassA(String p1, LocalDateTime p2)
        {
            this.p1 = p1;
            this.p2 = p2;
        }
    }
}