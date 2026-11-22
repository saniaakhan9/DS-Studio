package com.dsstudio.core.trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the entire trace of an operation with all intermediate snapshots.
 */
public class OperationTrace {
    private final String operationName;
    private final boolean success;
    private final String summary;
    private final String javaCode;
    private final List<TraceStep> steps;

    public OperationTrace(String operationName, boolean success, String summary, String javaCode, List<TraceStep> steps) {
        this.operationName = operationName;
        this.success = success;
        this.summary = summary;
        this.javaCode = javaCode != null ? javaCode : "";
        this.steps = Collections.unmodifiableList(new ArrayList<>(steps));
    }

    public String getOperationName() { return operationName; }
    public boolean isSuccess() { return success; }
    public String getSummary() { return summary; }
    public String getJavaCode() { return javaCode; }
    public List<TraceStep> getSteps() { return steps; }
    public int getStepCount() { return steps.size(); }

    public TraceStep getStep(int index) {
        if (index >= 0 && index < steps.size()) {
            return steps.get(index);
        }
        return null;
    }

    public static OperationTrace error(String operationName, String message, List<Integer> currentElements, String javaCode) {
        List<TraceStep> steps = new ArrayList<>();
        steps.add(TraceStep.builder()
                .stepNumber(1)
                .stepType(StepType.ERROR)
                .title("Operation Failed")
                .description(message)
                .elements(currentElements)
                .error(true)
                .build());
        return new OperationTrace(operationName, false, message, javaCode, steps);
    }
}
