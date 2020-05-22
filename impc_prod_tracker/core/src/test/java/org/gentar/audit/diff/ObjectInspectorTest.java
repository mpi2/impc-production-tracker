package org.gentar.audit.diff;

import lombok.Data;
import org.hamcrest.Matchers;
import org.gentar.biology.plan.Plan;
import org.gentar.organization.work_unit.WorkUnit;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;

public class ObjectInspectorTest
{
    private ObjectInspector objectInspector;

    @Test
    public void testGetPropertyValueMapSimpleValues()
    {
        ClassA classA =  new ClassA();
        classA.a1 = "a1";
        classA.a2 = 1;
        classA.a3 = LocalDateTime.now();
        ClassB classB = new ClassB();
        classB.b1 = 2.0;
        classA.a4 = classB;
        classA.a5 = Arrays.asList(classB);
        ObjectInspector objectInspector = new ObjectInspector(classA, Arrays.asList("id"));

        Map<String, Object> propertyValueMap = objectInspector.getValuesForSimpleProperties();

        assertThat("propertyValueMap:", propertyValueMap, is(notNullValue()));
        assertThat("Incorrect value:", propertyValueMap.get("a1"), is("a1"));
        assertThat("Incorrect value:", propertyValueMap.get("a2"), is(1));
        assertThat("Incorrect value:", propertyValueMap.get("a3"), is(classA.a3));
        assertThat("Incorrect value:", propertyValueMap.get("a4"), is(nullValue()));
        assertThat("Incorrect value:", propertyValueMap.get("a4.b1"), is(2.0));
    }

    @Test
    public void testGetPropertyValueMapAllValues()
    {
        ClassA classA =  new ClassA();
        classA.a1 = "a1";
        classA.a2 = 1;
        classA.a3 = LocalDateTime.now();
        ClassB classB = new ClassB();
        classB.b1 = 2.0;
        classA.a4 = classB;
        objectInspector = new ObjectInspector(classA, Arrays.asList("id"));
        Map<String, PropertyDescription> propertyValueMap = objectInspector.getMap();

        PropertyDescription expectedResult1 = new PropertyDescription("a1", String.class, "a1", true);
        PropertyDescription expectedResult2 = new PropertyDescription(1, Integer.class, "a2", true);
        PropertyDescription expectedResult3 =
            new PropertyDescription(classA.a3, LocalDateTime.class, "a3", true);
        PropertyDescription expectedResult4 =
            new PropertyDescription(classA.a4, ClassB.class, "a4", false);
        PropertyDescription expectedResult5 =
            new PropertyDescription(2.0, Double.class, "a4.b1", true);

        assertThat("propertyValueMap:", propertyValueMap, is(notNullValue()));
        assertThat("Incorrect value:", propertyValueMap.get("a1"), Matchers.is(expectedResult1));
        assertThat("Incorrect value:", propertyValueMap.get("a2"), Matchers.is(expectedResult2));
        assertThat("Incorrect value:", propertyValueMap.get("a3"), Matchers.is(expectedResult3));
        assertThat("Incorrect value:", propertyValueMap.get("a4"), Matchers.is(expectedResult4));
        assertThat("Incorrect value:", propertyValueMap.get("a4.b1"), Matchers.is(expectedResult5));
    }

    @Test
    public void testNested()
    {
        ClassC object = new ClassC();
        objectInspector = new ObjectInspector(object, Collections.singletonList(""));
        Map<String, PropertyDescription> map = objectInspector.getMap();

        assertThat(map.size(), is(3));

        PropertyDescription propertyDescriptionC1 = map.get("c1");
        assertThat(propertyDescriptionC1.getType(), is(ClassD.class));
        assertThat(propertyDescriptionC1.getName(), is("c1"));
        assertThat(propertyDescriptionC1.getValue(), is(nullValue()));
        assertThat(propertyDescriptionC1.isSimpleValue(), is(false));

        PropertyDescription propertyDescriptionC1D1 = map.get("c1.d1");
        assertThat(propertyDescriptionC1D1.getType(), is(ClassE.class));
        assertThat(propertyDescriptionC1D1.getName(), is("c1.d1"));
        assertThat(propertyDescriptionC1D1.getValue(), is(nullValue()));
        assertThat(propertyDescriptionC1D1.isSimpleValue(), is(false));

        PropertyDescription propertyDescriptionC1D1E1 = map.get("c1.d1.e1");
        assertThat(propertyDescriptionC1D1E1.getType(), is(String.class));
        assertThat(propertyDescriptionC1D1E1.getName(), is("c1.d1.e1"));
        assertThat(propertyDescriptionC1D1E1.getValue(), is(nullValue()));
        assertThat(propertyDescriptionC1D1E1.isSimpleValue(), is(true));
    }

    @Test
    public void testWithBusinessObject()
    {
        Plan plan = new Plan();
        plan.setId(1L);
        WorkUnit workUnit = new WorkUnit("workUnit_a");
        workUnit.setId(10L);
        plan.setWorkUnit(workUnit);
        objectInspector = new ObjectInspector(plan, Collections.singletonList(""));
        Map<String, PropertyDescription> map = objectInspector.getMap();
        assertThat(map, is(notNullValue()));
        assertThat(map.size(), greaterThan(0));
        PropertyDescription propertyDescriptionWorkUnit = map.get("workUnit");
        assertThat(propertyDescriptionWorkUnit.getType(), is(WorkUnit.class));
        assertThat(propertyDescriptionWorkUnit.getName(), is("workUnit"));
        assertThat(propertyDescriptionWorkUnit.getValue(), is(workUnit));
        assertThat(propertyDescriptionWorkUnit.isSimpleValue(), is(false));
    }

    @Test
    public void testObjectWithList()
    {
        ClassA a = new ClassA();
        ClassB b = new ClassB();
        b.setB1(1.0);
        b.setB2(new ClassA());
        List<ClassB> list = Arrays.asList(b);
        a.setA5(list);
        objectInspector = new ObjectInspector(a, Arrays.asList(""));
        Map<String, PropertyDescription> map = objectInspector.getMap();

        assertThat(map.size(), is(7));

        PropertyDescription propertyDescriptionA1 = map.get("a1");
        assertThat(propertyDescriptionA1.getType(), is(String.class));
        assertThat(propertyDescriptionA1.getName(), is("a1"));
        assertThat(propertyDescriptionA1.getValue(), is(nullValue()));
        assertThat(propertyDescriptionA1.isSimpleValue(), is(true));

        PropertyDescription propertyDescriptionA2 = map.get("a2");
        assertThat(propertyDescriptionA2.getType(), is(Integer.class));
        assertThat(propertyDescriptionA2.getName(), is("a2"));
        assertThat(propertyDescriptionA2.getValue(), is(nullValue()));
        assertThat(propertyDescriptionA2.isSimpleValue(), is(true));

        PropertyDescription propertyDescriptionA3 = map.get("a3");
        assertThat(propertyDescriptionA3.getType(), is(LocalDateTime.class));
        assertThat(propertyDescriptionA3.getName(), is("a3"));
        assertThat(propertyDescriptionA3.getValue(), is(nullValue()));
        assertThat(propertyDescriptionA3.isSimpleValue(), is(true));

        PropertyDescription propertyDescriptionA4 = map.get("a4");
        assertThat(propertyDescriptionA4.getType(), is(ClassB.class));
        assertThat(propertyDescriptionA4.getName(), is("a4"));
        assertThat(propertyDescriptionA4.getValue(), is(nullValue()));
        assertThat(propertyDescriptionA4.isSimpleValue(), is(false));

        PropertyDescription propertyDescriptionA4B2 = map.get("a4.b2");
        assertThat(propertyDescriptionA4B2.getType(), is(ClassA.class));
        assertThat(propertyDescriptionA4B2.getName(), is("a4.b2"));
        assertThat(propertyDescriptionA4B2.getValue(), is(nullValue()));
        assertThat(propertyDescriptionA4B2.isSimpleValue(), is(false));

        PropertyDescription propertyDescriptionA5 = map.get("a5");
        assertThat(propertyDescriptionA5.getType(), is(List.class));
        assertThat(propertyDescriptionA5.getName(), is("a5"));
        assertThat(propertyDescriptionA5.getValue(), is(list));
        assertThat(propertyDescriptionA5.isSimpleValue(), is(false));

        PropertyDescription propertyDescriptionA4B1 = map.get("a4.b1");
        assertThat(propertyDescriptionA4B1.getType(), is(Double.class));
        assertThat(propertyDescriptionA4B1.getName(), is("a4.b1"));
        assertThat(propertyDescriptionA4B1.getValue(), is(nullValue()));
        assertThat(propertyDescriptionA4B1.isSimpleValue(), is(true));
    }

    @Data
    public class ClassA
    {
        private String a1;
        private Integer a2;
        private LocalDateTime a3;
        private ClassB a4;
        private List<ClassB> a5;
    }

    @Data
    public class ClassB
    {
        private Double b1;
        private ClassA b2;
    }

    @Data
    public class ClassC
    {
        private ClassD c1;
    }

    @Data
    public class ClassD
    {
        private ClassE d1;
    }

    @Data
    public class ClassE
    {
        private String e1;
    }
}