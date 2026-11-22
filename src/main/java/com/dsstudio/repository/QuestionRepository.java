package com.dsstudio.repository;

import com.dsstudio.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionRepository {
    private static final List<Question> QUESTIONS = new ArrayList<>();

    static {
        // ARRAYS
        QUESTIONS.add(new Question(
                "ARR-01", "Arrays", "CONCEPT", "BEGINNER",
                "Given a 1D array of integers declared as int[] arr = new int[10] with base address 1000 and 4 bytes per integer, what is the memory address of arr[4]?",
                List.of("1004", "1016", "1020", "1040"),
                1,
                "Formula for 1D array address: Address(arr[i]) = Base + (i * size). Here: 1000 + (4 * 4) = 1016.",
                null, 15
        ));

        QUESTIONS.add(new Question(
                "ARR-02", "Arrays", "COMPLEXITY", "BEGINNER",
                "What is the worst-case time complexity of inserting an element at index 0 of an unsorted array of size N?",
                List.of("O(1)", "O(log N)", "O(N)", "O(N²)"),
                2,
                "Inserting at index 0 requires shifting all existing N elements one position to the right, which takes O(N) operations.",
                "for (int i = size - 1; i >= 0; i--) {\n    arr[i + 1] = arr[i]; // Shifts N items\n}", 15
        ));

        QUESTIONS.add(new Question(
                "ARR-03", "Arrays", "OUTPUT", "INTERMEDIATE",
                "What will be the output of executing the following Java array reversal code snippet?",
                List.of("[5, 4, 3, 2, 1]", "[1, 2, 3, 4, 5]", "[5, 2, 3, 4, 1]", "ArrayIndexOutOfBoundsException"),
                0,
                "The two-pointer approach starts with left=0 and right=4, swapping elements until the pointers cross at index 2, cleanly reversing the entire array.",
                "int[] a = {1, 2, 3, 4, 5};\nint i = 0, j = a.length - 1;\nwhile (i < j) {\n    int t = a[i]; a[i] = a[j]; a[j] = t;\n    i++; j--;\n}\nSystem.out.println(Arrays.toString(a));", 20
        ));

        // STACK
        QUESTIONS.add(new Question(
                "STK-01", "Stack", "CONCEPT", "BEGINNER",
                "Which primary data structure principle governs Stack behavior, and which typical system component relies on it?",
                List.of("FIFO; CPU Scheduler", "LIFO; Call Stack for function recursion", "Priority; Cache eviction", "Associative; Symbol table"),
                1,
                "Stack follows LIFO (Last In First Out). The JVM uses a runtime Call Stack to push activation records for method invocations and pop them when execution returns.",
                null, 15
        ));

        QUESTIONS.add(new Question(
                "STK-02", "Stack", "DEBUG", "INTERMEDIATE",
                "Identify the bug in this array-based stack implementation when pop() is called on an empty stack:",
                List.of("No check for top == capacity - 1", "Missing check for top == -1 leading to ArrayIndexOutOfBoundsException", "top is decremented before value retrieval", "Return type should be boolean"),
                1,
                "If top == -1 and pop() accesses elements[top--], Java throws an ArrayIndexOutOfBoundsException because index -1 is invalid. An underflow guard is required.",
                "public int pop() {\n    // Missing if (top == -1) check!\n    return elements[top--];\n}", 20
        ));

        QUESTIONS.add(new Question(
                "STK-03", "Stack", "DRY_RUN", "INTERMEDIATE",
                "Trace the stack operations: push(10), push(20), pop(), push(30), push(40), pop(), peek(). What value does peek() return?",
                List.of("10", "20", "30", "40"),
                2,
                "Trace: push(10)->[10]; push(20)->[10,20]; pop()->removes 20; push(30)->[10,30]; push(40)->[10,30,40]; pop()->removes 40; peek() observes 30.",
                null, 20
        ));

        // QUEUE & CIRCULAR QUEUE
        QUESTIONS.add(new Question(
                "QUE-01", "Queue", "CONCEPT", "BEGINNER",
                "Why is a Circular Queue preferred over a standard linear array queue for fixed buffers?",
                List.of("Linear queues take O(N) to peek", "Linear queues can cause false overflow when elements are dequeued but front > 0", "Circular queues support random index access in O(log N)", "Circular queues use dynamic pointers"),
                1,
                "In a linear array queue, dequeuing simply increments front. Even if slots 0..front-1 become empty, rear hitting capacity triggers false overflow. Circular modulo arithmetic recycles freed slots.",
                null, 20
        ));

        QUESTIONS.add(new Question(
                "QUE-02", "Queue", "COMPLEXITY", "BEGINNER",
                "In a properly implemented Circular Queue of capacity N, what is the time complexity of enqueue() and dequeue()?",
                List.of("O(1) and O(1)", "O(1) and O(N)", "O(N) and O(1)", "O(log N) and O(1)"),
                0,
                "Both enqueue and dequeue operate in O(1) constant time because rear and front indices are incremented using modular arithmetic without shifting any elements.",
                "rear = (rear + 1) % capacity; // O(1) constant time calculation", 15
        ));

        // LINKED LISTS
        QUESTIONS.add(new Question(
                "LL-01", "Singly Linked List", "CONCEPT", "BEGINNER",
                "What is the primary advantage of a Singly Linked List over a fixed-size Array?",
                List.of("Faster O(1) random index access", "Dynamic memory allocation without continuous block pre-allocation", "Less memory overhead per element", "Cache friendliness"),
                1,
                "Linked lists allocate nodes on heap as needed, eliminating the need to pre-allocate contiguous chunks or resize arrays. However, they incur extra pointer memory overhead.",
                null, 15
        ));

        QUESTIONS.add(new Question(
                "LL-02", "Singly Linked List", "DEBUG", "INTERMEDIATE",
                "What happens if a developer writes `head = newNode; newNode.next = head;` during insert at beginning?",
                List.of("Node is properly inserted", "Creates an infinite self-referential cycle and loses the previous list", "Throws NullPointerException immediately", "List size doubles"),
                1,
                "Setting head = newNode first overwrites head before capturing the old head reference. Then newNode.next = head makes newNode point to itself, losing all subsequent nodes!",
                "// WRONG ORDER:\nhead = newNode;\nnewNode.next = head; // Points to itself!\n\n// CORRECT:\nnewNode.next = head;\nhead = newNode;", 25
        ));

        QUESTIONS.add(new Question(
                "LL-03", "Doubly Linked List", "CONCEPT", "INTERMEDIATE",
                "Why is deleting a given node X in a Doubly Linked List O(1), whereas in a Singly Linked List it is O(N)?",
                List.of("Doubly linked lists have sorted values", "X.prev gives immediate O(1) access to the predecessor node without traversal", "DLL does not store next pointers", "Nodes are stored in an array"),
                1,
                "To delete a node in a Singly Linked List, you must traverse from HEAD to find node.prev. In a Doubly Linked List, X.prev is directly accessible in O(1).",
                "x.prev.next = x.next;\nif (x.next != null) x.next.prev = x.prev;", 20
        ));

        // SORTING & COMPLEXITY
        QUESTIONS.add(new Question(
                "SRT-01", "Algorithms", "COMPLEXITY", "INTERMEDIATE",
                "What is the best-case time complexity of Bubble Sort when an early termination `swapped == false` flag is used on an already sorted array?",
                List.of("O(1)", "O(log N)", "O(N)", "O(N²)"),
                2,
                "With an early-break flag, the first pass verifies all adjacent pairs arr[j] <= arr[j+1] with 0 swaps, terminating immediately after N-1 comparisons in O(N) time.",
                "if (!swapped) break; // O(N) best case", 20
        ));
    }

    public static List<Question> getAll() {
        return new ArrayList<>(QUESTIONS);
    }

    public static List<Question> getByTopic(String topic) {
        if (topic == null || topic.equalsIgnoreCase("ALL")) {
            return getAll();
        }
        return QUESTIONS.stream()
                .filter(q -> q.getTopic().equalsIgnoreCase(topic))
                .collect(Collectors.toList());
    }

    public static List<Question> getByDifficulty(String diff) {
        return QUESTIONS.stream()
                .filter(q -> q.getDifficulty().equalsIgnoreCase(diff))
                .collect(Collectors.toList());
    }
}
