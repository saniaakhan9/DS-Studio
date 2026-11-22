package com.dsstudio.ui.views;

import com.dsstudio.core.structures.linear.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;

public class CodeLabView extends VBox {
    private final ComboBox<String> structureSelector = new ComboBox<>();
    private final TextArea codeDisplayArea = new TextArea();
    private final Label complexityLabel = new Label("Time: O(N) | Space: O(1)");
    private final Label copyStatus = new Label("");

    public CodeLabView() {
        this.setSpacing(16);
        this.setPadding(new Insets(20));
        this.setFillWidth(true);

        // Header Card
        VBox headerCard = new VBox(12);
        headerCard.getStyleClass().add("card");

        HBox topRow = new HBox(16);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("💻 Java Code Lab");
        title.setStyle("-fx-font-size: 19px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        structureSelector.getItems().addAll(
                "Array (Insertion & Shift)",
                "Array (Deletion & Shift)",
                "Array (Linear Search)",
                "Array (Binary Search)",
                "Array (Two-Pointer Reversal)",
                "Stack (LIFO Push & Pop)",
                "Queue (FIFO Enqueue & Dequeue)",
                "Circular Queue (Modulo Indexing)",
                "Singly Linked List (Head Insertion)",
                "Doubly Linked List (Two-Way Links)"
        );
        structureSelector.setValue("Array (Insertion & Shift)");
        structureSelector.setOnAction(e -> updateCode());

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button copyBtn = new Button("📋 Copy Java Code");
        copyBtn.getStyleClass().add("btn-primary");
        copyBtn.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(codeDisplayArea.getText());
            clipboard.setContent(content);
            copyStatus.setText("✅ Copied to clipboard!");
        });

        topRow.getChildren().addAll(title, new Label("Module:"), structureSelector, sp, copyStatus, copyBtn);

        HBox metaRow = new HBox(12);
        metaRow.setAlignment(Pos.CENTER_LEFT);
        complexityLabel.getStyleClass().add("badge-cyan");
        copyStatus.setStyle("-fx-text-fill: #10B981; -fx-font-size: 12px; -fx-font-weight: 700;");

        metaRow.getChildren().addAll(complexityLabel);
        headerCard.getChildren().addAll(topRow, metaRow);

        // Code Editor Card with Window Bar
        VBox editorBox = new VBox();
        editorBox.getStyleClass().add("card");
        editorBox.setPadding(new Insets(0));
        VBox.setVgrow(editorBox, Priority.ALWAYS);

        HBox editorHeader = new HBox(10);
        editorHeader.setAlignment(Pos.CENTER_LEFT);
        editorHeader.setPadding(new Insets(10, 16, 10, 16));
        editorHeader.setStyle("-fx-background-color: #0F172A; -fx-border-color: #1F2937; -fx-border-width: 0 0 1 0; -fx-background-radius: 12 12 0 0;");

        Label dot1 = new Label("●"); dot1.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 11px;");
        Label dot2 = new Label("●"); dot2.setStyle("-fx-text-fill: #F59E0B; -fx-font-size: 11px;");
        Label dot3 = new Label("●"); dot3.setStyle("-fx-text-fill: #10B981; -fx-font-size: 11px;");
        Label fileTitle = new Label("Solution.java  •  Pure Academic Implementation");
        fileTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #94A3B8;");

        editorHeader.getChildren().addAll(dot1, dot2, dot3, fileTitle);

        codeDisplayArea.setEditable(false);
        codeDisplayArea.getStyleClass().add("code-panel");
        codeDisplayArea.setStyle("-fx-border-width: 0; -fx-background-radius: 0 0 12 12;");
        VBox.setVgrow(codeDisplayArea, Priority.ALWAYS);

        editorBox.getChildren().addAll(editorHeader, codeDisplayArea);

        this.getChildren().addAll(headerCard, editorBox);
        updateCode();
    }

    private void updateCode() {
        copyStatus.setText("");
        String sel = structureSelector.getValue();
        if (sel == null) return;

        switch (sel) {
            case "Array (Insertion & Shift)" -> {
                codeDisplayArea.setText(ArrayStructure.INSERT_JAVA_CODE);
                complexityLabel.setText("Time Complexity: O(N) worst case (shifts elements) | Auxiliary Space: O(1)");
            }
            case "Array (Deletion & Shift)" -> {
                codeDisplayArea.setText(ArrayStructure.DELETE_JAVA_CODE);
                complexityLabel.setText("Time Complexity: O(N) left shifts | Auxiliary Space: O(1)");
            }
            case "Array (Linear Search)" -> {
                codeDisplayArea.setText(ArrayStructure.LINEAR_SEARCH_JAVA_CODE);
                complexityLabel.setText("Time Complexity: O(N) sequential scan | Auxiliary Space: O(1)");
            }
            case "Array (Binary Search)" -> {
                codeDisplayArea.setText(ArrayStructure.BINARY_SEARCH_JAVA_CODE);
                complexityLabel.setText("Time Complexity: O(log N) divide and conquer | Auxiliary Space: O(1)");
            }
            case "Array (Two-Pointer Reversal)" -> {
                codeDisplayArea.setText(ArrayStructure.REVERSE_JAVA_CODE);
                complexityLabel.setText("Time Complexity: O(N) in-place swaps | Auxiliary Space: O(1)");
            }
            case "Stack (LIFO Push & Pop)" -> {
                codeDisplayArea.setText(StackStructure.PUSH_JAVA_CODE + "\n\n" + StackStructure.POP_JAVA_CODE);
                complexityLabel.setText("Time Complexity: O(1) Push & Pop | Auxiliary Space: O(1)");
            }
            case "Queue (FIFO Enqueue & Dequeue)" -> {
                codeDisplayArea.setText(QueueStructure.ENQUEUE_JAVA_CODE + "\n\n" + QueueStructure.DEQUEUE_JAVA_CODE);
                complexityLabel.setText("Time Complexity: O(1) Enqueue & Dequeue | Auxiliary Space: O(1)");
            }
            case "Circular Queue (Modulo Indexing)" -> {
                codeDisplayArea.setText(CircularQueueStructure.CIRCULAR_ENQUEUE_JAVA_CODE + "\n\n" + CircularQueueStructure.CIRCULAR_DEQUEUE_JAVA_CODE);
                complexityLabel.setText("Time Complexity: O(1) Modulo Math | Auxiliary Space: O(1)");
            }
            case "Singly Linked List (Head Insertion)" -> {
                codeDisplayArea.setText(SinglyLinkedListStructure.INSERT_HEAD_JAVA + "\n\n" + SinglyLinkedListStructure.DELETE_HEAD_JAVA);
                complexityLabel.setText("Time Complexity: O(1) Head Operations | Auxiliary Space: O(1)");
            }
            case "Doubly Linked List (Two-Way Links)" -> {
                codeDisplayArea.setText(DoublyLinkedListStructure.INSERT_HEAD_JAVA + "\n\n" + DoublyLinkedListStructure.INSERT_TAIL_JAVA);
                complexityLabel.setText("Time Complexity: O(1) Head & Tail | Auxiliary Space: O(1)");
            }
        }
    }
}
