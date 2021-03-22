package org.gentar.audit.diff;

import lombok.Data;
import lombok.ToString;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.hamcrest.Matchers;
import org.gentar.biology.plan.Plan;
import org.gentar.organization.work_unit.WorkUnit;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
    public void testSimpleClassNullValues()
    {
        SimpleClass simpleClass = new SimpleClass();

        ObjectInspector objectInspector = new ObjectInspector(simpleClass, Arrays.asList(""));

        var map = objectInspector.getMap();

        assertThat("Expected 3 properties:", map.size(), is(3));

        PropertyDescription propertyDescription1 = map.get("id");
        assertThat(propertyDescription1, is(notNullValue()));
        assertThat(propertyDescription1.getName(), is("id"));
        assertThat(propertyDescription1.getType(), is(Long.class));
        assertThat(propertyDescription1.getValue(), is(nullValue()));

        PropertyDescription propertyDescription2 = map.get("field1");
        assertThat(propertyDescription2, is(notNullValue()));
        assertThat(propertyDescription2.getName(), is("field1"));
        assertThat(propertyDescription2.getType(), is(String.class));
        assertThat(propertyDescription2.getValue(), is(nullValue()));

        PropertyDescription propertyDescription3 = map.get("field2");
        assertThat(propertyDescription3, is(notNullValue()));
        assertThat(propertyDescription3.getName(), is("field2"));
        assertThat(propertyDescription3.getType(), is(String.class));
        assertThat(propertyDescription3.getValue(), is(nullValue()));
    }

    @Test
    public void testGetPropertyValueMapSimpleValues()
    {
        ClassA classA =  new ClassA();
        classA.a1 = "a1";
        classA.a2 = 1;
        classA.a3 = LocalDateTime.now();
        ClassB classB = new ClassB();
        classB.b1 = 2.0;
        classB.b2 = null;
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

        PropertyDescription expectedResult1 =
            new PropertyDescription(new PropertyDefinition("a1", String.class, ClassA.class), "a1", true);
        PropertyDescription expectedResult2 =
            new PropertyDescription(new PropertyDefinition("a2", Integer.class, ClassA.class), 1, true);
        PropertyDescription expectedResult3 =
            new PropertyDescription(new PropertyDefinition("a3", LocalDateTime.class, ClassA.class), classA.a3, true);
        PropertyDescription expectedResult4 =
            new PropertyDescription(new PropertyDefinition("a4", ClassB.class, ClassA.class), classA.a4, false);
        PropertyDescription expectedResult5 =
            new PropertyDescription(new PropertyDefinition("a4.b1", Double.class, ClassB.class), 2.0, true);

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
        assertThat(propertyDescriptionC1.getPropertyDefinition().getName(), is("c1"));
        assertThat(propertyDescriptionC1.getValue(), is(nullValue()));
        assertThat(propertyDescriptionC1.isSimpleValue(), is(false));

        PropertyDescription propertyDescriptionC1D1 = map.get("c1.d1");
        assertThat(propertyDescriptionC1D1.getType(), is(ClassE.class));
        assertThat(propertyDescriptionC1D1.getPropertyDefinition().getName(), is("c1.d1"));
        assertThat(propertyDescriptionC1D1.getValue(), is(nullValue()));
        assertThat(propertyDescriptionC1D1.isSimpleValue(), is(false));

        PropertyDescription propertyDescriptionC1D1E1 = map.get("c1.d1.e1");
        assertThat(propertyDescriptionC1D1E1.getType(), is(String.class));
        assertThat(propertyDescriptionC1D1E1.getPropertyDefinition().getName(), is("c1.d1.e1"));
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
        assertThat(propertyDescriptionWorkUnit.getPropertyDefinition().getName(), is("workUnit"));
        assertThat(propertyDescriptionWorkUnit.getValue(), is(workUnit));
        assertThat(propertyDescriptionWorkUnit.isSimpleValue(), is(false));
    }

    @Test
    public void testListOfSimpleClasses()
    {
        //FAILING
        ClassWithAList classWithAList = new ClassWithAList();
        SimpleClass simpleClass1 = new SimpleClass();
        simpleClass1.setId(1L);
        simpleClass1.setField1("simpleClass1Field1");
        simpleClass1.setField2("simpleClass1Field2");
        SimpleClass simpleClass2 = new SimpleClass();
        simpleClass2.setId(2L);
        simpleClass2.setField1("simpleClass2Field1");
        simpleClass2.setField2("simpleClass2Field2");
        List<SimpleClass> list = Arrays.asList(simpleClass1, simpleClass2);
        classWithAList.setListOfSimpleClasses(list);
        objectInspector = new ObjectInspector(classWithAList, Collections.singletonList("id"));

        Map<String, PropertyDescription> map = objectInspector.getMap();

        assertThat(map.size(), is(7));

        PropertyDescription propertyDescription1 = map.get("listOfSimpleClasses.[1].field2");
        assertThat(propertyDescription1.getType(), is(String.class));
        assertThat(
            propertyDescription1.getPropertyDefinition().getName(),
            is("listOfSimpleClasses.[1].field2"));
        assertThat(propertyDescription1.getValue(), is("simpleClass1Field2"));
        assertThat(propertyDescription1.isSimpleValue(), is(true));

        PropertyDescription propertyDescription2 = map.get("listOfSimpleClasses.[1]");
        assertThat(propertyDescription2.getType(), is(SimpleClass.class));
        assertThat(
            propertyDescription2.getPropertyDefinition().getName(),
            is("listOfSimpleClasses.[1]"));
        assertThat(propertyDescription2.getValue(), is(simpleClass1));
        assertThat(propertyDescription2.isSimpleValue(), is(false));

        PropertyDescription propertyDescription3 = map.get("listOfSimpleClasses.[1].field1");
        assertThat(propertyDescription3.getType(), is(String.class));
        assertThat(
            propertyDescription3.getPropertyDefinition().getName(),
            is("listOfSimpleClasses.[1].field1"));
        assertThat(propertyDescription3.getValue(), is("simpleClass1Field1"));
        assertThat(propertyDescription3.isSimpleValue(), is(true));

        PropertyDescription propertyDescription4 = map.get("listOfSimpleClasses");
        assertThat(propertyDescription4.getType(), is(List.class));
        assertThat(
            propertyDescription4.getPropertyDefinition().getName(), is("listOfSimpleClasses"));
        assertThat(propertyDescription4.getValue(), is(list));
        assertThat(propertyDescription4.isSimpleValue(), is(false));

        PropertyDescription propertyDescription5 = map.get("listOfSimpleClasses.[2].field1");
        assertThat(propertyDescription5.getType(), is(String.class));
        assertThat(
            propertyDescription5.getPropertyDefinition().getName(), is("listOfSimpleClasses.[2].field1"));
        assertThat(propertyDescription5.getValue(), is("simpleClass2Field1"));
        assertThat(propertyDescription5.isSimpleValue(), is(true));

        PropertyDescription propertyDescription6 = map.get("listOfSimpleClasses.[2]");
        assertThat(propertyDescription6.getType(), is(SimpleClass.class));
        assertThat(
            propertyDescription6.getPropertyDefinition().getName(),
            is("listOfSimpleClasses.[2]"));
        assertThat(propertyDescription6.getValue(), is(simpleClass2));
        assertThat(propertyDescription6.isSimpleValue(), is(false));

        PropertyDescription propertyDescription7 = map.get("listOfSimpleClasses.[2].field2");
        assertThat(propertyDescription7.getType(), is(String.class));
        assertThat(
            propertyDescription7.getPropertyDefinition().getName(),
            is("listOfSimpleClasses.[2].field2"));
        assertThat(propertyDescription7.getValue(), is("simpleClass2Field2"));
        assertThat(propertyDescription7.isSimpleValue(), is(true));
    }

    @Test
    public void testManyToManyRelation()
    {
        RootEntity classWithCollection = new RootEntity();
        classWithCollection.setFieldInParent("fieldInParentValue");
        ManyToManyClass manyToManyClass = new ManyToManyClass();
        manyToManyClass.setRootEntity(classWithCollection);
        manyToManyClass.setId(1L);
        SimpleClass simpleClass = new SimpleClass();
        simpleClass.setId(100L);
        simpleClass.setField1("field1");
        simpleClass.setField2("field2");
        manyToManyClass.setSimpleClass(simpleClass);
        List<ManyToManyClass> list = Collections.singletonList(manyToManyClass);
        classWithCollection.setList(list);
        objectInspector = new ObjectInspector(classWithCollection, Collections.singletonList("id"));

        Map<String, PropertyDescription> map = objectInspector.getMap();

        assertThat(map.size(), is(7));

        PropertyDescription propertyDescription1 = map.get("list.[1].simpleClass.field2");
        assertThat(propertyDescription1.getType(), is(String.class));
        assertThat(
            propertyDescription1.getPropertyDefinition().getName(),
            is("list.[1].simpleClass.field2"));
        assertThat(propertyDescription1.getValue(), is("field2"));
        assertThat(propertyDescription1.isSimpleValue(), is(true));

        PropertyDescription propertyDescription2 = map.get("list.[1].simpleClass.field1");
        assertThat(propertyDescription2.getType(), is(String.class));
        assertThat(
            propertyDescription2.getPropertyDefinition().getName(),
            is("list.[1].simpleClass.field1"));
        assertThat(propertyDescription2.getValue(), is("field1"));
        assertThat(propertyDescription2.isSimpleValue(), is(true));

        PropertyDescription propertyDescription3 = map.get("list.[1]");
        assertThat(propertyDescription3.getType(), is(ManyToManyClass.class));
        assertThat(propertyDescription3.getPropertyDefinition().getName(), is("list.[1]"));
        assertThat(propertyDescription3.getValue(), is(manyToManyClass));
        assertThat(propertyDescription3.isSimpleValue(), is(false));

        PropertyDescription propertyDescription4 = map.get("list.[1].simpleClass");
        assertThat(propertyDescription4.getType(), is(SimpleClass.class));
        assertThat(propertyDescription4.getPropertyDefinition().getName(), is("list.[1].simpleClass"));
        assertThat(propertyDescription4.getValue(), is(simpleClass));
        assertThat(propertyDescription4.isSimpleValue(), is(false));

        PropertyDescription propertyDescription5 = map.get("list");
        assertThat(propertyDescription5.getType(), is(List.class));
        assertThat(propertyDescription5.getPropertyDefinition().getName(), is("list"));
        assertThat(propertyDescription5.getValue(), is(list));
        assertThat(propertyDescription5.isSimpleValue(), is(false));

        PropertyDescription propertyDescription6 = map.get("list.[1].rootEntity");
        assertThat(propertyDescription6.getType(), is(RootEntity.class));
        assertThat(propertyDescription6.getPropertyDefinition().getName(), is("list.[1].rootEntity"));
        assertThat(propertyDescription6.getValue(), is(classWithCollection));
        assertThat(propertyDescription6.isSimpleValue(), is(false));

        PropertyDescription propertyDescription7 = map.get("fieldInParent");
        assertThat(propertyDescription7.getType(), is(String.class));
        assertThat(propertyDescription7.getPropertyDefinition().getName(), is("fieldInParent"));
        assertThat(propertyDescription7.getValue(), is("fieldInParentValue"));
        assertThat(propertyDescription7.isSimpleValue(), is(true));
    }

    @Test
    public void testCollectionStaticList()
    {
        List<String> myList = Arrays.asList("A", "B", "C");
        objectInspector = new ObjectInspector(myList, Collections.singletonList(""));

        Map<String, PropertyDescription> map = objectInspector.getMap();
        assertThat(map.size(), is(3));

        PropertyDescription propertyDescription1 = map.get("[A]");
        assertThat(propertyDescription1.getType(), is(String.class));
        assertThat(propertyDescription1.getPropertyDefinition().getName(), is("[A]"));
        assertThat(propertyDescription1.getValue(), is("A"));
        assertThat(propertyDescription1.isSimpleValue(), is(true));

        PropertyDescription propertyDescription2 = map.get("[B]");
        assertThat(propertyDescription2.getType(), is(String.class));
        assertThat(propertyDescription2.getName(), is("[B]"));
        assertThat(propertyDescription2.getValue(), is("B"));
        assertThat(propertyDescription2.isSimpleValue(), is(true));

        PropertyDescription propertyDescription3 = map.get("[C]");
        assertThat(propertyDescription3.getType(), is(String.class));
        assertThat(propertyDescription3.getName(), is("[C]"));
        assertThat(propertyDescription3.getValue(), is("C"));
        assertThat(propertyDescription3.isSimpleValue(), is(true));

    }

    @Test
    public void testCycle()
    {
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        crisprAttempt.setId(20L);
        Guide guide = new Guide();
        guide.setId(1L);
        crisprAttempt.setGuides(new HashSet<>(Arrays.asList(guide)));

        Plan plan = new Plan();
        plan.setCrisprAttempt(crisprAttempt);

        objectInspector = new ObjectInspector(plan, Collections.singletonList(""));
        var map = objectInspector.getMap();

        assertThat(map.size(), is(153));
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

    @Data
    public class RootEntity
    {
        String fieldInParent;
        List<ManyToManyClass> list;
    }

    @Data
    public class ManyToManyClass
    {
        private Long id;
        @ToString.Exclude
        private RootEntity rootEntity;
        private SimpleClass simpleClass;
    }

    @Data
    public class ClassWithAList
    {
        private List<SimpleClass> listOfSimpleClasses;
    }

    @Data
    public class SimpleClass
    {
        private Long id;
        private String field1;
        private String field2;
    }

    @Data
    public class RootCycle
    {
        private CrisprAttempt crisprAttempt;
    }

    @Data
    public class CycleA
    {
        private String name;
        private List<CycleB> listOfCycleB;
    }

    @Data
    public class CycleB
    {
        private Long id;
        @ToString.Exclude
        private CycleA cycleA;
    }
}