package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.ac.ebi.impc_prod_tracker.common.tree.Node;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class CheckedClassesTreeTest
{
    private CheckedClassesTree checkedClassesTree;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
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
        checkedClassesTree.addRelation(ClassB.class, ClassA.class);

        Node<Class<?>> root = checkedClassesTree.getTree().find(ClassA.class);
        Node<Class<?>> child = checkedClassesTree.getTree().find(ClassB.class);

        assertThat("", root, is(notNullValue()));
        assertThat("", child, is(notNullValue()));
        assertThat("", root.getChildren().size(), is(1));
        assertThat("", root.getChildren().get(0).getData(), is(ClassB.class));
        assertThat("", child.getParent().getData(), is(ClassA.class));
    }

    @Test
    public void testAddRelationWithGrandChildren()
    {
        checkedClassesTree.setRootClass(ClassA.class);
        checkedClassesTree.addRelation(ClassB.class, ClassA.class);
        checkedClassesTree.addRelation(ClassC.class, ClassB.class);
        checkedClassesTree.print();

        Node<Class<?>> root = checkedClassesTree.getTree().find(ClassA.class);
        Node<Class<?>> child = checkedClassesTree.getTree().find(ClassB.class);
        Node<Class<?>> grandChildren = checkedClassesTree.getTree().find(ClassC.class);

        assertThat("", root, is(notNullValue()));
        assertThat("", child, is(notNullValue()));
        assertThat("", root.getChildren().size(), is(1));
        assertThat("", root.getChildren().get(0).getData(), is(ClassB.class));
        assertThat("", child.getParent().getData(), is(ClassA.class));

        assertThat("", child.getChildren().size(), is(1));
        assertThat("", child.getChildren().get(0).getData(), is(ClassC.class));
        assertThat("", grandChildren.getParent().getData(), is(ClassB.class));
    }

    @Test
    public void testAddRelationWhenCircularDependency()
    {
        checkedClassesTree.setRootClass(ClassA.class);
        checkedClassesTree.addRelation(ClassB.class, ClassA.class);
        checkedClassesTree.addRelation(ClassA.class, ClassB.class);

        Node<Class<?>> root = checkedClassesTree.getTree().find(ClassA.class);
        Node<Class<?>> child = checkedClassesTree.getTree().find(ClassB.class);

        assertThat("", root, is(notNullValue()));
        assertThat("", child, is(notNullValue()));
        assertThat("", root.getChildren().size(), is(1));
        assertThat("", root.getChildren().get(0).getData(), is(ClassB.class));
        assertThat("", child.getParent().getData(), is(ClassA.class));
        assertThat("", child.getChildren().size(), is(0));
    }

    @Test
    public void testAddRelationWithoutRootClass()
    {
       expectedEx.expect(IllegalArgumentException.class);
       expectedEx.expectMessage("Tree parent is null");

        checkedClassesTree.addRelation(ClassA.class, ClassB.class);
    }

    @Test
    public void testAddRelationWithEmptyChildClass()
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("childClass was null");

        checkedClassesTree.addRelation(null, ClassA.class);
    }

    @Test
    public void testAddRelationWithEmptyParentClass()
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("parentClass was null");

        checkedClassesTree.addRelation(ClassA.class, null);
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
}