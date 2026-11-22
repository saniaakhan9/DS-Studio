package com.dsstudio.core.structures.linear;

import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;

import java.util.ArrayList;
import java.util.List;

public class StackStructure {
    private final int capacity;
    private final int[] elements;
    private int top;

    public StackStructure(int capacity) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.top = -1;
    }

    public StackStructure(int capacity, int[] initialValues) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.top = -1;
        int count = Math.min(capacity, initialValues.length);
        for (int i = 0; i < count; i++) {
            this.elements[++top] = initialValues[i];
        }
    }

    public int getCapacity() { return capacity; }
    public int getSize() { return top + 1; }
    public int getTopIndex() { return top; }
    public boolean isEmpty() { return top == -1; }
    public boolean isFull() { return top == capacity - 1; }

    public List<Integer> toList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <= top; i++) {
            list.add(elements[i]);
        }
        return list;
    }

    // ==========================================
    // PUSH
    // ==========================================
    public static final String PUSH_JAVA_CODE = 
        "public void push(int value) {\n" +
        "    if (top == capacity - 1) {\n" +
        "        throw new StackOverflowError(\"Stack Overflow\");\n" +
        "    }\n" +
        "    top++;\n" +
        "    elements[top] = value;\n" +
        "}";

    public OperationTrace push(int value) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isFull()) {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.OVERFLOW)
                    .title("Stack Overflow Condition")
                    .description("Cannot push " + value + "! Stack is already FULL with " + (top + 1) + " elements (Max: " + capacity + ").")
                    .codeLine(2)
                    .elements(toList())
                    .pointer("TOP", top)
                    .error(true)
                    .build());
            return new OperationTrace("Push", false, "Stack Overflow: capacity " + capacity + " reached", PUSH_JAVA_CODE, steps);
        }

        // Step 1: Pre-push check
        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Verify Capacity")
                .description("Stack has available space (" + (top + 1) + "/" + capacity + "). Value " + value + " is valid.")
                .codeLine(1)
                .elements(toList())
                .pointer("TOP", top)
                .timeComplexity("O(1)")
                .build());

        // Increment TOP
        top++;
        elements[top] = value;

        // Step 2: Push animation
        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.PUSH)
                .title("Increment TOP and Store Value")
                .description("TOP moved to index " + top + ". Placed value " + value + " onto the top of the stack.")
                .codeLine(5)
                .elements(toList())
                .highlightedIndices(top)
                .pointer("TOP", top)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        // Step 3: Complete
        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Push Successful")
                .description("Pushed " + value + " to stack. Current stack size is " + (top + 1) + ".")
                .codeLine(6)
                .elements(toList())
                .highlightedIndices(top)
                .pointer("TOP", top)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Push", true, "Pushed " + value + " to stack", PUSH_JAVA_CODE, steps);
    }

    // ==========================================
    // POP
    // ==========================================
    public static final String POP_JAVA_CODE = 
        "public int pop() {\n" +
        "    if (top == -1) {\n" +
        "        throw new EmptyStackException(); // Underflow\n" +
        "    }\n" +
        "    int value = elements[top];\n" +
        "    top--;\n" +
        "    return value;\n" +
        "}";

    public OperationTrace pop() {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isEmpty()) {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.UNDERFLOW)
                    .title("Stack Underflow Condition")
                    .description("Cannot pop! Stack is EMPTY (TOP = -1).")
                    .codeLine(2)
                    .elements(toList())
                    .error(true)
                    .build());
            return new OperationTrace("Pop", false, "Stack Underflow: stack is empty", POP_JAVA_CODE, steps);
        }

        int poppedValue = elements[top];

        // Step 1: Highlight TOP
        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.HIGHLIGHT)
                .title("Inspect Element at TOP")
                .description("Identified element " + poppedValue + " at current TOP (index " + top + ").")
                .codeLine(4)
                .elements(toList())
                .highlightedIndices(top)
                .pointer("TOP", top)
                .timeComplexity("O(1)")
                .build());

        // Decrement TOP
        top--;

        // Step 2: Pop complete
        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.POP)
                .title("Remove Top Element")
                .description("Popped element " + poppedValue + ". TOP decremented to " + top + ". Remaining size: " + (top + 1) + ".")
                .codeLine(5)
                .elements(toList())
                .pointer("TOP", top)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Pop", true, "Popped " + poppedValue + " from stack", POP_JAVA_CODE, steps);
    }

    // ==========================================
    // PEEK
    // ==========================================
    public static final String PEEK_JAVA_CODE = 
        "public int peek() {\n" +
        "    if (top == -1) {\n" +
        "        throw new EmptyStackException();\n" +
        "    }\n" +
        "    return elements[top];\n" +
        "}";

    public OperationTrace peek() {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isEmpty()) {
            return OperationTrace.error("Peek", "Stack is empty. Nothing to peek.", toList(), PEEK_JAVA_CODE);
        }

        int peekValue = elements[top];

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.PEEK)
                .title("Peek Top Element")
                .description("Top element is " + peekValue + " at index " + top + ". Stack contents remain unmodified.")
                .codeLine(4)
                .elements(toList())
                .highlightedIndices(top)
                .pointer("TOP", top)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Peek", true, "Peeked value " + peekValue + " at TOP", PEEK_JAVA_CODE, steps);
    }
}
