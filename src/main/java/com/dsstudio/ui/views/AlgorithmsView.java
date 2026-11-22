package com.dsstudio.ui.views;

import com.dsstudio.core.structures.algorithms.SortingAlgorithms;
import com.dsstudio.core.trace.OperationTrace;
import com.dsstudio.core.trace.TraceStep;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.List;

public class AlgorithmsView extends VBox {
    private final ComboBox<String> algoCombo = new ComboBox<>();
    private final TextField inputField = new TextField("45, 12, 89, 23, 7, 56");
    private final Button runBtn = new Button("⚡ Sort Array");

    private final Button prevBtn = new Button("◀ Prev");
    private final Button nextBtn = new Button("Next ▶");
    private final Button playBtn = new Button("▶ Play");
    private final Button resetBtn = new Button("🔄 Reset");

    private final Label stepLabel = new Label("Step 0 / 0");
    private final Label descLabel = new Label("Select a sorting algorithm and press Sort.");
    private final Label complexityBadge = new Label("Time: O(N²)");

    private final HBox barContainer = new HBox(12);
    private final TextArea codeArea = new TextArea();

    private OperationTrace currentTrace;
    private int stepIndex = 0;
    private Timeline timeline;

    public AlgorithmsView() {
        this.setSpacing(16);
        this.setPadding(new Insets(20));
        this.setFillWidth(true);

        // Control Card
        VBox toolCard = new VBox(12);
        toolCard.getStyleClass().add("card");

        HBox row1 = new HBox(14);
        row1.setAlignment(Pos.CENTER_LEFT);

        algoCombo.getItems().addAll("Bubble Sort", "Selection Sort", "Insertion Sort");
        algoCombo.setValue("Bubble Sort");

        inputField.setPrefWidth(240);

        runBtn.getStyleClass().add("btn-primary");
        runBtn.setOnAction(e -> executeSort());

        row1.getChildren().addAll(
                new Label("Algorithm:"), algoCombo,
                new Label("Array Input:"), inputField,
                runBtn
        );

        HBox row2 = new HBox(12);
        row2.setAlignment(Pos.CENTER_LEFT);

        prevBtn.getStyleClass().add("btn-secondary");
        prevBtn.setOnAction(e -> stepPrev());

        nextBtn.getStyleClass().add("btn-secondary");
        nextBtn.setOnAction(e -> stepNext());

        playBtn.getStyleClass().add("btn-success");
        playBtn.setOnAction(e -> togglePlay());

        resetBtn.getStyleClass().add("btn-secondary");
        resetBtn.setOnAction(e -> reset());

        complexityBadge.getStyleClass().add("badge-cyan");
        stepLabel.setStyle("-fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-font-size: 13px;");

        row2.getChildren().addAll(prevBtn, playBtn, nextBtn, resetBtn, stepLabel, complexityBadge);
        toolCard.getChildren().addAll(row1, row2);

        // Display Split
        SplitPane splitPane = new SplitPane();

        VBox visualBox = new VBox(12);
        visualBox.getStyleClass().add("card");
        visualBox.setPadding(new Insets(16));

        descLabel.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #CBD5E1; -fx-font-weight: 600;");
        descLabel.setWrapText(true);

        barContainer.setAlignment(Pos.BOTTOM_CENTER);
        barContainer.getStyleClass().add("visualizer-canvas-container");
        barContainer.setMinHeight(280);
        VBox.setVgrow(barContainer, Priority.ALWAYS);

        visualBox.getChildren().addAll(descLabel, barContainer);
        VBox.setVgrow(visualBox, Priority.ALWAYS);

        VBox codeBox = new VBox(8);
        codeBox.getStyleClass().add("card");
        codeBox.setPrefWidth(400);
        codeBox.setPadding(new Insets(16));

        Label codeTitle = new Label("💻 Java Algorithm Implementation");
        codeTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #E2E8F0;");

        codeArea.setEditable(false);
        codeArea.getStyleClass().add("code-panel");
        VBox.setVgrow(codeArea, Priority.ALWAYS);

        codeBox.getChildren().addAll(codeTitle, codeArea);
        VBox.setVgrow(codeBox, Priority.ALWAYS);

        splitPane.getItems().addAll(visualBox, codeBox);
        splitPane.setDividerPositions(0.65);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        this.getChildren().addAll(toolCard, splitPane);

        timeline = new Timeline(new KeyFrame(Duration.millis(900), e -> {
            if (currentTrace != null && stepIndex < currentTrace.getStepCount() - 1) {
                stepNext();
            } else {
                pause();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        executeSort();
    }

    private void executeSort() {
        String algo = algoCombo.getValue();
        String[] parts = inputField.getText().split(",");
        int[] arr = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                arr[i] = Integer.parseInt(parts[i].trim());
            }
            inputField.setStyle("");
        } catch (Exception e) {
            descLabel.setText("⚠️ Please enter valid comma-separated numbers like 45, 12, 89, 23");
            inputField.setStyle("-fx-border-color: #EF4444;");
            return;
        }

        switch (algo) {
            case "Bubble Sort" -> currentTrace = SortingAlgorithms.bubbleSort(arr);
            case "Selection Sort" -> currentTrace = SortingAlgorithms.selectionSort(arr);
            case "Insertion Sort" -> currentTrace = SortingAlgorithms.insertionSort(arr);
        }

        if (currentTrace != null) {
            codeArea.setText(currentTrace.getJavaCode());
            stepIndex = 0;
            renderStep(currentTrace.getStep(0));
        }
    }

    private void renderStep(TraceStep step) {
        if (step == null) return;
        stepLabel.setText("Step " + step.getStepNumber() + " / " + currentTrace.getStepCount());
        descLabel.setText(step.getTitle() + " — " + step.getDescription());
        complexityBadge.setText("Time: " + step.getTimeComplexity());

        barContainer.getChildren().clear();
        List<Integer> elements = step.getElements();
        List<Integer> highlighted = step.getHighlightedIndices();

        int maxVal = elements.stream().max(Integer::compareTo).orElse(100);

        for (int i = 0; i < elements.size(); i++) {
            int val = elements.get(i);
            VBox col = new VBox(6);
            col.setAlignment(Pos.BOTTOM_CENTER);

            Label valLbl = new Label(String.valueOf(val));
            valLbl.setStyle("-fx-font-size: 12px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

            double height = Math.max(35, (val / (double) maxVal) * 180);
            Region bar = new Region();
            bar.setPrefSize(44, height);

            boolean isHl = highlighted.contains(i);
            if (isHl) {
                bar.setStyle("-fx-background-color: linear-gradient(to top, #0E7490, #38BDF8); -fx-background-radius: 6 6 2 2; -fx-effect: dropshadow(three-pass-box, rgba(56, 189, 248, 0.45), 8, 0, 0, 0);");
            } else {
                bar.setStyle("-fx-background-color: linear-gradient(to top, #4338CA, #6366F1); -fx-background-radius: 6 6 2 2;");
            }

            Label idxLbl = new Label("[" + i + "]");
            idxLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: #64748B;");

            col.getChildren().addAll(valLbl, bar, idxLbl);
            barContainer.getChildren().add(col);
        }
    }

    private void stepNext() {
        if (currentTrace != null && stepIndex < currentTrace.getStepCount() - 1) {
            stepIndex++;
            renderStep(currentTrace.getStep(stepIndex));
        }
    }

    private void stepPrev() {
        if (currentTrace != null && stepIndex > 0) {
            stepIndex--;
            renderStep(currentTrace.getStep(stepIndex));
        }
    }

    private void togglePlay() {
        if (timeline.getStatus() == Timeline.Status.RUNNING) {
            pause();
        } else {
            play();
        }
    }

    private void play() {
        if (currentTrace == null) return;
        if (stepIndex >= currentTrace.getStepCount() - 1) stepIndex = 0;
        playBtn.setText("⏸ Pause");
        timeline.play();
    }

    private void pause() {
        playBtn.setText("▶ Play");
        timeline.stop();
    }

    private void reset() {
        pause();
        stepIndex = 0;
        if (currentTrace != null) renderStep(currentTrace.getStep(0));
    }
}
