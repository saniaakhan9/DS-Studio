package com.dsstudio.core.structures.linear;

import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;

import java.util.ArrayList;
import java.util.List;

public class SinglyLinkedListStructure {
    public static class Node {
        public int data;
        public Node next;
        public Node(int data) { this.data = data; this.next = null; }
    }

    private Node head;
    private int size;

    public SinglyLinkedListStructure() {
        this.head = null;
        this.size = 0;
    }

    public SinglyLinkedListStructure(int[] initialValues) {
        this.head = null;
        this.size = 0;
        for (int val : initialValues) {
            insertTailRaw(val);
        }
    }

    private void insertTailRaw(int val) {
        Node newNode = new Node(val);
        if (head == null) {
            head = newNode;
        } else {
            Node curr = head;
            while (curr.next != null) curr = curr.next;
            curr.next = newNode;
        }
        size++;
    }

    public Node getHead() { return head; }
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
        "    newNode.next = head;\n" +
        "    head = newNode;\n" +
        "    size++;\n" +
        "}";

    public OperationTrace insertHead(int value) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Create New Node")
                .description("Created new isolated Node(" + value + ") with next = null.")
                .codeLine(1)
                .elements(toList())
                .timeComplexity("O(1)")
                .build());

        // Connect newNode.next to head
        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.POINTER_UPDATE)
                .title("Update newNode.next Pointer")
                .description("Pointing newNode(" + value + ").next to current HEAD (" + (head != null ? head.data : "NULL") + ").")
                .codeLine(2)
                .elements(toList())
                .pointer("HEAD", 0)
                .timeComplexity("O(1)")
                .build());

        Node newNode = new Node(value);
        newNode.next = head;
        head = newNode;
        size++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Update HEAD")
                .description("HEAD updated to point to newNode(" + value + "). Size is now " + size + ".")
                .codeLine(3)
                .elements(toList())
                .highlightedIndices(0)
                .pointer("HEAD", 0)
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
        "    if (head == null) { head = newNode; size++; return; }\n" +
        "    Node curr = head;\n" +
        "    while (curr.next != null) curr = curr.next;\n" +
        "    curr.next = newNode;\n" +
        "    size++;\n" +
        "}";

    public OperationTrace insertTail(int value) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (head == null) {
            return insertHead(value);
        }

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Start Traversal from HEAD")
                .description("Created Node(" + value + "). Traversing from HEAD (" + head.data + ") to locate tail.")
                .codeLine(3)
                .elements(toList())
                .pointer("curr", 0)
                .timeComplexity("O(N)")
                .build());

        Node curr = head;
        int index = 0;
        while (curr.next != null) {
            curr = curr.next;
            index++;
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.POINTER_MOVE)
                    .title("Advance curr Pointer")
                    .description("Traversed to Node(" + curr.data + ") at position " + index + ".")
                    .codeLine(4)
                    .elements(toList())
                    .highlightedIndices(index)
                    .pointer("curr", index)
                    .timeComplexity("O(N)")
                    .build());
        }

        // Attach node
        Node newNode = new Node(value);
        curr.next = newNode;
        size++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.POINTER_UPDATE)
                .title("Link Tail to New Node")
                .description("Linked previous tail Node(" + curr.data + ").next -> Node(" + value + "). Size is now " + size + ".")
                .codeLine(5)
                .elements(toList())
                .highlightedIndices(index + 1)
                .pointer("TAIL", index + 1)
                .timeComplexity("O(N) Traversal / O(1) Linking")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Insert Tail", true, "Inserted " + value + " at TAIL", INSERT_TAIL_JAVA, steps);
    }

    // ==========================================
    // DELETE AT HEAD
    // ==========================================
    public static final String DELETE_HEAD_JAVA = 
        "public int deleteHead() {\n" +
        "    if (head == null) throw new NoSuchElementException();\n" +
        "    int val = head.data;\n" +
        "    head = head.next;\n" +
        "    size--;\n" +
        "    return val;\n" +
        "}";

    public OperationTrace deleteHead() {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (head == null) {
            return OperationTrace.error("Delete Head", "Cannot delete from empty linked list", toList(), DELETE_HEAD_JAVA);
        }

        int removedVal = head.data;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.HIGHLIGHT)
                .title("Target HEAD Node")
                .description("Current HEAD node has value " + removedVal + ". Next node is " + (head.next != null ? head.next.data : "NULL") + ".")
                .codeLine(2)
                .elements(toList())
                .highlightedIndices(0)
                .pointer("HEAD", 0)
                .timeComplexity("O(1)")
                .build());

        head = head.next;
        size--;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.DELETE)
                .title("Advance HEAD Pointer")
                .description("HEAD updated to next node. Node(" + removedVal + ") unlinked and garbage collected. New size: " + size + ".")
                .codeLine(3)
                .elements(toList())
                .pointer("HEAD", 0)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Delete Head", true, "Deleted " + removedVal + " from HEAD", DELETE_HEAD_JAVA, steps);
    }

    // ==========================================
    // SEARCH
    // ==========================================
    public static final String SEARCH_JAVA = 
        "public int search(int target) {\n" +
        "    Node curr = head;\n" +
        "    int index = 0;\n" +
        "    while (curr != null) {\n" +
        "        if (curr.data == target) return index;\n" +
        "        curr = curr.next;\n" +
        "        index++;\n" +
        "    }\n" +
        "    return -1;\n" +
        "}";

    public OperationTrace search(int target) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (head == null) {
            return OperationTrace.error("Search", "List is empty.", toList(), SEARCH_JAVA);
        }

        Node curr = head;
        int index = 0;
        int foundPos = -1;

        while (curr != null) {
            boolean match = (curr.data == target);
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(match ? StepType.FOUND : StepType.COMPARE)
                    .title(match ? "Target Found!" : "Compare Node Value")
                    .description("Inspecting Node at position " + index + " (value: " + curr.data + ") against target " + target + (match ? " -> Match found!" : "."))
                    .codeLine(4)
                    .elements(toList())
                    .highlightedIndices(index)
                    .pointer("curr", index)
                    .timeComplexity("O(N)")
                    .build());

            if (match) {
                foundPos = index;
                break;
            }
            curr = curr.next;
            index++;
        }

        if (foundPos != -1) {
            return new OperationTrace("Search", true, "Found target " + target + " at position " + foundPos, SEARCH_JAVA, steps);
        } else {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.NOT_FOUND)
                    .title("Search Finished")
                    .description("Reached end of list (curr == NULL). Target " + target + " was not found.")
                    .codeLine(8)
                    .elements(toList())
                    .timeComplexity("O(N)")
                    .build());
            return new OperationTrace("Search", false, "Target " + target + " not found", SEARCH_JAVA, steps);
        }
    }
}
