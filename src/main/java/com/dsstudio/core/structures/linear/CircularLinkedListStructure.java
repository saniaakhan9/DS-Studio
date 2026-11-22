package com.dsstudio.core.structures.linear;

import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;

import java.util.ArrayList;
import java.util.List;

public class CircularLinkedListStructure {
    public static class Node {
        public int data;
        public Node next;
        public Node(int data) { this.data = data; this.next = null; }
    }

    private Node head;
    private Node tail;
    private int size;

    public CircularLinkedListStructure() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public CircularLinkedListStructure(int[] initialValues) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        for (int v : initialValues) {
            insertTailRaw(v);
        }
    }

    private void insertTailRaw(int val) {
        Node node = new Node(val);
        if (head == null) {
            head = node;
            tail = node;
            node.next = head;
        } else {
            tail.next = node;
            node.next = head;
            tail = node;
        }
        size++;
    }

    public Node getHead() { return head; }
    public Node getTail() { return tail; }
    public int getSize() { return size; }
    public boolean isEmpty() { return size == 0; }

    public List<Integer> toList() {
        List<Integer> list = new ArrayList<>();
        if (head == null) return list;
        Node curr = head;
        do {
            list.add(curr.data);
            curr = curr.next;
        } while (curr != head);
        return list;
    }

    // ==========================================
    // INSERT AT HEAD
    // ==========================================
    public static final String CIRCULAR_INSERT_HEAD_JAVA = 
        "public void insertHead(int value) {\n" +
        "    Node newNode = new Node(value);\n" +
        "    if (head == null) {\n" +
        "        head = tail = newNode;\n" +
        "        newNode.next = head;\n" +
        "    } else {\n" +
        "        newNode.next = head;\n" +
        "        tail.next = newNode; // Circular wrap\n" +
        "        head = newNode;\n" +
        "    }\n" +
        "    size++;\n" +
        "}";

    public OperationTrace insertHead(int value) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Create New Node")
                .description("Created Node(" + value + "). Circular list has " + size + " nodes.")
                .codeLine(1)
                .elements(toList())
                .timeComplexity("O(1)")
                .build());

        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.next = head;
            size++;
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.COMPLETED)
                    .title("Self-Referential Circular Node")
                    .description("Single node points to itself: Node(" + value + ").next = Node(" + value + ").")
                    .codeLine(4)
                    .elements(toList())
                    .highlightedIndices(0)
                    .pointer("HEAD", 0)
                    .pointer("TAIL", 0)
                    .timeComplexity("O(1)")
                    .build());
            return new OperationTrace("Insert Head", true, "Inserted first circular node", CIRCULAR_INSERT_HEAD_JAVA, steps);
        }

        newNode.next = head;
        tail.next = newNode;
        head = newNode;
        size++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Update HEAD and Circular Tail Link")
                .description("newNode.next -> old HEAD, tail.next -> newNode (" + value + "). HEAD updated to " + value + ".")
                .codeLine(8)
                .elements(toList())
                .highlightedIndices(0)
                .pointer("HEAD", 0)
                .pointer("TAIL", size - 1)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Insert Head", true, "Inserted " + value + " at HEAD", CIRCULAR_INSERT_HEAD_JAVA, steps);
    }
}
