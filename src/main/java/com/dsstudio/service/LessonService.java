package com.dsstudio.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LessonService {
    public static class LessonData {
        public String id;
        public String title;
        public String category; // Linear, Non-Linear, Algorithm
        public String overview;
        public String realWorldUsage;
        public String formalDefinition;
        public String memoryModel;
        public Map<String, String> operations;
        public Map<String, String> complexities;
        public List<String> commonPitfalls;
        public List<String> vivaQuestions;
        public String javaCodeSnippet;
    }

    private static final Map<String, LessonData> LESSONS = new LinkedHashMap<>();

    static {
        // 1. ARRAYS
        LessonData arr = new LessonData();
        arr.id = "arrays";
        arr.title = "Arrays & Memory Indexing";
        arr.category = "Linear Data Structures";
        arr.overview = "An array is a contiguous memory collection of fixed-size elements of identical data types, accessed via zero-based indices.";
        arr.formalDefinition = "A homogeneous, linear, contiguous collection characterized by constant-time random access: Address(A[i]) = Base + (i * element_size).";
        arr.realWorldUsage = "Lookup tables, pixel buffers in graphics processing, mathematical matrix computations, basis for dynamic lists (ArrayList).";
        arr.memoryModel = "Contiguous block in heap memory. Elements sit back-to-back with zero pointer overhead, granting maximum CPU cache locality.";
        arr.operations = Map.of(
                "Access / Update by Index", "O(1) direct offset calculation using base pointer.",
                "Insertion at End", "O(1) if capacity remains; O(N) if resize required.",
                "Insertion at Middle / Beginning", "O(N) due to rightward element shifting.",
                "Deletion", "O(N) due to leftward compaction of elements.",
                "Linear Search", "O(N) sequential comparison across elements."
        );
        arr.complexities = Map.of(
                "Access", "O(1)",
                "Search (Linear)", "O(N)",
                "Search (Binary)", "O(log N) [Sorted]",
                "Insertion", "O(N)",
                "Deletion", "O(N)",
                "Space Complexity", "O(N)"
        );
        arr.commonPitfalls = List.of(
                "ArrayIndexOutOfBoundsException: attempting index < 0 or >= size.",
                "Confusion between array capacity (allocated storage) and current logical size (occupied slots).",
                "Off-by-one errors during boundary shifting loops (i < size vs i <= size)."
        );
        arr.vivaQuestions = List.of(
                "Q: How is an array's random access O(1) in physical hardware?\nA: The CPU calculates memory offset in hardware using base address + index * scale factor in a single cycle.",
                "Q: What is the limitation of static arrays?\nA: Fixed pre-allocated capacity; cannot expand without allocating a new larger array and copying all elements.",
                "Q: Why is binary search faster than linear search?\nA: It eliminates half of the remaining search space per iteration, yielding O(log N) runtime."
        );
        arr.javaCodeSnippet = 
            "// Contiguous Array Insertion\n" +
            "public void insert(int[] arr, int index, int value, int size) {\n" +
            "    for (int i = size - 1; i >= index; i--) {\n" +
            "        arr[i + 1] = arr[i]; // Shift right\n" +
            "    }\n" +
            "    arr[index] = value;\n" +
            "}";
        LESSONS.put(arr.id, arr);

        // 2. STACK
        LessonData stk = new LessonData();
        stk.id = "stack";
        stk.title = "Stack (LIFO Structure)";
        stk.category = "Linear Data Structures";
        stk.overview = "A linear data structure operating under the Last-In, First-Out (LIFO) protocol. Additions and removals occur exclusively at the TOP.";
        stk.formalDefinition = "An abstract data type with two fundamental operations: push (adds to top) and pop (removes from top), constrained by LIFO ordering.";
        stk.realWorldUsage = "JVM method call stack / activation records, browser back/forward history, undo/redo mechanisms in text editors, bracket balancing.";
        stk.memoryModel = "Sequential memory with a single pointer `top`. Elements below top remain protected and inaccessible until upper elements are popped.";
        stk.operations = Map.of(
                "Push", "O(1) increments top and assigns value.",
                "Pop", "O(1) retrieves value and decrements top.",
                "Peek / Top", "O(1) inspects element at top without removing it.",
                "isEmpty / isFull", "O(1) comparison against -1 or capacity-1."
        );
        stk.complexities = Map.of(
                "Push", "O(1)",
                "Pop", "O(1)",
                "Peek", "O(1)",
                "Search", "O(N)",
                "Space Complexity", "O(N)"
        );
        stk.commonPitfalls = List.of(
                "Stack Overflow: attempting to push into an already saturated stack.",
                "Stack Underflow: invoking pop() or peek() on an empty stack (top == -1).",
                "Reversing order inadvertently when transferring items between two stacks."
        );
        stk.vivaQuestions = List.of(
                "Q: How does recursion utilize a stack?\nA: Each recursive invocation creates a new stack frame on the call stack holding local parameters and return addresses.",
                "Q: Can a stack be implemented using a linked list?\nA: Yes, inserting and deleting at the HEAD provides O(1) push and pop with dynamic sizing."
        );
        stk.javaCodeSnippet = 
            "// LIFO Stack Push & Pop\n" +
            "public void push(int val) {\n" +
            "    if (top == capacity - 1) throw new StackOverflowError();\n" +
            "    elements[++top] = val;\n" +
            "}\n" +
            "public int pop() {\n" +
            "    if (top == -1) throw new EmptyStackException();\n" +
            "    return elements[top--];\n" +
            "}";
        LESSONS.put(stk.id, stk);

        // 3. QUEUE & CIRCULAR QUEUE
        LessonData q = new LessonData();
        q.id = "queue";
        q.title = "Queue & Circular Queue (FIFO)";
        q.category = "Linear Data Structures";
        q.overview = "A First-In, First-Out (FIFO) linear structure where insertions occur at the REAR and removals occur at the FRONT.";
        q.formalDefinition = "An ordered list where elements are inserted at rear and removed from front. Circular queues utilize modular arithmetic: (index + 1) % capacity.";
        q.realWorldUsage = "Operating system CPU scheduling (Round Robin), printer spoolers, asynchronous message brokers (RabbitMQ/Kafka), BFS graph traversal.";
        q.memoryModel = "Array with two floating indices (front and rear). Circular wrap-around prevents false overflow by recycling freed slots.";
        q.operations = Map.of(
                "Enqueue", "O(1) increments rear circularly and stores element.",
                "Dequeue", "O(1) extracts front element and advances front.",
                "Peek Front", "O(1) inspects front element.",
                "isFull (Circular)", "O(1) verified by `(rear + 1) % capacity == front`."
        );
        q.complexities = Map.of(
                "Enqueue", "O(1)",
                "Dequeue", "O(1)",
                "Front Peek", "O(1)",
                "Search", "O(N)",
                "Space Complexity", "O(N)"
        );
        q.commonPitfalls = List.of(
                "False Overflow in linear array queues when rear reaches end despite freed front slots.",
                "Incorrect empty vs full condition in circular queues without a count or empty flag sentinel.",
                "Dequeuing on empty queue without validating front != -1."
        );
        q.vivaQuestions = List.of(
                "Q: How does circular queue eliminate false overflow?\nA: By using modular arithmetic `(rear + 1) % capacity`, rear wraps around to index 0 when space is available.",
                "Q: What is a priority queue?\nA: An extension where elements are dequeued based on priority rather than arrival time."
        );
        q.javaCodeSnippet = 
            "// Circular Queue Modulo Indexing\n" +
            "public void enqueue(int val) {\n" +
            "    if ((rear + 1) % capacity == front) throw new FullQueueException();\n" +
            "    if (front == -1) front = 0;\n" +
            "    rear = (rear + 1) % capacity;\n" +
            "    arr[rear] = val;\n" +
            "}";
        LESSONS.put(q.id, q);

        // 4. SINGLY LINKED LIST
        LessonData sll = new LessonData();
        sll.id = "singly_linked_list";
        sll.title = "Singly Linked List";
        sll.category = "Linear Data Structures";
        sll.overview = "A dynamic data structure consisting of discrete nodes connected by forward references (`next`).";
        sll.formalDefinition = "A collection of nodes where each node contains `data` and a single reference pointer `next` terminating in `null`.";
        sll.realWorldUsage = "Symbol tables in compilers, dynamic memory allocation freelists, implementation of stacks and queues.";
        sll.memoryModel = "Scattered non-contiguous heap allocations. Each node holds data + reference pointer (4 or 8 bytes overhead).";
        sll.operations = Map.of(
                "Insert at Head", "O(1) update newNode.next to head, then head to newNode.",
                "Insert at Tail", "O(N) without tail pointer; O(1) with maintained tail pointer.",
                "Delete Head", "O(1) advance head to head.next.",
                "Traversal / Search", "O(N) sequential pointer chasing."
        );
        sll.complexities = Map.of(
                "Insert Head", "O(1)",
                "Insert Tail", "O(N) [O(1) with tail]",
                "Delete Head", "O(1)",
                "Delete Tail", "O(N)",
                "Search", "O(N)",
                "Space Complexity", "O(N)"
        );
        sll.commonPitfalls = List.of(
                "Losing list reference by updating head before saving `newNode.next = head`.",
                "NullPointerException when traversing past tail (`curr.next.next` on single-node list).",
                "Memory leaks in languages without garbage collection; or cycle creation in Java."
        );
        sll.vivaQuestions = List.of(
                "Q: How do you detect a loop in a linked list?\nA: Floyd's Cycle-Finding Algorithm (Tortoise and Hare) with two pointers moving at speed 1 and 2.",
                "Q: Contrast Array vs Linked List.\nA: Arrays have O(1) random access but fixed size and O(N) insertion; Linked Lists have dynamic size and O(1) head insertion but O(N) access."
        );
        sll.javaCodeSnippet = 
            "// Singly Linked List Insertion\n" +
            "public void insertHead(int value) {\n" +
            "    Node newNode = new Node(value);\n" +
            "    newNode.next = head;\n" +
            "    head = newNode;\n" +
            "}";
        LESSONS.put(sll.id, sll);
    }

    public static List<LessonData> getAllLessons() {
        return new ArrayList<>(LESSONS.values());
    }

    public static LessonData getLesson(String id) {
        return LESSONS.get(id);
    }
}
