package uk.ac.ebi.impc_prod_tracker.common.diff;

import lombok.Data;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
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
        objectInspector.printSimple();

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
        Map<String, PropertyValueData> propertyValueMap = objectInspector.getMap();


        PropertyValueData expectedResult1 = new PropertyValueData("a1", String.class, "a1", true);
        PropertyValueData expectedResult2 = new PropertyValueData(1, Integer.class, "a2", true);
        PropertyValueData expectedResult3 =
            new PropertyValueData(classA.a3, LocalDateTime.class, "a3", true);
        PropertyValueData expectedResult4 =
            new PropertyValueData(classA.a4, ClassB.class, "a4", false);
        PropertyValueData expectedResult5 =
            new PropertyValueData(2.0, Double.class, "a4.b1", true);

        assertThat("propertyValueMap:", propertyValueMap, is(notNullValue()));
        assertThat("Incorrect value:", propertyValueMap.get("a1"), is(expectedResult1));
        assertThat("Incorrect value:", propertyValueMap.get("a2"), is(expectedResult2));
        assertThat("Incorrect value:", propertyValueMap.get("a3"), is(expectedResult3));
        assertThat("Incorrect value:", propertyValueMap.get("a4"), is(expectedResult4));
        assertThat("Incorrect value:", propertyValueMap.get("a4.b1"), is(expectedResult5));
    }

    @Test
    public void testNested()
    {
        ClassC object = new ClassC();
        objectInspector = new ObjectInspector(object, Arrays.asList(""));
        Map<String, PropertyValueData> map = objectInspector.getMap();

        assertThat("Size:", map.size(), is(3));
        assertThat("Size:", map.get("c1"), is(notNullValue()));
        assertThat("Size:", map.get("c1.d1"), is(notNullValue()));
        assertThat("Size:", map.get("c1.d1.e1"), is(notNullValue()));
        objectInspector.printSimple();
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