package com.dsstudio.core.structures.algorithms;

import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortingAlgorithms {

    // ==========================================
    // BUBBLE SORT
    // ==========================================
    public static final String BUBBLE_SORT_JAVA = 
        "public static void bubbleSort(int[] arr) {\n" +
        "    int n = arr.length;\n" +
        "    for (int i = 0; i < n - 1; i++) {\n" +
        "        boolean swapped = false;\n" +
        "        for (int j = 0; j < n - i - 1; j++) {\n" +
        "            if (arr[j] > arr[j + 1]) {\n" +
        "                int temp = arr[j];\n" +
        "                arr[j] = arr[j + 1];\n" +
        "                arr[j + 1] = temp;\n" +
        "                swapped = true;\n" +
        "            }\n" +
        "        }\n" +
        "        if (!swapped) break;\n" +
        "    }\n" +
        "}";

    public static OperationTrace bubbleSort(int[] input) {
        int[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Start Bubble Sort")
                .description("Initial array with " + n + " elements. Adjacent pairs will be compared and swapped.")
                .codeLine(1)
                .elements(toList(arr))
                .timeComplexity("O(N²)")
                .spaceComplexity("O(1)")
                .build());

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                boolean needSwap = arr[j] > arr[j + 1];
                steps.add(TraceStep.builder()
                        .stepNumber(stepNum++)
                        .stepType(StepType.COMPARE)
                        .title("Compare Pair")
                        .description("Comparing arr[" + j + "] (" + arr[j] + ") and arr[" + (j + 1) + "] (" + arr[j + 1] + ")" +
                                (needSwap ? " -> " + arr[j] + " > " + arr[j + 1] + ", SWAP required!" : " -> in correct order."))
                        .codeLine(6)
                        .elements(toList(arr))
                        .highlightedIndices(j, j + 1)
                        .pointer("j", j)
                        .timeComplexity("O(N²)")
                        .build());

                if (needSwap) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;

                    steps.add(TraceStep.builder()
                            .stepNumber(stepNum++)
                            .stepType(StepType.SWAP)
                            .title("Swap Elements")
                            .description("Swapped arr[" + j + "] and arr[" + (j + 1) + "]. New order: " + arr[j] + ", " + arr[j + 1] + ".")
                            .codeLine(8)
                            .elements(toList(arr))
                            .highlightedIndices(j, j + 1)
                            .timeComplexity("O(N²)")
                            .build());
                }
            }
            if (!swapped) {
                steps.add(TraceStep.builder()
                        .stepNumber(stepNum++)
                        .stepType(StepType.COMPLETED)
                        .title("Early Termination")
                        .description("Pass " + (i + 1) + " finished with 0 swaps! Array is already fully sorted.")
                        .codeLine(13)
                        .elements(toList(arr))
                        .timeComplexity("O(N) Best Case")
                        .build());
                break;
            }
        }

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Bubble Sort Completed")
                .description("All passes complete. Array is now fully sorted in ascending order.")
                .codeLine(15)
                .elements(toList(arr))
                .timeComplexity("O(N²) Worst/Avg, O(N) Best")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Bubble Sort", true, "Sorted " + n + " elements", BUBBLE_SORT_JAVA, steps);
    }

    // ==========================================
    // SELECTION SORT
    // ==========================================
    public static final String SELECTION_SORT_JAVA = 
        "public static void selectionSort(int[] arr) {\n" +
        "    int n = arr.length;\n" +
        "    for (int i = 0; i < n - 1; i++) {\n" +
        "        int minIdx = i;\n" +
        "        for (int j = i + 1; j < n; j++) {\n" +
        "            if (arr[j] < arr[minIdx]) minIdx = j;\n" +
        "        }\n" +
        "        int temp = arr[minIdx];\n" +
        "        arr[minIdx] = arr[i];\n" +
        "        arr[i] = temp;\n" +
        "    }\n" +
        "}";

    public static OperationTrace selectionSort(int[] input) {
        int[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Start Selection Sort")
                .description("Array with " + n + " elements. Repeatedly find the minimum element in unsorted portion.")
                .codeLine(1)
                .elements(toList(arr))
                .timeComplexity("O(N²)")
                .spaceComplexity("O(1)")
                .build());

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }

            if (minIdx != i) {
                int temp = arr[minIdx];
                arr[minIdx] = arr[i];
                arr[i] = temp;
            }

            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.SWAP)
                    .title("Place Minimum Element")
                    .description("Found minimum value " + arr[i] + " and placed at index " + i + ".")
                    .codeLine(9)
                    .elements(toList(arr))
                    .highlightedIndices(i)
                    .pointer("sortedUpTo", i)
                    .timeComplexity("O(N²)")
                    .build());
        }

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Selection Sort Completed")
                .description("Array is now completely sorted.")
                .codeLine(12)
                .elements(toList(arr))
                .timeComplexity("O(N²)")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Selection Sort", true, "Sorted " + n + " elements", SELECTION_SORT_JAVA, steps);
    }

    // ==========================================
    // INSERTION SORT
    // ==========================================
    public static final String INSERTION_SORT_JAVA = 
        "public static void insertionSort(int[] arr) {\n" +
        "    int n = arr.length;\n" +
        "    for (int i = 1; i < n; i++) {\n" +
        "        int key = arr[i];\n" +
        "        int j = i - 1;\n" +
        "        while (j >= 0 && arr[j] > key) {\n" +
        "            arr[j + 1] = arr[j];\n" +
        "            j--;\n" +
        "        }\n" +
        "        arr[j + 1] = key;\n" +
        "    }\n" +
        "}";

    public static OperationTrace insertionSort(int[] input) {
        int[] arr = Arrays.copyOf(input, input.length);
        int n = arr.length;
        List<TraceStep> steps = new ArrayList<>();
        int stepNum = 1;

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.INITIAL)
                .title("Start Insertion Sort")
                .description("Initial array with " + n + " elements. Elements are inserted into sorted subarray one by one.")
                .codeLine(1)
                .elements(toList(arr))
                .timeComplexity("O(N²)")
                .spaceComplexity("O(1)")
                .build());

        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;

            steps.add(TraceStep.builder()
                    .stepNumber(stepNum++)
                    .stepType(StepType.INSERT)
                    .title("Inserted Key Element")
                    .description("Inserted key " + key + " into sorted subarray at position " + (j + 1) + ".")
                    .codeLine(10)
                    .elements(toList(arr))
                    .highlightedIndices(j + 1)
                    .pointer("key", j + 1)
                    .timeComplexity("O(N²)")
                    .build());
        }

        steps.add(TraceStep.builder()
                .stepNumber(stepNum++)
                .stepType(StepType.COMPLETED)
                .title("Insertion Sort Complete")
                .description("All elements inserted into proper positions.")
                .codeLine(12)
                .elements(toList(arr))
                .timeComplexity("O(N²) Worst / O(N) Best")
                .spaceComplexity("O(1)")
                .build());

        return new OperationTrace("Insertion Sort", true, "Sorted " + n + " elements", INSERTION_SORT_JAVA, steps);
    }

    private static List<Integer> toList(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int v : arr) list.add(v);
        return list;
    }
}
