package com.dsstudio.core.structures.linear;

import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayStructure {
    private final int capacity;
    private final int[] elements;
    private int size;

    public ArrayStructure(int capacity) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        this.size = 0;
    }

    public ArrayStructure(int capacity, int[] initialValues) {
        this.capacity = capacity;
        this.elements = new int[capacity];
        int count = Math.min(capacity, initialValues.length);
        System.arraycopy(initialValues, 0, this.elements, 0, count);
        this.size = count;
    }

    public int getCapacity() { return capacity; }
    public int getSize() { return size; }
    public boolean isEmpty() { return size == 0; }
    public boolean isFull() { return size == capacity; }

    public List<Integer> toList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(elements[i]);
        }
        return list;
    }

    public List<Integer> toCapacityList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (i < size) {
                list.add(elements[i]);
            } else {
                list.add(null);
            }
        }
        return list;
    }

    // ==========================================
    // INSERT AT INDEX
    // ==========================================
    public static final String INSERT_JAVA_CODE = 
        "public boolean insert(int index, int value) {\n" +
        "    if (size >= capacity || index < 0 || index > size) {\n" +
        "        return false; // Out of bounds or full\n" +
        "    }\n" +
        "    for (int i = size - 1; i >= index; i--) {\n" +
        "        arr[i + 1] = arr[i]; // Shift right\n" +
        "    }\n" +
        "    arr[index] = value; // Place element\n" +
        "    size++;\n" +
        "    return true;\n" +
        "}";

    public OperationTrace insert(int index, int value) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isFull()) {
            return OperationTrace.error("Insert", "Array is FULL (Capacity " + capacity + " reached). Cannot insert " + value, toList(), INSERT_JAVA_CODE);
        }
        if (index < 0 || index > size) {
            return OperationTrace.error("Insert", "Index " + index + " is out of bounds (Valid range: 0 to " + size + ")", toList(), INSERT_JAVA_CODE);
        }

        // Step 1: Initial state & highlight index
        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Verify Index & Capacity")
                .description("Validated index " + index + " and array capacity (" + size + "/" + capacity + "). Ready to insert " + value + ".")
                .codeLine(1)
                .elements(toList())
                .highlightedIndices(index)
                .timeComplexity("O(1)")
                .build());

        // Working copy for trace simulation
        int[] temp = Arrays.copyOf(elements, capacity);
        int currentSize = size;

        // Step 2..k: Shifts
        for (int i = currentSize - 1; i >= index; i--) {
            int shiftingVal = temp[i];
            temp[i + 1] = shiftingVal;
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.SHIFT_RIGHT)
                    .title("Shift Element Right")
                    .description("Shifting element " + shiftingVal + " from index " + i + " to index " + (i + 1) + " to make room.")
                    .codeLine(5)
                    .elements(copyList(temp, currentSize + 1))
                    .highlightedIndices(i, i + 1)
                    .timeComplexity("O(N)")
                    .build());
        }

        // Place value
        temp[index] = value;
        currentSize++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INSERT)
                .title("Place Element")
                .description("Inserted value " + value + " at target index " + index + ".")
                .codeLine(7)
                .elements(copyList(temp, currentSize))
                .highlightedIndices(index)
                .timeComplexity("O(1)")
                .build());

        // Commit real state
        for (int i = size - 1; i >= index; i--) {
            elements[i + 1] = elements[i];
        }
        elements[index] = value;
        size++;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Insertion Complete")
                .description("Successfully inserted " + value + " at index " + index + ". New size is " + size + ".")
                .codeLine(8)
                .elements(toList())
                .highlightedIndices(index)
                .timeComplexity("O(N) Worst / O(1) Best at end")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Insert", true, "Inserted " + value + " at index " + index, INSERT_JAVA_CODE, steps);
    }

    // ==========================================
    // DELETE AT INDEX
    // ==========================================
    public static final String DELETE_JAVA_CODE = 
        "public int delete(int index) {\n" +
        "    if (size == 0 || index < 0 || index >= size) {\n" +
        "        throw new IndexOutOfBoundsException();\n" +
        "    }\n" +
        "    int removed = arr[index];\n" +
        "    for (int i = index; i < size - 1; i++) {\n" +
        "        arr[i] = arr[i + 1]; // Shift left\n" +
        "    }\n" +
        "    size--;\n" +
        "    return removed;\n" +
        "}";

    public OperationTrace delete(int index) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isEmpty()) {
            return OperationTrace.error("Delete", "Array is EMPTY. Cannot delete from an empty array.", toList(), DELETE_JAVA_CODE);
        }
        if (index < 0 || index >= size) {
            return OperationTrace.error("Delete", "Index " + index + " is out of bounds (Valid range: 0 to " + (size - 1) + ")", toList(), DELETE_JAVA_CODE);
        }

        int removedValue = elements[index];

        // Step 1: Highlight target
        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.HIGHLIGHT)
                .title("Locate Element to Delete")
                .description("Target element " + removedValue + " found at index " + index + ". Preparing deletion.")
                .codeLine(4)
                .elements(toList())
                .highlightedIndices(index)
                .timeComplexity("O(1)")
                .build());

        int[] temp = Arrays.copyOf(elements, capacity);

        // Step 2..k: Shift left
        for (int i = index; i < size - 1; i++) {
            temp[i] = temp[i + 1];
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.SHIFT_LEFT)
                    .title("Shift Element Left")
                    .description("Shifting element " + temp[i] + " from index " + (i + 1) + " to index " + i + ".")
                    .codeLine(6)
                    .elements(copyList(temp, size))
                    .highlightedIndices(i, i + 1)
                    .timeComplexity("O(N)")
                    .build());
        }

        // Commit real state
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Deletion Complete")
                .description("Successfully removed " + removedValue + " from index " + index + ". New size is " + size + ".")
                .codeLine(8)
                .elements(toList())
                .timeComplexity("O(N) Worst / O(1) Best at end")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Delete", true, "Deleted " + removedValue + " from index " + index, DELETE_JAVA_CODE, steps);
    }

    // ==========================================
    // LINEAR SEARCH
    // ==========================================
    public static final String LINEAR_SEARCH_JAVA_CODE = 
        "public int linearSearch(int target) {\n" +
        "    for (int i = 0; i < size; i++) {\n" +
        "        if (arr[i] == target) {\n" +
        "            return i; // Found target\n" +
        "        }\n" +
        "    }\n" +
        "    return -1; // Target not found\n" +
        "}";

    public OperationTrace linearSearch(int target) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isEmpty()) {
            return OperationTrace.error("Linear Search", "Array is empty. Search target cannot be found.", toList(), LINEAR_SEARCH_JAVA_CODE);
        }

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Start Linear Search")
                .description("Searching for target value " + target + " across " + size + " elements from index 0.")
                .codeLine(1)
                .elements(toList())
                .timeComplexity("O(N)")
                .build());

        int foundIndex = -1;
        for (int i = 0; i < size; i++) {
            boolean match = (elements[i] == target);
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(match ? StepType.FOUND : StepType.COMPARE)
                    .title(match ? "Target Found!" : "Compare Element")
                    .description("Comparing index " + i + " (Value: " + elements[i] + ") with target " + target + (match ? " -> Match found!" : " -> No match, advancing."))
                    .codeLine(2)
                    .elements(toList())
                    .highlightedIndices(i)
                    .timeComplexity("O(N)")
                    .build());

            if (match) {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex != -1) {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.COMPLETED)
                    .title("Search Successful")
                    .description("Target " + target + " found at index " + foundIndex + ".")
                    .codeLine(3)
                    .elements(toList())
                    .highlightedIndices(foundIndex)
                    .timeComplexity("O(N)")
                    .build());
            return new OperationTrace("Linear Search", true, "Found target " + target + " at index " + foundIndex, LINEAR_SEARCH_JAVA_CODE, steps);
        } else {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.NOT_FOUND)
                    .title("Target Not Found")
                    .description("Target " + target + " is not present in the array after checking all " + size + " elements.")
                    .codeLine(6)
                    .elements(toList())
                    .timeComplexity("O(N)")
                    .build());
            return new OperationTrace("Linear Search", false, "Target " + target + " not found in array", LINEAR_SEARCH_JAVA_CODE, steps);
        }
    }

    // ==========================================
    // BINARY SEARCH
    // ==========================================
    public static final String BINARY_SEARCH_JAVA_CODE = 
        "public int binarySearch(int target) {\n" +
        "    int low = 0, high = size - 1;\n" +
        "    while (low <= high) {\n" +
        "        int mid = low + (high - low) / 2;\n" +
        "        if (arr[mid] == target) return mid;\n" +
        "        if (arr[mid] < target) low = mid + 1;\n" +
        "        else high = mid - 1;\n" +
        "    }\n" +
        "    return -1;\n" +
        "}";

    public OperationTrace binarySearch(int target) {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (isEmpty()) {
            return OperationTrace.error("Binary Search", "Array is empty.", toList(), BINARY_SEARCH_JAVA_CODE);
        }

        // Check if sorted
        for (int i = 0; i < size - 1; i++) {
            if (elements[i] > elements[i + 1]) {
                return OperationTrace.error("Binary Search", "Precondition Failed: Array must be sorted in ascending order for Binary Search.", toList(), BINARY_SEARCH_JAVA_CODE);
            }
        }

        int low = 0;
        int high = size - 1;
        int foundIndex = -1;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Initialize Binary Search")
                .description("Search range initialized: low = 0, high = " + high + " for target " + target + ".")
                .codeLine(1)
                .elements(toList())
                .pointer("low", low)
                .pointer("high", high)
                .timeComplexity("O(log N)")
                .build());

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midVal = elements[mid];

            if (midVal == target) {
                steps.add(TraceStep.builder()
                        .stepNumber(stepNum++)
                        .stepType(StepType.FOUND)
                        .title("Target Found at Mid")
                        .description("Mid index " + mid + " has value " + midVal + " == target " + target + ". Match found!")
                        .codeLine(4)
                        .elements(toList())
                        .highlightedIndices(mid)
                        .pointer("low", low)
                        .pointer("mid", mid)
                        .pointer("high", high)
                        .timeComplexity("O(log N)")
                        .build());
                foundIndex = mid;
                break;
            } else if (midVal < target) {
                steps.add(TraceStep.builder()
                        .stepNumber(stepNum++)
                        .stepType(StepType.COMPARE)
                        .title("Mid < Target -> Search Right Half")
                        .description("Mid value " + midVal + " < " + target + ". Discard left half, move low to " + (mid + 1) + ".")
                        .codeLine(5)
                        .elements(toList())
                        .highlightedIndices(mid)
                        .pointer("low", low)
                        .pointer("mid", mid)
                        .pointer("high", high)
                        .timeComplexity("O(log N)")
                        .build());
                low = mid + 1;
            } else {
                steps.add(TraceStep.builder()
                        .stepNumber(stepNum++)
                        .stepType(StepType.COMPARE)
                        .title("Mid > Target -> Search Left Half")
                        .description("Mid value " + midVal + " > " + target + ". Discard right half, move high to " + (mid - 1) + ".")
                        .codeLine(6)
                        .elements(toList())
                        .highlightedIndices(mid)
                        .pointer("low", low)
                        .pointer("mid", mid)
                        .pointer("high", high)
                        .timeComplexity("O(log N)")
                        .build());
                high = mid - 1;
            }
        }

        if (foundIndex != -1) {
            return new OperationTrace("Binary Search", true, "Found " + target + " at index " + foundIndex, BINARY_SEARCH_JAVA_CODE, steps);
        } else {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.NOT_FOUND)
                    .title("Target Not Found")
                    .description("Search range exhausted (low > high). Target " + target + " is not in the array.")
                    .codeLine(8)
                    .elements(toList())
                    .timeComplexity("O(log N)")
                    .build());
            return new OperationTrace("Binary Search", false, "Target " + target + " not found in array", BINARY_SEARCH_JAVA_CODE, steps);
        }
    }

    // ==========================================
    // REVERSE ARRAY
    // ==========================================
    public static final String REVERSE_JAVA_CODE = 
        "public void reverse() {\n" +
        "    int left = 0, right = size - 1;\n" +
        "    while (left < right) {\n" +
        "        int temp = arr[left];\n" +
        "        arr[left] = arr[right];\n" +
        "        arr[right] = temp;\n" +
        "        left++; right--;\n" +
        "    }\n" +
        "}";

    public OperationTrace reverse() {
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        if (size <= 1) {
            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.COMPLETED)
                    .title("Reverse Complete")
                    .description("Array has 0 or 1 element, already reversed.")
                    .elements(toList())
                    .build());
            return new OperationTrace("Reverse", true, "Array reversed", REVERSE_JAVA_CODE, steps);
        }

        int left = 0;
        int right = size - 1;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Start Array Reversal")
                .description("Two pointers placed: left = 0, right = " + right + ".")
                .codeLine(1)
                .elements(toList())
                .pointer("left", left)
                .pointer("right", right)
                .timeComplexity("O(N)")
                .build());

        while (left < right) {
            // Swap
            int temp = elements[left];
            elements[left] = elements[right];
            elements[right] = temp;

            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.SWAP)
                    .title("Swap Elements")
                    .description("Swapped elements at index " + left + " (" + elements[right] + ") and index " + right + " (" + elements[left] + ").")
                    .codeLine(3)
                    .elements(toList())
                    .highlightedIndices(left, right)
                    .pointer("left", left)
                    .pointer("right", right)
                    .timeComplexity("O(N)")
                    .build());

            left++;
            right--;
        }

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Reversal Complete")
                .description("All pairs swapped. Array is completely reversed.")
                .codeLine(6)
                .elements(toList())
                .timeComplexity("O(N)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Reverse", true, "Array reversed successfully", REVERSE_JAVA_CODE, steps);
    }

    private List<Integer> copyList(int[] arr, int count) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(arr[i]);
        }
        return list;
    }
}
