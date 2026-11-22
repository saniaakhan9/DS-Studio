package com.dsstudio.core.structures.linear;

import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;

import java.util.ArrayList;
import java.util.List;

public class CircularQueueStructure {
    private final int capacity;
    private final Integer[] elements;
    private int front;
    private int rear;
    private int count;

    public CircularQueueStructure(int capacity) {
        this.capacity = capacity;
        this.elements = new Integer[capacity];
        this.front = -1;
        this.rear = -1;
        this.count = 0;
    }

    public CircularQueueStructure(int capacity, int[] initialValues) {
        this.capacity = capacity;
        this.elements = new Integer[capacity];
        this.front = -1;
        this.rear = -1;
        this.count = 0;
        int n = Math.min(capacity, initialValues.length);
        for (int i = 0; i < n; i++) {
            if (this.front == -1) this.front = 0;
            this.rear = (this.rear + 1) % capacity;
            this.elements[this.rear] = initialValues[i];
            this.count++;
        }
    }

    public int getCapacity() { return capacity; }
    public int getSize() { return count; }
    public boolean isEmpty() { return count == 0; }
    public boolean isFull() { return count == capacity; }
    public int getFront() { return front; }
    public int getRear() { return rear; }

    public List<Integer> toSlotList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            list.add(elements[i]);
        }
        return list;
    }

    // ==========================================
    // ENQUEUE
    // ==========================================
    public static final String CIRCULAR_ENQUEUE_JAVA_CODE = 
        "public void enqueue(int value) {\n" +
        "    if ((rear + 1) % capacity == front) {\n" +
        "        throw new IllegalStateException(\"Circular Queue is Full\");\n" +
        "    }\n" +
        "    if (front == -1) front = 0;\n" +
        "    rear = (rear + 1) % capacity; // Wrap around\n" +
        "    elements[rear] = value;\n" +
        "    count++;\n" +
        "}";

    public OperationTrace enqueue(int value) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isFull()) {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.OVERFLOW)
                    .title("Circular Queue Full")
                    .description("Cannot enqueue " + value + "! Condition (rear + 1) % capacity == front holds. All " + capacity + " slots are filled.")
                    .codeLine(2)
                    .elements(toSlotList())
                    .pointer("FRONT", front)
                    .pointer("REAR", rear)
                    .error(true)
                    .build());
            return new OperationTrace("Enqueue", false, "Circular Queue Full", CIRCULAR_ENQUEUE_JAVA_CODE, steps);
        }

        int nextRear = (rear + 1) % capacity;
        boolean wraps = nextRear < rear;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Calculate Next REAR")
                .description("Next rear position is (" + rear + " + 1) % " + capacity + " = " + nextRear + (wraps ? " (Wraps around to 0!)" : "."))
                .codeLine(5)
                .elements(toSlotList())
                .pointer("FRONT", front == -1 ? 0 : front)
                .pointer("NEXT_REAR", nextRear)
                .timeComplexity("O(1)")
                .build());

        if (front == -1) {
            front = 0;
        }
        rear = nextRear;
        elements[rear] = value;
        count++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.ENQUEUE)
                .title("Store Element at Modulo Index")
                .description("Placed " + value + " at slot " + rear + ". Circular queue size is now " + count + "/" + capacity + ".")
                .codeLine(6)
                .elements(toSlotList())
                .highlightedIndices(rear)
                .pointer("FRONT", front)
                .pointer("REAR", rear)
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Enqueue", true, "Enqueued " + value + " at slot " + rear, CIRCULAR_ENQUEUE_JAVA_CODE, steps);
    }

    // ==========================================
    // DEQUEUE
    // ==========================================
    public static final String CIRCULAR_DEQUEUE_JAVA_CODE = 
        "public int dequeue() {\n" +
        "    if (isEmpty()) {\n" +
        "        throw new NoSuchElementException(\"Queue is Empty\");\n" +
        "    }\n" +
        "    int value = elements[front];\n" +
        "    elements[front] = null;\n" +
        "    if (front == rear) {\n" +
        "        front = -1; rear = -1; // Reset when empty\n" +
        "    } else {\n" +
        "        front = (front + 1) % capacity; // Wrap around\n" +
        "    }\n" +
        "    count--;\n" +
        "    return value;\n" +
        "}";

    public OperationTrace dequeue() {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isEmpty()) {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.UNDERFLOW)
                    .title("Circular Queue Empty")
                    .description("Cannot dequeue! Queue is empty.")
                    .codeLine(2)
                    .elements(toSlotList())
                    .error(true)
                    .build());
            return new OperationTrace("Dequeue", false, "Circular Queue Underflow", CIRCULAR_DEQUEUE_JAVA_CODE, steps);
        }

        int dequeuedVal = elements[front];

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.HIGHLIGHT)
                .title("Identify FRONT Slot")
                .description("Removing element " + dequeuedVal + " from slot " + front + ".")
                .codeLine(5)
                .elements(toSlotList())
                .highlightedIndices(front)
                .pointer("FRONT", front)
                .pointer("REAR", rear)
                .timeComplexity("O(1)")
                .build());

        elements[front] = null;
        if (front == rear) {
            front = -1;
            rear = -1;
        } else {
            front = (front + 1) % capacity;
        }
        count--;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.DEQUEUE)
                .title("Advance FRONT with Wrap-Around")
                .description("Dequeued " + dequeuedVal + ". Slot freed. FRONT is now at index " + front + ". Count: " + count + ".")
                .codeLine(10)
                .elements(toSlotList())
                .pointer("FRONT", Math.max(0, front))
                .pointer("REAR", Math.max(0, rear))
                .timeComplexity("O(1)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Dequeue", true, "Dequeued " + dequeuedVal + " from slot", CIRCULAR_DEQUEUE_JAVA_CODE, steps);
    }
}
