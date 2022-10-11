package test;

import layouts.BHLayout;
import org.junit.jupiter.api.Test;
import types.Node;
import types.Tuple;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests the Node and Tuple types in the types package
 *
 */
public class TypeTest extends SuperTest {

    /**
     * Tests the equals override behaviour in the Tuple class
     */
    @Test
    public void tupleEqualTest() {
        Tuple t = new Tuple(0,1);
        assertTrue(t.equals(new Tuple(0, 1)));
        Tuple t2 = new Tuple (1, 3);
        assertFalse(t.equals(t2));

    }


    /**
     * Performs a test on the equals override behaviour in Node with two empty nodes
     */
    @Test
    public void equalsTest() {
        Node node = new Node(layout, new ArrayList<>());
        Node newNode = new Node(layout, new ArrayList<>());
        assertTrue(node.equals(newNode));
    }

    /**
     * Performs a test on the equals override behaviour in Node with one empty and one randomised layout
     */
    @Test
    public void equalsTest2() {
        layout.randomise(rand.nextInt());
        Node node = new Node(new BHLayout(layout), new ArrayList<>());
        layout = new BHLayout();
        Node newNode = new Node(layout, new ArrayList<>());
        assertFalse(node.equals(newNode));
    }

    /**
     * Performs another equals test for the Node class
     * Starts with the nodes being unequal then updates the log to make them equal
     */
    @Test
    public void equalsTest3() {
        ArrayList<Tuple> tuples = new ArrayList<>();
        tuples.add(new Tuple (1, 2));
        Node node = new Node(layout, tuples);
        Node newNode = new Node(layout, new ArrayList<>());
        assertFalse(node.equals(newNode));
        newNode = new Node(layout, tuples);
        assertTrue(node.equals(newNode));

    }

    /**
     * Performs the equals operation with two different types which should be false
     */
    @Test
    public void equalsTest4() {
        Tuple t = new Tuple(0, 1);
        Node n = new Node(new BHLayout(), new ArrayList<>());
        assertFalse(t.equals(n));
        assertFalse(n.equals(t));
    }

    /**
     * Tests that the Node class correctly copies the ArrayList of tuples
     */
    @Test
    public void copyLogTest() {
        ArrayList<Tuple> tuples = new ArrayList<>();
        tuples.add(new Tuple(0, 1));
        Node node = new Node(layout, tuples);
        assertEquals(tuples, node.copyLog());
        tuples.add(new Tuple(1, 2));
        assertNotEquals(node.copyLog(), tuples);
        Node newNode = new Node(layout, tuples);
        assertEquals(tuples, newNode.copyLog());
    }

}
