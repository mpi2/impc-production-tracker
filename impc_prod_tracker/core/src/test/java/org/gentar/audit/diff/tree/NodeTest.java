package org.gentar.audit.diff.tree;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;

public class NodeTest
{
    private Node<String> root;

    public NodeTest()
    {
        root = new Node<>("root");

        Node<String> node1 = root.addChild(new Node<String>("node 1"));

        Node<String> node11 = node1.addChild(new Node<String>("node 11"));
        Node<String> node111 = node11.addChild(new Node<String>("node 111"));
        Node<String> node112 = node11.addChild(new Node<String>("node 112"));

        Node<String> node12 = node1.addChild(new Node<String>("node 12"));

        Node<String> node2 = root.addChild(new Node<String>("node 2"));

        Node<String> node21 = node2.addChild(new Node<String>("node 21"));
        Node<String> node211 = node2.addChild(new Node<String>("node 22"));
    }

    @Test
    public void testFind()
    {
        Node<String> myNode = root.find("node 21");
        assertThat("Node is null", myNode, is(notNullValue()));
        assertThat("Value not found", myNode.getData(), is("node 21"));
    }

    @Test
    public void testFindWhenNotExists()
    {
        Node<String> myNode = root.find("x");
        assertThat("Node is not null", myNode, is(nullValue()));
    }

    @Test
    public void testAnyParentContains()
    {
        Node<String> node = root.find("node 12");
        boolean result = node.anyParentContains("node 1");
        assertThat("Should find a parent", result, is(true));
    }

    @Test
    public void testAnyParentContainsWhenNotContains()
    {
        Node<String> node = root.find("node 12");
        boolean result = node.anyParentContains("node 11");
        assertThat("Parent should not be found", result, is(false));
    }
}