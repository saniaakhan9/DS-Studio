package com.dsstudio.core.trace;

import java.util.*;

/**
 * Immutable snapshot of the data structure's state at a particular micro-step.
 * Decouples algorithm execution from JavaFX rendering.
 */
public class TraceStep {
    private final int stepNumber;
    private final StepType stepType;
    private final String title;
    private final String description;
    private final int codeLineToHighlight;
    private final List<Integer> elements;
    private final List<Integer> secondaryElements;
    private final List<Integer> highlightedIndices;
    private final List<Integer> secondaryHighlightedIndices;
    private final Map<String, Integer> pointerPositions;
    private final String timeComplexity;
    private final String spaceComplexity;
    private final boolean isError;

    private TraceStep(Builder builder) {
        this.stepNumber = builder.stepNumber;
        this.stepType = builder.stepType != null ? builder.stepType : StepType.HIGHLIGHT;
        this.title = builder.title != null ? builder.title : "";
        this.description = builder.description != null ? builder.description : "";
        this.codeLineToHighlight = builder.codeLineToHighlight;
        this.elements = Collections.unmodifiableList(new ArrayList<>(builder.elements));
        this.secondaryElements = Collections.unmodifiableList(new ArrayList<>(builder.secondaryElements));
        this.highlightedIndices = Collections.unmodifiableList(new ArrayList<>(builder.highlightedIndices));
        this.secondaryHighlightedIndices = Collections.unmodifiableList(new ArrayList<>(builder.secondaryHighlightedIndices));
        this.pointerPositions = Collections.unmodifiableMap(new LinkedHashMap<>(builder.pointerPositions));
        this.timeComplexity = builder.timeComplexity != null ? builder.timeComplexity : "O(1)";
        this.spaceComplexity = builder.spaceComplexity != null ? builder.spaceComplexity : "O(1)";
        this.isError = builder.isError;
    }

    public int getStepNumber() { return stepNumber; }
    public StepType getStepType() { return stepType; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getCodeLineToHighlight() { return codeLineToHighlight; }
    public List<Integer> getElements() { return elements; }
    public List<Integer> getSecondaryElements() { return secondaryElements; }
    public List<Integer> getHighlightedIndices() { return highlightedIndices; }
    public List<Integer> getSecondaryHighlightedIndices() { return secondaryHighlightedIndices; }
    public Map<String, Integer> getPointerPositions() { return pointerPositions; }
    public String getTimeComplexity() { return timeComplexity; }
    public String getSpaceComplexity() { return spaceComplexity; }
    public boolean isError() { return isError; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int stepNumber;
        private StepType stepType = StepType.HIGHLIGHT;
        private String title = "";
        private String description = "";
        private int codeLineToHighlight = -1;
        private List<Integer> elements = new ArrayList<>();
        private List<Integer> secondaryElements = new ArrayList<>();
        private List<Integer> highlightedIndices = new ArrayList<>();
        private List<Integer> secondaryHighlightedIndices = new ArrayList<>();
        private Map<String, Integer> pointerPositions = new LinkedHashMap<>();
        private String timeComplexity = "O(1)";
        private String spaceComplexity = "O(1)";
        private boolean isError = false;

        public Builder stepNumber(int stepNumber) { this.stepNumber = stepNumber; return this; }
        public Builder stepType(StepType stepType) { this.stepType = stepType; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder codeLine(int codeLine) { this.codeLineToHighlight = codeLine; return this; }
        public Builder elements(List<Integer> elements) {
            this.elements = elements != null ? new ArrayList<>(elements) : new ArrayList<>();
            return this;
        }
        public Builder secondaryElements(List<Integer> secondary) {
            this.secondaryElements = secondary != null ? new ArrayList<>(secondary) : new ArrayList<>();
            return this;
        }
        public Builder highlightedIndices(Integer... indices) {
            this.highlightedIndices = Arrays.asList(indices);
            return this;
        }
        public Builder highlightedIndices(List<Integer> indices) {
            this.highlightedIndices = indices != null ? new ArrayList<>(indices) : new ArrayList<>();
            return this;
        }
        public Builder secondaryHighlightedIndices(Integer... indices) {
            this.secondaryHighlightedIndices = Arrays.asList(indices);
            return this;
        }
        public Builder pointer(String name, int index) {
            this.pointerPositions.put(name, index);
            return this;
        }
        public Builder pointers(Map<String, Integer> pointers) {
            if (pointers != null) this.pointerPositions.putAll(pointers);
            return this;
        }
        public Builder timeComplexity(String tc) { this.timeComplexity = tc; return this; }
        public Builder spaceComplexity(String sc) { this.spaceComplexity = sc; return this; }
        public Builder error(boolean isError) { this.isError = isError; return this; }

        public TraceStep build() {
            return new TraceStep(this);
        }
    }
}
