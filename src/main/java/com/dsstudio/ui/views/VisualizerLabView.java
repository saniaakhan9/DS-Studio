package com.dsstudio.ui.views;

import com.dsstudio.core.structures.linear.*;
import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.StepType;
import com.dsstudio.core.trace.TraceStep;
import com.dsstudio.service.GamificationService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.List;

public class VisualizerLabView extends VBox {
    // Selectors & inputs
    private final ComboBox<String> structureCombo = new ComboBox<>();
    private final ComboBox<String> operationCombo = new ComboBox<>();
    private final TextField valueField = new TextField();
    private final TextField indexField = new TextField();
    private final Button executeBtn = new Button("⚡ Execute");

    // Playback controls
    private final Button prevBtn = new Button("◀ Prev");
    private final Button nextBtn = new Button("Next ▶");
    private final Button playBtn = new Button("▶ Play");
    private final Button resetBtn = new Button("🔄 Reset");
    private final ComboBox<String> speedCombo = new ComboBox<>();

    // Status & Explanation
    private final Label stepCounterLabel = new Label("Step 0 / 0");
    private final Label stepTitleLabel = new Label("Ready");
    private final Label stepDescLabel = new Label("Select an operation and press Execute to generate step-by-step animation trace.");
    private final Label timeComplexityBadge = new Label("Time: O(1)");
    private final Label spaceComplexityBadge = new Label("Space: O(1)");

    // Visual display area
    private final HBox canvasContainer = new HBox(12);
    private final TextArea codeArea = new TextArea();

    // Active structure instances
    private ArrayStructure arrayStructure = new ArrayStructure(8, new int[]{12, 28, 45, 67, 89});
    private StackStructure stackStructure = new StackStructure(6, new int[]{10, 20, 30});
    private QueueStructure queueStructure = new QueueStructure(6, new int[]{15, 30, 45});
    private CircularQueueStructure circularQueue = new CircularQueueStructure(5, new int[]{10, 20, 30});
    private SinglyLinkedListStructure singlyList = new SinglyLinkedListStructure(new int[]{10, 25, 40, 55});
    private DoublyLinkedListStructure doublyList = new DoublyLinkedListStructure(new int[]{10, 20, 30});
    private CircularLinkedListStructure circularList = new CircularLinkedListStructure(new int[]{10, 20, 30});

    // Trace playback state
    private OperationTrace currentTrace;
    private int currentStepIndex = 0;
    private Timeline timeline;
    private boolean isPlaying = false;
    private double speedFactor = 1.0;

    public VisualizerLabView() {
        this.setSpacing(16);
        this.setPadding(new Insets(20));
        this.setFillWidth(true);

        setupControls();
        setupVisualArea();
        setupTimeline();

        // Initial render
        renderInitialStructureState();
    }

    private void setupControls() {
        // Toolbar Card
        VBox toolCard = new VBox(12);
        toolCard.getStyleClass().add("card");

        HBox inputsRow = new HBox(14);
        inputsRow.setAlignment(Pos.CENTER_LEFT);

        structureCombo.getItems().addAll(
                "Array",
                "Stack",
                "Queue",
                "Circular Queue",
                "Singly Linked List",
                "Doubly Linked List",
                "Circular Linked List"
        );
        structureCombo.setValue("Array");
        structureCombo.setOnAction(e -> onStructureChanged());

        updateOperationsForStructure("Array");
        operationCombo.setOnAction(e -> onOperationChanged());

        valueField.setPromptText("Value");
        valueField.setPrefWidth(80);
        valueField.setText("99");

        indexField.setPromptText("Index");
        indexField.setPrefWidth(70);
        indexField.setText("2");

        executeBtn.getStyleClass().add("btn-primary");
        executeBtn.setOnAction(e -> handleExecute());

        inputsRow.getChildren().addAll(
                new Label("Data Structure:"), structureCombo,
                new Label("Operation:"), operationCombo,
                new Label("Val:"), valueField,
                new Label("Idx:"), indexField,
                executeBtn
        );

        // Playback row
        HBox playbackRow = new HBox(12);
        playbackRow.setAlignment(Pos.CENTER_LEFT);

        prevBtn.getStyleClass().add("btn-secondary");
        prevBtn.setOnAction(e -> stepPrev());

        nextBtn.getStyleClass().add("btn-secondary");
        nextBtn.setOnAction(e -> stepNext());

        playBtn.getStyleClass().add("btn-success");
        playBtn.setOnAction(e -> togglePlay());

        resetBtn.getStyleClass().add("btn-secondary");
        resetBtn.setOnAction(e -> resetVisualization());

        speedCombo.getItems().addAll("0.5x Speed", "1.0x Speed", "2.0x Speed");
        speedCombo.setValue("1.0x Speed");
        speedCombo.setOnAction(e -> {
            String s = speedCombo.getValue();
            if (s.startsWith("0.5")) speedFactor = 0.5;
            else if (s.startsWith("2.0")) speedFactor = 2.0;
            else speedFactor = 1.0;
            setupTimeline();
        });

        stepCounterLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #38BDF8; -fx-font-size: 13px;");
        timeComplexityBadge.getStyleClass().add("badge-cyan");
        spaceComplexityBadge.getStyleClass().add("badge-indigo");

        playbackRow.getChildren().addAll(
                prevBtn, playBtn, nextBtn, resetBtn,
                new Label("Speed:"), speedCombo,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                stepCounterLabel,
                timeComplexityBadge,
                spaceComplexityBadge
        );

        toolCard.getChildren().addAll(inputsRow, playbackRow);
        this.getChildren().add(toolCard);
    }

    private void setupVisualArea() {
        SplitPane splitPane = new SplitPane();

        // Left Visualizer Canvas
        VBox visualContainer = new VBox(12);
        visualContainer.getStyleClass().add("card");
        visualContainer.setPadding(new Insets(16));

        HBox bannerBox = new HBox(8);
        bannerBox.setAlignment(Pos.CENTER_LEFT);
        stepTitleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
        bannerBox.getChildren().addAll(stepTitleLabel);

        stepDescLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94A3B8;");
        stepDescLabel.setWrapText(true);

        canvasContainer.setAlignment(Pos.CENTER);
        canvasContainer.getStyleClass().add("visualizer-canvas-container");
        canvasContainer.setMinHeight(280);
        VBox.setVgrow(canvasContainer, Priority.ALWAYS);

        visualContainer.getChildren().addAll(bannerBox, stepDescLabel, canvasContainer);
        VBox.setVgrow(visualContainer, Priority.ALWAYS);

        // Right Java Code Panel
        VBox codeContainer = new VBox(8);
        codeContainer.getStyleClass().add("card");
        codeContainer.setPrefWidth(400);
        codeContainer.setPadding(new Insets(16));

        Label codeTitle = new Label("💻 Real Java Implementation");
        codeTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #E2E8F0;");

        codeArea.setEditable(false);
        codeArea.getStyleClass().add("code-panel");
        codeArea.setWrapText(false);
        VBox.setVgrow(codeArea, Priority.ALWAYS);

        codeContainer.getChildren().addAll(codeTitle, codeArea);
        VBox.setVgrow(codeContainer, Priority.ALWAYS);

        splitPane.getItems().addAll(visualContainer, codeContainer);
        splitPane.setDividerPositions(0.65);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        this.getChildren().add(splitPane);
    }

    private void setupTimeline() {
        if (timeline != null) {
            timeline.stop();
        }
        double interval = 1200.0 / speedFactor;
        timeline = new Timeline(new KeyFrame(Duration.millis(interval), e -> {
            if (currentTrace != null && currentStepIndex < currentTrace.getStepCount() - 1) {
                stepNext();
            } else {
                pause();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void onStructureChanged() {
        String struct = structureCombo.getValue();
        updateOperationsForStructure(struct);
        resetVisualization();
    }

    private void updateOperationsForStructure(String struct) {
        operationCombo.getItems().clear();
        switch (struct) {
            case "Array" -> {
                operationCombo.getItems().addAll("Insert", "Delete", "Linear Search", "Binary Search", "Reverse");
                operationCombo.setValue("Insert");
            }
            case "Stack" -> {
                operationCombo.getItems().addAll("Push", "Pop", "Peek");
                operationCombo.setValue("Push");
            }
            case "Queue" -> {
                operationCombo.getItems().addAll("Enqueue", "Dequeue");
                operationCombo.setValue("Enqueue");
            }
            case "Circular Queue" -> {
                operationCombo.getItems().addAll("Enqueue", "Dequeue");
                operationCombo.setValue("Enqueue");
            }
            case "Singly Linked List" -> {
                operationCombo.getItems().addAll("Insert Head", "Insert Tail", "Delete Head", "Search");
                operationCombo.setValue("Insert Head");
            }
            case "Doubly Linked List" -> {
                operationCombo.getItems().addAll("Insert Head", "Insert Tail");
                operationCombo.setValue("Insert Head");
            }
            case "Circular Linked List" -> {
                operationCombo.getItems().addAll("Insert Head");
                operationCombo.setValue("Insert Head");
            }
        }
        onOperationChanged();
    }

    private void onOperationChanged() {
        String op = operationCombo.getValue();
        if (op == null) return;

        boolean needVal = op.contains("Insert") || op.equals("Push") || op.equals("Enqueue") || op.contains("Search");
        boolean needIdx = op.equals("Insert") || op.equals("Delete");

        valueField.setDisable(!needVal);
        indexField.setDisable(!needIdx);
    }

    private void handleExecute() {
        String struct = structureCombo.getValue();
        String op = operationCombo.getValue();
        if (op == null) return;

        int val = 0;
        int idx = 0;
        try {
            if (!valueField.isDisable()) val = Integer.parseInt(valueField.getText().trim());
            if (!indexField.isDisable()) idx = Integer.parseInt(indexField.getText().trim());
        } catch (NumberFormatException e) {
            stepDescLabel.setText("⚠️ Invalid number format! Please enter valid integers.");
            return;
        }

        switch (struct) {
            case "Array" -> {
                switch (op) {
                    case "Insert" -> currentTrace = arrayStructure.insert(idx, val);
                    case "Delete" -> currentTrace = arrayStructure.delete(idx);
                    case "Linear Search" -> currentTrace = arrayStructure.linearSearch(val);
                    case "Binary Search" -> currentTrace = arrayStructure.binarySearch(val);
                    case "Reverse" -> currentTrace = arrayStructure.reverse();
                }
            }
            case "Stack" -> {
                switch (op) {
                    case "Push" -> currentTrace = stackStructure.push(val);
                    case "Pop" -> currentTrace = stackStructure.pop();
                    case "Peek" -> currentTrace = stackStructure.peek();
                }
            }
            case "Queue" -> {
                switch (op) {
                    case "Enqueue" -> currentTrace = queueStructure.enqueue(val);
                    case "Dequeue" -> currentTrace = queueStructure.dequeue();
                }
            }
            case "Circular Queue" -> {
                switch (op) {
                    case "Enqueue" -> currentTrace = circularQueue.enqueue(val);
                    case "Dequeue" -> currentTrace = circularQueue.dequeue();
                }
            }
            case "Singly Linked List" -> {
                switch (op) {
                    case "Insert Head" -> currentTrace = singlyList.insertHead(val);
                    case "Insert Tail" -> currentTrace = singlyList.insertTail(val);
                    case "Delete Head" -> currentTrace = singlyList.deleteHead();
                    case "Search" -> currentTrace = singlyList.search(val);
                }
            }
            case "Doubly Linked List" -> {
                switch (op) {
                    case "Insert Head" -> currentTrace = doublyList.insertHead(val);
                    case "Insert Tail" -> currentTrace = doublyList.insertTail(val);
                }
            }
            case "Circular Linked List" -> {
                if ("Insert Head".equals(op)) {
                    currentTrace = circularList.insertHead(val);
                }
            }
        }

        if (currentTrace != null && currentTrace.getStepCount() > 0) {
            codeArea.setText(currentTrace.getJavaCode());
            currentStepIndex = 0;
            renderTraceStep(currentTrace.getStep(0));
            // Award XP for executing interactive operation
            GamificationService.addXp(5);
        }
    }

    private void renderTraceStep(TraceStep step) {
        if (step == null) return;

        stepCounterLabel.setText("Step " + step.getStepNumber() + " / " + (currentTrace != null ? currentTrace.getStepCount() : 1));
        stepTitleLabel.setText((step.isError() ? "❌ " : "🔹 ") + step.getTitle());
        stepDescLabel.setText(step.getDescription());
        timeComplexityBadge.setText("Time: " + step.getTimeComplexity());
        spaceComplexityBadge.setText("Space: " + step.getSpaceComplexity());

        canvasContainer.getChildren().clear();
        String struct = structureCombo.getValue();

        List<Integer> elements = step.getElements();
        List<Integer> highlighted = step.getHighlightedIndices();

        switch (struct) {
            case "Array" -> renderArrayElements(elements, highlighted, step);
            case "Stack" -> renderStackElements(elements, highlighted, step);
            case "Queue" -> renderQueueElements(elements, highlighted, step);
            case "Circular Queue" -> renderCircularQueueElements(elements, highlighted, step);
            case "Singly Linked List", "Doubly Linked List", "Circular Linked List" ->
                    renderLinkedListNodes(elements, highlighted, step, struct);
        }
    }

    private void renderArrayElements(List<Integer> elements, List<Integer> highlighted, TraceStep step) {
        for (int i = 0; i < elements.size(); i++) {
            Integer val = elements.get(i);
            VBox cell = new VBox(5);
            cell.setAlignment(Pos.CENTER);

            // Pointer label if any
            String pointerLabel = getPointerAt(step, i);
            Label pLabel = new Label(pointerLabel.isEmpty() ? " " : pointerLabel);
            pLabel.getStyleClass().add("viz-pointer-label");

            StackPane box = new StackPane();
            box.setPrefSize(58, 58);
            boolean isHighlighted = highlighted.contains(i);
            box.getStyleClass().add(isHighlighted ? "viz-node-highlight" : "viz-node-box");

            Label valLabel = new Label(val != null ? String.valueOf(val) : "-");
            valLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
            box.getChildren().add(valLabel);

            Label idxLabel = new Label("[" + i + "]");
            idxLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: #64748B;");

            cell.getChildren().addAll(pLabel, box, idxLabel);
            canvasContainer.getChildren().add(cell);
        }
    }

    private void renderStackElements(List<Integer> elements, List<Integer> highlighted, TraceStep step) {
        VBox stackCol = new VBox(8);
        stackCol.setAlignment(Pos.BOTTOM_CENTER);

        for (int i = elements.size() - 1; i >= 0; i--) {
            int val = elements.get(i);
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER);

            Label ptrLabel = new Label(i == elements.size() - 1 ? "TOP ➜" : "     ");
            ptrLabel.setStyle("-fx-font-weight: 800; -fx-text-fill: #F43F5E; -fx-font-size: 12px;");

            StackPane box = new StackPane();
            box.setPrefSize(110, 38);
            boolean isHighlighted = highlighted.contains(i);
            if (isHighlighted) {
                box.setStyle("-fx-background-color: #4C0519; -fx-border-color: #FB7185; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(251, 113, 133, 0.4), 8, 0, 0, 0);");
            } else {
                box.getStyleClass().add("viz-node-box");
            }

            Label valLabel = new Label(String.valueOf(val));
            valLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
            box.getChildren().add(valLabel);

            Label idxLabel = new Label("idx " + i);
            idxLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: #64748B;");

            row.getChildren().addAll(ptrLabel, box, idxLabel);
            stackCol.getChildren().add(row);
        }
        canvasContainer.getChildren().add(stackCol);
    }

    private void renderQueueElements(List<Integer> elements, List<Integer> highlighted, TraceStep step) {
        renderArrayElements(elements, highlighted, step);
    }

    private void renderCircularQueueElements(List<Integer> elements, List<Integer> highlighted, TraceStep step) {
        renderArrayElements(elements, highlighted, step);
    }

    private void renderLinkedListNodes(List<Integer> elements, List<Integer> highlighted, TraceStep step, String type) {
        if (elements.isEmpty()) {
            Label emptyLbl = new Label("HEAD ➜ NULL (Empty List)");
            emptyLbl.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748B; -fx-font-weight: 700;");
            canvasContainer.getChildren().add(emptyLbl);
            return;
        }

        Label headMarker = new Label("HEAD ➜");
        headMarker.setStyle("-fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: #38BDF8;");
        canvasContainer.getChildren().add(headMarker);

        for (int i = 0; i < elements.size(); i++) {
            int val = elements.get(i);
            VBox nodeBox = new VBox(4);
            nodeBox.setAlignment(Pos.CENTER);

            HBox cell = new HBox();
            boolean isHighlighted = highlighted.contains(i);
            if (isHighlighted) {
                cell.setStyle("-fx-background-color: #064E3B; -fx-border-color: #34D399; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(52, 211, 153, 0.4), 8, 0, 0, 0);");
            } else {
                cell.setStyle("-fx-background-color: #1E293B; -fx-border-color: #6366F1; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
            }

            Label dataPart = new Label(" " + val + " ");
            dataPart.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC; -fx-padding: 8 10;");

            Label ptrPart = new Label(" • ");
            ptrPart.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-background-color: #0F172A; -fx-padding: 8 10; -fx-background-radius: 0 6 6 0;");

            cell.getChildren().addAll(dataPart, ptrPart);

            Label idxLabel = new Label("Node " + i);
            idxLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: 600; -fx-text-fill: #64748B;");

            nodeBox.getChildren().addAll(cell, idxLabel);
            canvasContainer.getChildren().add(nodeBox);

            if (i < elements.size() - 1) {
                Label arrow = new Label(type.equals("Doubly Linked List") ? " ⇄ " : " ➜ ");
                arrow.setStyle("-fx-font-size: 16px; -fx-font-weight: 900; -fx-text-fill: #818CF8;");
                canvasContainer.getChildren().add(arrow);
            }
        }

        Label tailMarker = new Label(type.equals("Circular Linked List") ? " ➜ [HEAD]" : " ➜ NULL");
        tailMarker.setStyle("-fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: #EC4899;");
        canvasContainer.getChildren().add(tailMarker);
    }

    private String getPointerAt(TraceStep step, int idx) {
        StringBuilder sb = new StringBuilder();
        step.getPointerPositions().forEach((name, pos) -> {
            if (pos == idx) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(name);
            }
        });
        return sb.toString();
    }

    private void stepNext() {
        if (currentTrace != null && currentStepIndex < currentTrace.getStepCount() - 1) {
            currentStepIndex++;
            renderTraceStep(currentTrace.getStep(currentStepIndex));
        }
    }

    private void stepPrev() {
        if (currentTrace != null && currentStepIndex > 0) {
            currentStepIndex--;
            renderTraceStep(currentTrace.getStep(currentStepIndex));
        }
    }

    private void togglePlay() {
        if (isPlaying) {
            pause();
        } else {
            play();
        }
    }

    private void play() {
        if (currentTrace == null) return;
        if (currentStepIndex >= currentTrace.getStepCount() - 1) {
            currentStepIndex = 0;
        }
        isPlaying = true;
        playBtn.setText("⏸ Pause");
        playBtn.getStyleClass().remove("btn-success");
        playBtn.getStyleClass().add("btn-danger");
        timeline.play();
    }

    private void pause() {
        isPlaying = false;
        playBtn.setText("▶ Play");
        playBtn.getStyleClass().remove("btn-danger");
        playBtn.getStyleClass().add("btn-success");
        timeline.stop();
    }

    private void resetVisualization() {
        pause();
        currentTrace = null;
        currentStepIndex = 0;
        stepCounterLabel.setText("Step 0 / 0");
        stepTitleLabel.setText("Ready");
        stepDescLabel.setText("Visualization reset. Enter values and click Execute.");
        renderInitialStructureState();
    }

    private void renderInitialStructureState() {
        String struct = structureCombo.getValue();
        List<Integer> sample = switch (struct) {
            case "Array" -> arrayStructure.toList();
            case "Stack" -> stackStructure.toList();
            case "Queue" -> queueStructure.toList();
            case "Circular Queue" -> circularQueue.toSlotList();
            case "Singly Linked List" -> singlyList.toList();
            case "Doubly Linked List" -> doublyList.toList();
            default -> circularList.toList();
        };

        TraceStep initStep = TraceStep.builder()
                .stepNumber(0)
                .stepType(StepType.INITIAL)
                .title("Current State")
                .description("Initial visual state of " + struct + ".")
                .elements(sample)
                .build();
        renderTraceStep(initStep);
    }
}
