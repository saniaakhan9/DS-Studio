package com.dsstudio.ui.views;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ComplexityGuideView extends ScrollPane {

    public ComplexityGuideView() {
        this.setFitToWidth(true);
        this.getStyleClass().add("scroll-pane");

        VBox root = new VBox(22);
        root.setPadding(new Insets(24));

        // Header Card
        VBox headerCard = new VBox(8);
        headerCard.getStyleClass().addAll("card", "card-accent");
        headerCard.setPadding(new Insets(22));

        Label title = new Label("📈 Master Complexity & Big-O Guide");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC;");

        Label sub = new Label("Comprehensive reference comparing asymptotic time and space complexities for linear data structures and sorting algorithms.");
        sub.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #94A3B8;");
        headerCard.getChildren().addAll(title, sub);

        // Comparison Table Card
        VBox tableCard = new VBox(14);
        tableCard.getStyleClass().add("card");
        tableCard.setPadding(new Insets(22));

        Label tableTitle = new Label("Data Structure Operations Asymptotic Matrix");
        tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(10);
        grid.setPadding(new Insets(8, 0, 8, 0));

        // Header
        addHeaderCell(grid, "Data Structure", 0);
        addHeaderCell(grid, "Access (by index)", 1);
        addHeaderCell(grid, "Search", 2);
        addHeaderCell(grid, "Insertion", 3);
        addHeaderCell(grid, "Deletion", 4);
        addHeaderCell(grid, "Space", 5);

        // Rows
        addRow(grid, 1, "Array", "O(1)", "O(N) / O(log N)*", "O(N)", "O(N)", "O(N)");
        addRow(grid, 2, "Stack (Array/List)", "O(N)", "O(N)", "O(1) [Push]", "O(1) [Pop]", "O(N)");
        addRow(grid, 3, "Queue (Linear)", "O(N)", "O(N)", "O(1) [Enqueue]", "O(1) [Dequeue]", "O(N)");
        addRow(grid, 4, "Circular Queue", "O(N)", "O(N)", "O(1) [Modulo]", "O(1) [Modulo]", "O(N)");
        addRow(grid, 5, "Singly Linked List", "O(N)", "O(N)", "O(1) [Head] / O(N) [Tail]", "O(1) [Head]", "O(N)");
        addRow(grid, 6, "Doubly Linked List", "O(N)", "O(N)", "O(1) [Head & Tail]", "O(1) [with ref]", "O(N)");
        addRow(grid, 7, "Circular Linked List", "O(N)", "O(N)", "O(1) [Head & Tail]", "O(1) [Head]", "O(N)");

        tableCard.getChildren().addAll(tableTitle, grid);

        // Asymptotic Definitions
        VBox asympCard = new VBox(14);
        asympCard.getStyleClass().add("card");
        asympCard.setPadding(new Insets(22));

        Label asympTitle = new Label("Core Asymptotic Notations");
        asympTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        asympCard.getChildren().addAll(
                asympTitle,
                createNotationNote("Big-O Notation (O)", "Represents the strict upper bound on runtime. Guarantees that algorithm performance will never exceed this bound in the worst case."),
                createNotationNote("Big-Omega (Ω)", "Represents the tight lower bound on runtime, reflecting the best-case execution performance under ideal conditions."),
                createNotationNote("Big-Theta (Θ)", "Represents the asymptotically tight bound when upper and lower bounds asymptotically converge (e.g., Merge Sort Θ(N log N)).")
        );

        root.getChildren().addAll(headerCard, tableCard, asympCard);
        this.setContent(root);
    }

    private void addHeaderCell(GridPane grid, String text, int col) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-font-size: 12px; -fx-padding: 8 0;");
        grid.add(lbl, col, 0);
    }

    private void addRow(GridPane grid, int row, String name, String acc, String search, String ins, String del, String space) {
        Label n = new Label(name);
        n.setStyle("-fx-font-weight: 700; -fx-text-fill: #F8FAFC; -fx-font-size: 13px;");
        grid.add(n, 0, row);
        grid.add(createBadge(acc), 1, row);
        grid.add(createBadge(search), 2, row);
        grid.add(createBadge(ins), 3, row);
        grid.add(createBadge(del), 4, row);
        grid.add(createBadge(space), 5, row);
    }

    private Label createBadge(String text) {
        Label lbl = new Label(text);
        if (text.startsWith("O(1)")) {
            lbl.getStyleClass().add("badge-emerald");
        } else if (text.startsWith("O(log")) {
            lbl.getStyleClass().add("badge-cyan");
        } else {
            lbl.getStyleClass().add("badge-indigo");
        }
        return lbl;
    }

    private VBox createNotationNote(String symbol, String explanation) {
        VBox b = new VBox(4);
        b.setStyle("-fx-background-color: #0F172A; -fx-padding: 12 16; -fx-background-radius: 8; -fx-border-color: #334155; -fx-border-radius: 8;");
        Label s = new Label(symbol);
        s.setStyle("-fx-font-weight: 800; -fx-text-fill: #A5B4FC; -fx-font-size: 13.5px;");
        Label e = new Label(explanation);
        e.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12.5px; -fx-line-spacing: 2px;");
        e.setWrapText(true);
        b.getChildren().addAll(s, e);
        return b;
    }
}
