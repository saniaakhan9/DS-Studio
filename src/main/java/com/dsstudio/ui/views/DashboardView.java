package com.dsstudio.ui.views;

import com.dsstudio.service.GamificationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class DashboardView extends ScrollPane {
    private final Consumer<String> onNavigate;

    public DashboardView(Consumer<String> onNavigate) {
        this.onNavigate = onNavigate;
        this.setFitToWidth(true);
        this.getStyleClass().add("scroll-pane");

        VBox root = new VBox(22);
        root.setPadding(new Insets(24));
        root.setFillWidth(true);

        GamificationService.ProfileData profile = GamificationService.getProfile();

        // 1. Welcome Hero Banner
        HBox welcomeCard = new HBox(16);
        welcomeCard.getStyleClass().addAll("card", "card-accent");
        welcomeCard.setAlignment(Pos.CENTER_LEFT);
        welcomeCard.setPadding(new Insets(26));

        VBox welcomeText = new VBox(8);
        Label greet = new Label("Welcome back, " + profile.username + " 🚀");
        greet.setStyle("-fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC; -fx-letter-spacing: -0.4px;");
        Label sub = new Label("Explore interactive execution traces, master verified Java implementations, and climb the mastery leaderboard.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #94A3B8;");
        welcomeText.getChildren().addAll(greet, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button startBtn = new Button("Launch Visualizer Lab ➜");
        startBtn.getStyleClass().add("btn-primary");
        startBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 22;");
        startBtn.setOnAction(e -> onNavigate.accept("Visualizer Lab"));

        welcomeCard.getChildren().addAll(welcomeText, spacer, startBtn);

        // 2. Metrics Grid
        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(16);
        metricsGrid.setVgap(16);

        metricsGrid.add(createStatCard("TOTAL XP EARNED", String.valueOf(profile.xp), "Level " + profile.level + " • " + profile.levelTitle, "#818CF8"), 0, 0);
        metricsGrid.add(createStatCard("DAILY STREAK", profile.streak + " Days", "Consistent daily study", "#F59E0B"), 1, 0);
        metricsGrid.add(createStatCard("LINEAR STRUCTURES", "7 Modules", "Arrays, Stack, Queue, Lists", "#10B981"), 2, 0);
        metricsGrid.add(createStatCard("ALGORITHMS READY", "5 Sorts & Searches", "Visual traces available", "#38BDF8"), 3, 0);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            metricsGrid.getColumnConstraints().add(col);
        }

        // 3. Quick Visualizers & Daily Challenge
        HBox middleRow = new HBox(16);

        // Daily Challenge Card
        VBox dailyCard = new VBox(14);
        dailyCard.getStyleClass().add("card");
        dailyCard.setPrefWidth(440);

        HBox dailyHead = new HBox(8);
        dailyHead.setAlignment(Pos.CENTER_LEFT);
        Label dIcon = new Label("🎯");
        Label dTitle = new Label("Daily Concept & Trace Challenge");
        dTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
        dailyHead.getChildren().addAll(dIcon, dTitle);

        Label qTeaser = new Label("Circular Queue Modulo Indexing: What is the exact full condition expression for a ring buffer?");
        qTeaser.setStyle("-fx-font-size: 13px; -fx-text-fill: #CBD5E1; -fx-line-spacing: 2px;");
        qTeaser.setWrapText(true);

        HBox badgeRow = new HBox(8);
        badgeRow.setAlignment(Pos.CENTER_LEFT);
        Label dDiff = new Label("Medium • 20 XP");
        dDiff.getStyleClass().add("badge-cyan");
        badgeRow.getChildren().add(dDiff);

        Region cardSpacer = new Region();
        VBox.setVgrow(cardSpacer, Priority.ALWAYS);

        Button solveBtn = new Button("Solve in DS Quest ➜");
        solveBtn.getStyleClass().add("btn-primary");
        solveBtn.setOnAction(e -> onNavigate.accept("DS Quest"));

        dailyCard.getChildren().addAll(dailyHead, qTeaser, badgeRow, cardSpacer, solveBtn);

        // Quick Visualizer Launchpad
        VBox launchCard = new VBox(14);
        launchCard.getStyleClass().add("card");
        HBox.setHgrow(launchCard, Priority.ALWAYS);

        Label launchTitle = new Label("⚡ Quick Access Visualizer Lab");
        launchTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        Label launchDesc = new Label("Directly open interactive snapshot animation workspaces with custom inputs.");
        launchDesc.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #94A3B8;");

        FlowPane quickChips = new FlowPane(10, 10);
        quickChips.getChildren().addAll(
                createChip("Array Shifts & Search", "Visualizer Lab"),
                createChip("Stack LIFO & Overflow", "Visualizer Lab"),
                createChip("Queue & Modulo Wrap", "Visualizer Lab"),
                createChip("Singly Linked List Nodes", "Visualizer Lab"),
                createChip("Doubly Linked Two-Way Links", "Visualizer Lab"),
                createChip("Sorting Animations", "Algorithms")
        );

        launchCard.getChildren().addAll(launchTitle, launchDesc, quickChips);
        middleRow.getChildren().addAll(dailyCard, launchCard);

        root.getChildren().addAll(welcomeCard, metricsGrid, middleRow);
        this.setContent(root);
    }

    private VBox createStatCard(String label, String value, String sub, String accentColor) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setStyle("-fx-border-color: " + accentColor + "; -fx-border-width: 1 1 1 3;");

        Label l = new Label(label);
        l.getStyleClass().add("stat-title");

        Label v = new Label(value);
        v.getStyleClass().add("stat-value");
        v.setStyle("-fx-text-fill: " + accentColor + ";");

        Label s = new Label(sub);
        s.setStyle("-fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: #64748B;");

        card.getChildren().addAll(l, v, s);
        return card;
    }

    private Button createChip(String text, String target) {
        Button b = new Button("🔬 " + text);
        b.getStyleClass().add("btn-secondary");
        b.setOnAction(e -> onNavigate.accept(target));
        return b;
    }
}
