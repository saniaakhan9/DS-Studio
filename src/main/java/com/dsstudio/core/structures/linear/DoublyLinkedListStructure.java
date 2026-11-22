package com.dsstudio.core.structures.linear;

import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;

import java.util.ArrayList;
import java.util.List;

public class DoublyLinkedListStructure {
    public static class Node {
        public int data;
        public Node prev;
        public Node next;
        public Node(int data) { this.data = data; }
    }

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedListStructure() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public DoublyLinkedListStructure(int[] initialValues) {
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
        } else {
            tail.next = node;
            node.prev = tail;
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
        Node curr = head;
        while (curr != null) {
            list.add(curr.data);
            curr = curr.next;
        }
        return list;
    }

    // ==========================================
    // INSERT AT HEAD
    // ==========================================
    public static final String INSERT_HEAD_JAVA = 
        "public void insertHead(int value) {\n" +
        "    Node newNode = new Node(value);\n" +
        "    if (head == null) {\n" +
        "        head = tail = newNode;\n" +
        "    } else {\n" +
        "        newNode.next = head;\n" +
        "        head.prev = newNode;\n" +
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
                .title("Create Doubly Linked Node")
                .description("Created Node(" + value + ") with prev = null and next = null.")
                .codeLine(1)
                .elements(toList())
                .timeComplexity("O(1)")
                .build());

        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
            tail = newNode;
            size++;
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.COMPLETED)
                    .title("Set as HEAD and TAIL")
                    .description("List was empty. Node(" + value + ") is now both HEAD and TAIL.")
                    .codeLine(3)
                    .elements(toList())
                    .highlightedIndices(0)
                    .pointer("HEAD", 0)
                    .pointer("TAIL", 0)
                    .timeComplexity("O(1)")
                    .build());
            return new OperationTrace("Insert Head", true, "Inserted " + value + " as only node", INSERT_HEAD_JAVA, steps);
        }

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.POINTER_UPDATE)
                .title("Link newNode.next -> HEAD and head.prev -> newNode")
                .description("Two-way links established: newNode.next = " + head.data + " and head.prev = " + value + ".")
                .codeLine(5)
                .elements(toList())
                .pointer("HEAD", 0)
                .timeComplexity("O(1)")
                .build());

        newNode.next = head;
        head.prev = newNode;
        head = newNode;
        size++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Update HEAD Pointer")
                .description("HEAD pointer updated to point to Node(" + value + "). Bidirectional integrity maintained.")
                .codeLine(7)
                .elements(toList())
                .highlightedIndices(0)
                .pointer("HEAD", 0)
                .pointer("TAIL", size - 1)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Insert Head", true, "Inserted " + value + " at HEAD", INSERT_HEAD_JAVA, steps);
    }

    // ==========================================
    // INSERT AT TAIL
    // ==========================================
    public static final String INSERT_TAIL_JAVA = 
        "public void insertTail(int value) {\n" +
        "    Node newNode = new Node(value);\n" +
        "    if (tail == null) {\n" +
        "        head = tail = newNode;\n" +
        "    } else {\n" +
        "        tail.next = newNode;\n" +
        "        newNode.prev = tail;\n" +
        "        tail = newNode;\n" +
        "    }\n" +
        "    size++;\n" +
        "}";

    public OperationTrace insertTail(int value) {
        if (head == null) return insertHead(value);

        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Access TAIL Pointer Directly")
                .description("Direct access to TAIL node (" + tail.data + ") in O(1) without full list traversal!")
                .codeLine(1)
                .elements(toList())
                .pointer("TAIL", size - 1)
                .timeComplexity("O(1)")
                .build());

        Node newNode = new Node(value);
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        size++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Link and Update TAIL")
                .description("Linked tail.next -> " + value + " and newNode.prev -> previous tail. Updated TAIL pointer. O(1) complexity.")
                .codeLine(7)
                .elements(toList())
                .highlightedIndices(size - 1)
                .pointer("HEAD", 0)
                .pointer("TAIL", size - 1)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Insert Tail", true, "Inserted " + value + " at TAIL", INSERT_TAIL_JAVA, steps);
    }
}
