package com.dsstudio.ui.components;

import com.dsstudio.service.GamificationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class HeaderBar extends HBox {
    private final Label pageTitleLabel;
    private final Label levelBadge;
    private final Label xpBadge;
    private final Label streakBadge;
    private final ProgressBar xpBar;

    public HeaderBar(Runnable onQuickChallenge) {
        this.getStyleClass().add("header-bar");
        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(16);

        pageTitleLabel = new Label("Dashboard");
        pageTitleLabel.getStyleClass().add("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Gamification profile metrics chip
        HBox statsChip = new HBox(12);
        statsChip.setAlignment(Pos.CENTER_LEFT);
        statsChip.setPadding(new Insets(4, 12, 4, 12));
        statsChip.setStyle("-fx-background-color: #1E293B; -fx-background-radius: 999px; -fx-border-color: #334155; -fx-border-radius: 999px;");

        streakBadge = new Label("🔥 3 Days");
        streakBadge.getStyleClass().add("badge-amber");

        levelBadge = new Label("Level 2 • Data Craftsman");
        levelBadge.getStyleClass().add("badge-indigo");

        xpBadge = new Label("150 / 250 XP");
        xpBadge.setStyle("-fx-font-size: 11.5px; -fx-font-weight: 700; -fx-text-fill: #94A3B8;");

        xpBar = new ProgressBar(0.60);
        xpBar.setPrefWidth(90);
        xpBar.setPrefHeight(8);
        xpBar.getStyleClass().add("progress-bar");

        statsChip.getChildren().addAll(streakBadge, levelBadge, xpBar, xpBadge);

        Button quickActionBtn = new Button("⚡ Daily Challenge");
        quickActionBtn.getStyleClass().add("btn-primary");
        if (onQuickChallenge != null) {
            quickActionBtn.setOnAction(e -> onQuickChallenge.run());
        }

        this.getChildren().addAll(pageTitleLabel, spacer, statsChip, quickActionBtn);
        refresh();
    }

    public void setPageTitle(String title) {
        pageTitleLabel.setText(title);
    }

    public void refresh() {
        GamificationService.ProfileData profile = GamificationService.getProfile();
        streakBadge.setText("🔥 " + profile.streak + " Days");
        levelBadge.setText("Level " + profile.level + " • " + profile.levelTitle);
        xpBadge.setText(profile.xp + " XP");
        double progress = (profile.xp % 250) / 250.0;
        xpBar.setProgress(Math.max(0.1, progress));
    }
}
