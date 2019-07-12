package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.springframework.util.Assert;
import uk.ac.ebi.impc_prod_tracker.common.tree.Node;

public class CheckedClassesTree
{
    private Node<Class<?>> tree;

    public CheckedClassesTree()
    {
        tree = new Node<>();
    }

    final Node<Class<?>> getTree()
    {
        return tree;
    }

    public void setRootClass(Class<?> rootClass)
    {
        tree.setData(rootClass);
    }


    private Node<Class<?>> getNode(Class<?> clazz)
    {
        return tree.find(clazz);
    }

    public boolean addRelation(Class<?> childClass, Class<?> parentClass)
    {
        Assert.notNull(childClass, "childClass was null");
        Assert.notNull(parentClass, "parentClass was null");

        if (tree.getData() == null)
        {
            throw new IllegalArgumentException("Tree parent is null");
        }

        boolean relationWasAdded = false;

        boolean relationAlreadyExists = relationExists(childClass, parentClass);

        if (!relationAlreadyExists)
        {
            Node<Class<?>> parentNode = getNode(parentClass);
            Node<Class<?>> newNode = new Node<>(childClass);
            parentNode.addChild(newNode);
            relationWasAdded = true;
        }

        return relationWasAdded;
    }

    public void print()
    {
        tree.print();
    }

    private boolean relationExists( Class<?> childClass, Class<?> parentClass)
    {
        boolean relationExists = false;
        Node<Class<?>> parentNode = getNode(parentClass);
        if (parentNode != null)
        {
            relationExists = parentNode.anyParentContains(childClass);
        }

        return relationExists;
    }
}
