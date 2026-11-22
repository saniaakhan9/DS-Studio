package com.dsstudio.core.structures.linear;

import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;

import java.util.ArrayList;
import java.util.List;

public class QueueStructure {
    private final int capacity;
    private final int[] elements;
    private int front;
    private int rear;
    private int count;

    public QueueStructure(int capacity) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.front = 0;
        this.rear = -1;
        this.count = 0;
    }

    public QueueStructure(int capacity, int[] initialValues) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.front = 0;
        this.rear = -1;
        this.count = 0;
        int n = Math.min(capacity, initialValues.length);
        for (int i = 0; i < n; i++) {
            this.elements[++this.rear] = initialValues[i];
            this.count++;
        }
    }

    public int getCapacity() { return capacity; }
    public int getSize() { return count; }
    public boolean isEmpty() { return count == 0; }
    public boolean isFull() { return count == capacity; }
    public int getFront() { return front; }
    public int getRear() { return rear; }

    public List<Integer> toList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(elements[front + i]);
        }
        return list;
    }

    public List<Integer> toRawElements() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (i >= front && i <= rear) {
                list.add(elements[i]);
            } else {
                list.add(null);
            }
        }
        return list;
    }

    // ==========================================
    // ENQUEUE
    // ==========================================
    public static final String ENQUEUE_JAVA_CODE = 
        "public void enqueue(int value) {\n" +
        "    if (rear == capacity - 1) {\n" +
        "        throw new IllegalStateException(\"Queue is Full\");\n" +
        "    }\n" +
        "    rear++;\n" +
        "    elements[rear] = value;\n" +
        "    count++;\n" +
        "}";

    public OperationTrace enqueue(int value) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (rear == capacity - 1) {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.OVERFLOW)
                    .title("Queue Overflow")
                    .description("Cannot enqueue " + value + "! Rear reached end of array (" + rear + "). Linear queue space exhausted.")
                    .codeLine(2)
                    .elements(toList())
                    .pointer("FRONT", front)
                    .pointer("REAR", rear)
                    .error(true)
                    .build());
            return new OperationTrace("Enqueue", false, "Queue Overflow: rear reached capacity limit", ENQUEUE_JAVA_CODE, steps);
        }

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Validate Enqueue")
                .description("Queue has space for element " + value + " at index " + (rear + 1) + ".")
                .codeLine(1)
                .elements(toList())
                .pointer("FRONT", front)
                .pointer("REAR", Math.max(0, rear))
                .timeComplexity("O(1)")
                .build());

        rear++;
        elements[rear] = value;
        count++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.ENQUEUE)
                .title("Advance REAR and Store Element")
                .description("REAR moved to index " + rear + ". Enqueued value " + value + ". Total count: " + count + ".")
                .codeLine(5)
                .elements(toList())
                .highlightedIndices(toList().size() - 1)
                .pointer("FRONT", 0)
                .pointer("REAR", toList().size() - 1)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Enqueue", true, "Enqueued " + value + " at REAR", ENQUEUE_JAVA_CODE, steps);
    }

    // ==========================================
    // DEQUEUE
    // ==========================================
    public static final String DEQUEUE_JAVA_CODE = 
        "public int dequeue() {\n" +
        "    if (isEmpty()) {\n" +
        "        throw new NoSuchElementException(\"Queue Underflow\");\n" +
        "    }\n" +
        "    int removed = elements[front];\n" +
        "    front++;\n" +
        "    count--;\n" +
        "    return removed;\n" +
        "}";

    public OperationTrace dequeue() {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isEmpty()) {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.UNDERFLOW)
                    .title("Queue Underflow")
                    .description("Cannot dequeue! Queue is empty.")
                    .codeLine(2)
                    .elements(toList())
                    .error(true)
                    .build());
            return new OperationTrace("Dequeue", false, "Queue Underflow: queue is empty", DEQUEUE_JAVA_CODE, steps);
        }

        int dequeuedVal = elements[front];

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.HIGHLIGHT)
                .title("Inspect Element at FRONT")
                .description("Element " + dequeuedVal + " located at FRONT (index " + front + "). Ready to remove according to FIFO.")
                .codeLine(4)
                .elements(toList())
                .highlightedIndices(0)
                .pointer("FRONT", 0)
                .timeComplexity("O(1)")
                .build());

        front++;
        count--;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.DEQUEUE)
                .title("Remove from FRONT")
                .description("Dequeued " + dequeuedVal + ". FRONT advanced. Remaining elements in queue: " + count + ".")
                .codeLine(6)
                .elements(toList())
                .pointer("FRONT", 0)
                .pointer("REAR", Math.max(0, count - 1))
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Dequeue", true, "Dequeued " + dequeuedVal + " from FRONT", DEQUEUE_JAVA_CODE, steps);
    }
}
