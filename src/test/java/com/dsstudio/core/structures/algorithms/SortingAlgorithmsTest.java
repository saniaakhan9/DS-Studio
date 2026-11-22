package com.dsstudio.core.structures.algorithms;

import com.dsstudio.core.trace.OperationTrace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortingAlgorithmsTest {

    @Test
    @DisplayName("Bubble Sort: Correct Sorting and Trace Generation")
    void testBubbleSort() {
        int[] input = {5, 2, 8, 1, 9};
        OperationTrace trace = SortingAlgorithms.bubbleSort(input);
        assertTrue(trace.isSuccess());
        assertEquals(List.of(1, 2, 5, 8, 9), trace.getSteps().get(trace.getStepCount() - 1).getElements());
    }

    @Test
    @DisplayName("Selection Sort: Correct Sorting and Trace Generation")
    void testSelectionSort() {
        int[] input = {64, 25, 12, 22, 11};
        OperationTrace trace = SortingAlgorithms.selectionSort(input);
        assertTrue(trace.isSuccess());
        assertEquals(List.of(11, 12, 22, 25, 64), trace.getSteps().get(trace.getStepCount() - 1).getElements());
    }

    @Test
    @DisplayName("Insertion Sort: Correct Sorting and Trace Generation")
    void testInsertionSort() {
        int[] input = {12, 11, 13, 5, 6};
        OperationTrace trace = SortingAlgorithms.insertionSort(input);
        assertTrue(trace.isSuccess());
        assertEquals(List.of(5, 6, 11, 12, 13), trace.getSteps().get(trace.getStepCount() - 1).getElements());
    }
}
