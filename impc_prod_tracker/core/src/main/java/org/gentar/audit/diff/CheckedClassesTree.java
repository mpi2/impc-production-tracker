package org.gentar.audit.diff;

import org.gentar.audit.diff.tree.Node;
import org.springframework.util.Assert;

/**
 * Keeps a tree with the classes that have been examined in an object inspection process.
 * The parent is a class and the children are classes used by that class.
 */
class CheckedClassesTree
{
    private final Node<Class<?>> tree;

    CheckedClassesTree()
    {
        tree = new Node<>();
    }

    final Node<Class<?>> getTree()
    {
        return tree;
    }

    /**
     * Sets the root of the tree
     * @param rootClass Root
     */
    void setRootClass(Class<?> rootClass)
    {
        tree.setData(rootClass);
    }

    private Node<Class<?>> getNode(Class<?> clazz)
    {
        return tree.find(clazz);
    }

    /**
     * Checks it the relation children-parent can be registered in the tree.
     * @param childClass Class being used (children class).
     * @param parentClass Class that used the childClass (parent class).
     * @return True if the relation is valid to be added. False if not.
     */
    boolean canRelationBeAdded(Class<?> childClass, Class<?> parentClass)
    {
        boolean relationAlreadyExists = true;
        if (!childClass.equals(parentClass))
        {
            Node<Class<?>> parentNode = getNode(parentClass);
            if (parentNode != null)
            {
                // We don't want to restrict collections in the tree because they can exist
                // in any place of the objects graph.
                if (PropertyChecker.isCollection(childClass) || PropertyChecker.isCollection(parentClass))
                {
                    relationAlreadyExists = false;
                }
                else
                {
                    relationAlreadyExists = parentNode.anyParentContains(childClass);
                }
            }
        }
        return !relationAlreadyExists;
    }

    /**
     * Adds the relation in the tree if it does not exists
     * @param childClass Child class.
     * @param parentClass Parent class.
     */
    void addRelationIfNotExist(Class<?> childClass, Class<?> parentClass)
    {
        Assert.notNull(childClass, "childClass was null");
        Assert.notNull(parentClass, "parentClass was null");

        if (tree.getData() == null)
        {
            throw new IllegalArgumentException("Tree parent is null");
        }

        if (canRelationBeAdded(childClass, parentClass))
        {
            {
                Node<Class<?>> parentNode = getNode(parentClass);
                Node<Class<?>> newNode = new Node<>(childClass);
                parentNode.addChild(newNode);
            }
        }
    }

    void print()
    {
        tree.print();
    }
}
