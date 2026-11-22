package com.dsstudio.ui.views;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AboutView extends ScrollPane {

    public AboutView() {
        this.setFitToWidth(true);
        this.getStyleClass().add("scroll-pane");

        VBox root = new VBox(22);
        root.setPadding(new Insets(24));

        // Header Card
        VBox headerCard = new VBox(10);
        headerCard.getStyleClass().addAll("card", "card-accent");
        headerCard.setPadding(new Insets(22));

        Label title = new Label("ℹ️ About DS Studio & DS Quest");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC;");

        Label tag = new Label("Tagline: Learn It. Visualize It. Code It. Master It.");
        tag.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #38BDF8;");

        Label sub = new Label("Engineered as an advanced desktop platform for MCA and Computer Science academic demonstration and viva examination.");
        sub.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #94A3B8;");
        headerCard.getChildren().addAll(title, tag, sub);

        // Technical Specifications Card
        VBox techCard = new VBox(14);
        techCard.getStyleClass().add("card");
        techCard.setPadding(new Insets(22));

        Label techTitle = new Label("🛠 Technical Architecture Specifications");
        techTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        GridPane specGrid = new GridPane();
        specGrid.setHgap(20);
        specGrid.setVgap(12);

        specGrid.add(createTechItem("Programming Language", "Java 21 LTS (Eclipse Temurin Hotspot)"), 0, 0);
        specGrid.add(createTechItem("Desktop UI Framework", "JavaFX 21.0.6 (Modular Architecture with Obsidian CSS)"), 1, 0);
        specGrid.add(createTechItem("Execution-Trace Engine", "Granular Immutable State Snapshots with StepType event dispatch"), 0, 1);
        specGrid.add(createTechItem("Persistence Layer", "SQLite 3.45.2.0 via JDBC (Offline local database in ~/.dsstudio)"), 1, 1);
        specGrid.add(createTechItem("Automated Test Suite", "JUnit Jupiter 5.10.2 (100% linear structures & sorting tests passing)"), 0, 2);
        specGrid.add(createTechItem("Build & Dependency Tool", "Apache Maven 3.9.9"), 1, 2);

        techCard.getChildren().addAll(techTitle, specGrid);

        // Viva Demonstration Guide Card
        VBox vivaCard = new VBox(14);
        vivaCard.getStyleClass().add("card");
        vivaCard.setPadding(new Insets(22));

        Label vivaTitle = new Label("🎓 MCA Mini-Project Viva Demonstration Highlights");
        vivaTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        vivaCard.getChildren().addAll(
                vivaTitle,
                createBullet("Decoupled Execution Tracing", "The UI does not compute or fake algorithms; standalone core Java data structures execute real operations and return immutable state snapshot traces."),
                createBullet("Boundary & Edge-Case Resilience", "Graceful handling of stack overflow/underflow, linear queue false overflow, circular queue modulo indexing, and empty list operations."),
                createBullet("Synchronized Java Code Highlighting", "As the student steps through an insertion or search animation, the corresponding line of real Java source code is actively highlighted in real-time."),
                createBullet("Gamified Learning Engine", "Adaptive question bank, XP scoring policy, streak tracking, level progression, and badges recorded in local SQLite database.")
        );

        root.getChildren().addAll(headerCard, techCard, vivaCard);
        this.setContent(root);
    }

    private VBox createTechItem(String label, String value) {
        VBox b = new VBox(4);
        b.setStyle("-fx-background-color: #0F172A; -fx-padding: 10 14; -fx-background-radius: 8; -fx-border-color: #334155; -fx-border-radius: 8;");
        Label l = new Label(label);
        l.setStyle("-fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-font-size: 12.5px;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 12.5px;");
        b.getChildren().addAll(l, v);
        return b;
    }

    private VBox createBullet(String heading, String detail) {
        VBox b = new VBox(4);
        b.setStyle("-fx-background-color: #0F172A; -fx-padding: 12 16; -fx-background-radius: 8; -fx-border-color: #334155; -fx-border-radius: 8;");
        Label h = new Label("• " + heading);
        h.setStyle("-fx-font-weight: 800; -fx-text-fill: #A5B4FC; -fx-font-size: 13.5px;");
        Label d = new Label(detail);
        d.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12.5px; -fx-line-spacing: 2px;");
        d.setWrapText(true);
        b.getChildren().addAll(h, d);
        return b;
    }
}
