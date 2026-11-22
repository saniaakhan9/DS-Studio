package com.dsstudio.core.structures.linear;

import com.dsstudio.core.trace.OperationTrace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinearStructuresTest {

    @Test
    @DisplayName("ArrayStructure: Insert, Delete, Search, Reverse and Boundary Tests")
    void testArrayStructure() {
        ArrayStructure arr = new ArrayStructure(5, new int[]{10, 20, 30});
        assertEquals(3, arr.getSize());
        assertEquals(5, arr.getCapacity());

        // Insert at index 1
        OperationTrace insertTrace = arr.insert(1, 15);
        assertTrue(insertTrace.isSuccess());
        assertEquals(List.of(10, 15, 20, 30), arr.toList());
        assertEquals(4, arr.getSize());

        // Insert at end (index 4)
        OperationTrace insertEnd = arr.insert(4, 40);
        assertTrue(insertEnd.isSuccess());
        assertTrue(arr.isFull());

        // Insert when full -> should fail gracefully
        OperationTrace overflowTrace = arr.insert(0, 99);
        assertFalse(overflowTrace.isSuccess());

        // Delete at index 1
        OperationTrace delTrace = arr.delete(1);
        assertTrue(delTrace.isSuccess());
        assertEquals(List.of(10, 20, 30, 40), arr.toList());

        // Linear Search
        OperationTrace searchFound = arr.linearSearch(30);
        assertTrue(searchFound.isSuccess());
        OperationTrace searchNotFound = arr.linearSearch(999);
        assertFalse(searchNotFound.isSuccess());

        // Binary Search
        OperationTrace binSearch = arr.binarySearch(20);
        assertTrue(binSearch.isSuccess());

        // Reverse
        OperationTrace revTrace = arr.reverse();
        assertTrue(revTrace.isSuccess());
        assertEquals(List.of(40, 30, 20, 10), arr.toList());
    }

    @Test
    @DisplayName("StackStructure: Push, Pop, Peek, Overflow, and Underflow")
    void testStackStructure() {
        StackStructure stack = new StackStructure(3);
        assertTrue(stack.isEmpty());

        // Underflow on empty stack
        OperationTrace underflow = stack.pop();
        assertFalse(underflow.isSuccess());

        // Push 3 items
        stack.push(100);
        stack.push(200);
        OperationTrace push3 = stack.push(300);
        assertTrue(push3.isSuccess());
        assertTrue(stack.isFull());
        assertEquals(3, stack.getSize());

        // Overflow on 4th push
        OperationTrace overflow = stack.push(400);
        assertFalse(overflow.isSuccess());

        // Peek
        OperationTrace peek = stack.peek();
        assertTrue(peek.isSuccess());
        assertEquals(3, stack.getSize());

        // Pop
        OperationTrace pop1 = stack.pop();
        assertTrue(pop1.isSuccess());
        assertEquals(2, stack.getSize());
        assertEquals(List.of(100, 200), stack.toList());
    }

    @Test
    @DisplayName("QueueStructure: Enqueue, Dequeue, FIFO Order, Boundary Conditions")
    void testQueueStructure() {
        QueueStructure queue = new QueueStructure(4);
        assertTrue(queue.isEmpty());

        // Underflow
        OperationTrace underflow = queue.dequeue();
        assertFalse(underflow.isSuccess());

        // Enqueue elements
        queue.enqueue(11);
        queue.enqueue(22);
        queue.enqueue(33);
        OperationTrace enq4 = queue.enqueue(44);
        assertTrue(enq4.isSuccess());
        assertEquals(4, queue.getSize());

        // Overflow
        OperationTrace overflow = queue.enqueue(55);
        assertFalse(overflow.isSuccess());

        // Dequeue verifies FIFO (first in, first out)
        OperationTrace deq1 = queue.dequeue();
        assertTrue(deq1.isSuccess());
        assertEquals(3, queue.getSize());
        assertEquals(List.of(22, 33, 44), queue.toList());
    }

    @Test
    @DisplayName("CircularQueueStructure: Circular Wrap-Around and Modulo Indexing")
    void testCircularQueueStructure() {
        CircularQueueStructure cq = new CircularQueueStructure(3);
        assertTrue(cq.isEmpty());

        cq.enqueue(10);
        cq.enqueue(20);
        cq.enqueue(30);
        assertTrue(cq.isFull());

        // Overflow
        OperationTrace over = cq.enqueue(40);
        assertFalse(over.isSuccess());

        // Dequeue one slot to make room at the front
        OperationTrace deq = cq.dequeue();
        assertTrue(deq.isSuccess());
        assertFalse(cq.isFull());

        // Now enqueue should wrap around to slot 0
        OperationTrace wrapEnq = cq.enqueue(99);
        assertTrue(wrapEnq.isSuccess());
        assertTrue(cq.isFull());
    }

    @Test
    @DisplayName("SinglyLinkedListStructure: Insert Head/Tail, Delete Head, Search")
    void testSinglyLinkedListStructure() {
        SinglyLinkedListStructure list = new SinglyLinkedListStructure();
        assertTrue(list.isEmpty());

        list.insertHead(20);
        list.insertHead(10);
        list.insertTail(30);
        assertEquals(List.of(10, 20, 30), list.toList());
        assertEquals(3, list.getSize());

        // Search
        OperationTrace searchFound = list.search(20);
        assertTrue(searchFound.isSuccess());
        OperationTrace searchNotFound = list.search(99);
        assertFalse(searchNotFound.isSuccess());

        // Delete Head
        OperationTrace delHead = list.deleteHead();
        assertTrue(delHead.isSuccess());
        assertEquals(List.of(20, 30), list.toList());
        assertEquals(2, list.getSize());
    }

    @Test
    @DisplayName("DoublyLinkedListStructure: Head and Tail Insertions with Bidirectional Integrity")
    void testDoublyLinkedListStructure() {
        DoublyLinkedListStructure dll = new DoublyLinkedListStructure();
        dll.insertHead(2);
        dll.insertHead(1);
        dll.insertTail(3);
        assertEquals(List.of(1, 2, 3), dll.toList());
        assertEquals(1, dll.getHead().data);
        assertEquals(3, dll.getTail().data);
        assertEquals(2, dll.getHead().next.data);
        assertEquals(2, dll.getTail().prev.data);
    }

    @Test
    @DisplayName("CircularLinkedListStructure: Circular Linking")
    void testCircularLinkedListStructure() {
        CircularLinkedListStructure cll = new CircularLinkedListStructure();
        cll.insertHead(20);
        cll.insertHead(10);
        assertEquals(List.of(10, 20), cll.toList());
        assertEquals(cll.getHead(), cll.getTail().next);
    }
}
