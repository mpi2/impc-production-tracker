package org.gentar.audit.diff;

import lombok.Data;
import lombok.ToString;
import org.gentar.audit.diff.tree.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckedClassesTreeTest
{
    private CheckedClassesTree checkedClassesTree;

    @BeforeEach
    public void setup()
    {
        checkedClassesTree = new CheckedClassesTree();
    }

    @Test
    public void testSetRootClass()
    {
        checkedClassesTree.setRootClass(ClassA.class);

        assertThat("Tree is empty", checkedClassesTree.getTree(), is(notNullValue()));
        assertThat(
            "Class not registered",
            checkedClassesTree.getTree().find(ClassA.class),
            is(notNullValue()));
    }

    @Test
    public void testAddRelation()
    {
        checkedClassesTree.setRootClass(ClassA.class);
        checkedClassesTree.addRelationIfNotExist(ClassB.class, ClassA.class);
        checkedClassesTree.addRelationIfNotExist(ClassB.class, ClassA.class);

        Node<Class<?>> root = checkedClassesTree.getTree().find(ClassA.class);
        Node<Class<?>> child = checkedClassesTree.getTree().find(ClassB.class);

        assertThat("", root, is(notNullValue()));
        assertThat("", child, is(notNullValue()));
        assertThat("", root.getChildren().size(), is(1));
        assertThat("", root.getChildren().iterator().next().getData(), is(ClassB.class));
        assertThat("", child.getParent().getData(), is(ClassA.class));
    }

    @Test
    public void testAddRelationWithGrandChildren()
    {
        checkedClassesTree.setRootClass(ClassA.class);
        checkedClassesTree.addRelationIfNotExist(ClassB.class, ClassA.class);
        checkedClassesTree.addRelationIfNotExist(ClassC.class, ClassB.class);

        Node<Class<?>> root = checkedClassesTree.getTree().find(ClassA.class);
        Node<Class<?>> child = checkedClassesTree.getTree().find(ClassB.class);
        Node<Class<?>> grandChildren = checkedClassesTree.getTree().find(ClassC.class);

        assertThat("", root, is(notNullValue()));
        assertThat("", child, is(notNullValue()));
        assertThat("", root.getChildren().size(), is(1));
        assertThat("", root.getChildren().iterator().next().getData(), is(ClassB.class));
        assertThat("", child.getParent().getData(), is(ClassA.class));

        assertThat("", child.getChildren().size(), is(1));
        assertThat("", child.getChildren().iterator().next().getData(), is(ClassC.class));
        assertThat("", grandChildren.getParent().getData(), is(ClassB.class));
    }

    @Test
    public void testAddRelationWhenCircularDependency()
    {
        checkedClassesTree.setRootClass(ClassA.class);
        checkedClassesTree.addRelationIfNotExist(ClassB.class, ClassA.class);
        checkedClassesTree.addRelationIfNotExist(ClassA.class, ClassB.class);

        Node<Class<?>> root = checkedClassesTree.getTree().find(ClassA.class);
        Node<Class<?>> child = checkedClassesTree.getTree().find(ClassB.class);

        assertThat("", root, is(notNullValue()));
        assertThat("", child, is(notNullValue()));
        assertThat("", root.getChildren().size(), is(1));
        assertThat("", root.getChildren().iterator().next().getData(), is(ClassB.class));
        assertThat("", child.getParent().getData(), is(ClassA.class));
        assertThat("", child.getChildren().size(), is(0));
    }

    @Test
    public void testAddRelationWhenLongCircularDependency()
    {
        checkedClassesTree.setRootClass(ClassA.class);
        checkedClassesTree.addRelationIfNotExist(ClassB.class, ClassA.class);
        checkedClassesTree.addRelationIfNotExist(ClassC.class, ClassB.class);
        checkedClassesTree.addRelationIfNotExist(ClassA.class, ClassC.class);

        Node<Class<?>> classANode = checkedClassesTree.getTree().find(ClassA.class);
        Node<Class<?>> classBNode = checkedClassesTree.getTree().find(ClassB.class);
        Node<Class<?>> classCNode = checkedClassesTree.getTree().find(ClassC.class);

        assertThat("", classANode, is(notNullValue()));
        assertThat("", classBNode, is(notNullValue()));
        assertThat("", classCNode, is(notNullValue()));
        assertThat("", classANode.getChildren().size(), is(1));
        assertThat("", classANode.getChildren().iterator().next().getData(), is(ClassB.class));
        assertThat("", classBNode.getParent().getData(), is(ClassA.class));
        assertThat("", classBNode.getChildren().size(), is(1));
        assertThat("", classCNode.getParent().getData(), is(ClassB.class));
        assertThat("", classCNode.getChildren().size(), is(0));
    }

    @Test
    public void testAddRelationWithoutRootClass()
    {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> checkedClassesTree.addRelationIfNotExist(ClassA.class, ClassB.class),
            "Exception not thrown");
        assertTrue(thrown.getMessage().contains("Tree parent is null"));
    }

    @Test
    public void testAddRelationWithEmptyChildClass()
    {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> checkedClassesTree.addRelationIfNotExist(null, ClassA.class),
            "Exception not thrown");
        assertTrue(thrown.getMessage().contains("childClass was null"));
    }

    @Test
    public void testAddRelationWithEmptyParentClass()
    {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> checkedClassesTree.addRelationIfNotExist(ClassA.class, null),
            "Exception not thrown");
        assertTrue(thrown.getMessage().contains("parentClass was null"));
    }

    class ClassA
    {
    }

    class ClassB
    {
    }

    class ClassC
    {
    }

    class ClassD
    {
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
    public class ListOfSimpleClass
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
}