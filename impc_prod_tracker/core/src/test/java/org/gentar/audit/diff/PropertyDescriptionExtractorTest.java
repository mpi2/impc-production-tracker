package org.gentar.audit.diff;

import lombok.Data;
import lombok.ToString;
import org.gentar.util.CollectionPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class PropertyDescriptionExtractorTest
{
    private PropertyDescriptionExtractor testInstance;

    @BeforeEach
    void setUp()
    {
        testInstance = new PropertyDescriptionExtractor();
    }

    @Test
    void buildByCollectionPrimitive()
    {
        List<String> list = Arrays.asList("A", "B", "C");

        List<PropertyDescription> propertyDescriptions = testInstance.buildByCollection(list, null);

        assertThat("Expected 3 descriptions",propertyDescriptions.size(), is(3));
        PropertyDescription description1 = findByName("[A]", propertyDescriptions);
        assertThat("Incorrect type", description1.getType(), is(String.class));
        assertThat("Incorrect value", description1.getValue(), is("A"));
        PropertyDescription description2 = findByName("[B]", propertyDescriptions);
        assertThat("Incorrect type", description2.getType(), is(String.class));
        assertThat("Incorrect value", description2.getValue(), is("B"));
        PropertyDescription description3 = findByName("[C]", propertyDescriptions);
        assertThat("Incorrect type", description3.getType(), is(String.class));
        assertThat("Incorrect value", description3.getValue(), is("C"));
    }

    @Test
    void buildByCollectionPrimitiveWithParent()
    {
        List<String> list = Arrays.asList("A", "B", "C");

        PropertyDefinition parent = new PropertyDefinition();
        parent.setName("parentName");
        List<PropertyDescription> propertyDescriptions = testInstance.buildByCollection(list, parent);

        assertThat("Expected 3 descriptions",propertyDescriptions.size(), is(3));
        PropertyDescription description1 = findByName("parentName.[A]", propertyDescriptions);
        assertThat("Incorrect type", description1.getType(), is(String.class));
        assertThat("Incorrect value", description1.getValue(), is("A"));
        PropertyDescription description2 = findByName("parentName.[B]", propertyDescriptions);
        assertThat("Incorrect type", description2.getType(), is(String.class));
        assertThat("Incorrect value", description2.getValue(), is("B"));
        PropertyDescription description3 = findByName("parentName.[C]", propertyDescriptions);
        assertThat("Incorrect type", description3.getType(), is(String.class));
        assertThat("Incorrect value", description3.getValue(), is("C"));
    }

    @Test
    void buildByCollectionSimpleObject()
    {
        SimpleClass simpleClass1 = new SimpleClass();
        simpleClass1.setId(1L);
        simpleClass1.setField1("simpleClass1Field1");
        simpleClass1.setField2("simpleClass1Field2");
        SimpleClass simpleClass2 = new SimpleClass();
        simpleClass2.setField1("simpleClass2Field1");
        simpleClass2.setField2("simpleClass2Field2");
        simpleClass2.setId(2L);
        List<SimpleClass> list = Arrays.asList(simpleClass1, simpleClass2);

        List<PropertyDescription> propertyDescriptions = testInstance.buildByCollection(list, null);

        assertThat("Expected 2 descriptions",propertyDescriptions.size(), is(2));
        PropertyDescription description1 = findByName("[1]", propertyDescriptions);
        assertThat(
            "Incorrect type", description1.getType(), is(SimpleClass.class));
        assertThat("Incorrect value", description1.getValue(), is(simpleClass1));
        PropertyDescription description2 = findByName("[2]", propertyDescriptions);
        assertThat(
            "Incorrect type", description2.getType(), is(SimpleClass.class));
        assertThat("Incorrect value", description2.getValue(), is(simpleClass2));
    }

    @Test
    void buildByTypeSimpleClass()
    {
        SimpleClass simpleClass = new SimpleClass();
        simpleClass.setField1("field1");
        simpleClass.setField2("field2");
        List<PropertyDescription> propertyDescriptions =
            testInstance.buildByType(simpleClass.getClass(), simpleClass, null);

        assertThat("Expected 3 descriptions",propertyDescriptions.size(), is(3));
        PropertyDescription description1 = findByName("id", propertyDescriptions);
        assertThat("Incorrect type", description1.getType(), is(Long.class));
        assertThat("Incorrect value", description1.getValue(), is(simpleClass.id));
        PropertyDescription description2 = findByName("field1", propertyDescriptions);
        assertThat(
            "Incorrect type",
            description2.getType(),
            is(simpleClass.field1.getClass()));
        assertThat("Incorrect value", description2.getValue(), is(simpleClass.field1));
        PropertyDescription description3 = findByName("field2", propertyDescriptions);
        assertThat(
            "Incorrect type",
            description3.getType(),
            is(simpleClass.field2.getClass()));
        assertThat("Incorrect value", description3.getValue(), is(simpleClass.field2));
    }

    private PropertyDescription findByName(String name, List<PropertyDescription> propertyDescriptions)
    {
        return propertyDescriptions.stream()
            .filter(x -> x.getPropertyDefinition().getName().equals(name))
            .findAny().orElse(null);
    }

    @Test
    void buildByTypeComplexClass()
    {
        SimpleClass simpleClass = new SimpleClass();
        simpleClass.setField1("field1");
        simpleClass.setField2("field2");
        ManyToManyClass manyToManyClass = new ManyToManyClass();
        manyToManyClass.setSimpleClass(simpleClass);

        List<PropertyDescription> propertyDescriptions =
            testInstance.buildByType(manyToManyClass.getClass(), manyToManyClass, null);

        assertThat("Expected 2 descriptions",propertyDescriptions.size(), is(2));
        PropertyDescription description1 = findByName("rootEntity", propertyDescriptions);
        assertThat(
            "Incorrect type", description1.getType(), is(RootEntity.class));
        assertThat("Incorrect value", description1.getValue(), is(manyToManyClass.getRootEntity()));
        PropertyDescription description2 = findByName("simpleClass", propertyDescriptions);
        assertThat(
            "Incorrect type", description2.getType(), is(SimpleClass.class));
        assertThat("Incorrect value", description2.getValue(), is(manyToManyClass.getSimpleClass()));
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
        @ToString.Exclude
        private RootEntity rootEntity;
        private SimpleClass simpleClass;
    }

    @Data
    public class SimpleClass
    {
        private Long id;
        private String field1;
        private String field2;
    }

    private void printPretty(List<PropertyDescription> propertyDescriptions)
    {
        System.out.println("--- Init PropertyDescription list ---");
        System.out.println("[");
        if (propertyDescriptions != null)
        {
            propertyDescriptions.forEach(x -> System.out.println("\t" + x));
        }
        System.out.println("]");
        System.out.println("--- End PropertyDescription list ---");
    }
}