package com.example.johnson.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link Node}.
 *
 * <p><strong>Overview:</strong></p>
 * This test suite verifies the correctness of the {@code Node} class,
 * which is used in Dijkstra’s algorithm as elements in a priority queue.
 * It validates behavior related to comparison, equality, and hash codes.
 *
 * <p><strong>Functionality Tested:</strong></p>
 * <ul>
 *   <li>Object construction and field accessors.</li>
 *   <li>{@link Node#compareTo(Node)} for sorting by distance.</li>
 *   <li>{@link Node#equals(Object)} for logical equality (based on vertex).</li>
 *   <li>{@link Node#hashCode()} for hash-based collections.</li>
 * </ul>
 *
 * <p><strong>Pass/Fail Criteria:</strong></p>
 * <ul>
 *   <li><strong>Pass:</strong> Node behaves as expected in comparisons and collections.</li>
 *   <li><strong>Fail:</strong> Node does not compare or match according to specification.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2025-03-26
 */
public class NodeTest {

    /**
     * Test 1: Node fields are initialized and accessible.
     *
     * <p><strong>Scenario:</strong> Create a node with vertex "A" and distance 5.</p>
     * <p><strong>Expected:</strong> Getter methods return correct values.</p>
     */
    @Test
    @DisplayName("Test 1: Constructor and getter methods work correctly")
    void testNodeInitialization() {
        Node node = new Node("A", 5);

        assertEquals("A", node.getVertex());
        assertEquals(5, node.getDistance());
    }

    /**
     * Test 2: Nodes are compared correctly by distance.
     *
     * <p><strong>Scenario:</strong> Compare nodes with distances 3 and 7.</p>
     * <p><strong>Expected:</strong> Node with smaller distance is "less than" the other.</p>
     */
    @Test
    @DisplayName("Test 2: compareTo compares by distance")
    void testNodeComparison() {
        Node n1 = new Node("A", 3);
        Node n2 = new Node("B", 7);

        assertTrue(n1.compareTo(n2) < 0);
        assertTrue(n2.compareTo(n1) > 0);
        assertEquals(0, n1.compareTo(new Node("X", 3)));
    }

    /**
     * Test 3: Node equality is based on vertex name only.
     *
     * <p><strong>Scenario:</strong> Two nodes with same vertex but different distances.</p>
     * <p><strong>Expected:</strong> They are equal by {@code equals()}.</p>
     */
    @Test
    @DisplayName("Test 3: equals() compares only vertex")
    void testEquals() {
        Node n1 = new Node("A", 10);
        Node n2 = new Node("A", 999); // Same vertex, different distance

        assertEquals(n1, n2);
    }

    /**
     * Test 4: hashCode is consistent with equals.
     *
     * <p><strong>Scenario:</strong> Two nodes that are equal by vertex should produce the same hash code.</p>
     */
    @Test
    @DisplayName("Test 4: hashCode() matches equals()")
    void testHashCode() {
        Node n1 = new Node("X", 1);
        Node n2 = new Node("X", 1000);

        assertEquals(n1.hashCode(), n2.hashCode());
        assertEquals(n1, n2);
    }

    /**
     * Test 5: Node works correctly inside a HashSet.
     *
     * <p><strong>Scenario:</strong> Add nodes with the same vertex to a {@code HashSet}.</p>
     * <p><strong>Expected:</strong> Set should treat them as duplicates.</p>
     */
    @Test
    @DisplayName("Test 5: Node uniqueness in HashSet based on vertex")
    void testNodeInSet() {
        Set<Node> set = new HashSet<>();
        set.add(new Node("A", 3));
        set.add(new Node("A", 10)); // Should be considered same

        assertEquals(1, set.size(), "Set should treat nodes with same vertex as duplicate");
    }

    /**
     * Test 6: Node ordering inside a PriorityQueue.
     *
     * <p><strong>Scenario:</strong> Add multiple nodes with varying distances.</p>
     * <p><strong>Expected:</strong> PriorityQueue returns nodes in ascending distance order.</p>
     */
    @Test
    @DisplayName("Test 6: PriorityQueue ordering by distance")
    void testPriorityQueueOrdering() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node("A", 10));
        pq.add(new Node("B", 3));
        pq.add(new Node("C", 7));

        assertEquals("B", pq.poll().getVertex());
        assertEquals("C", pq.poll().getVertex());
        assertEquals("A", pq.poll().getVertex());
    }
}
