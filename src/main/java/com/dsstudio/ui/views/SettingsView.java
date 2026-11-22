package com.dsstudio.ui.views;

import com.dsstudio.db.DatabaseManager;
import com.dsstudio.service.GamificationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class SettingsView extends VBox {
    private final TextField usernameField = new TextField();
    private final Label statusLabel = new Label();

    public SettingsView() {
        this.setSpacing(20);
        this.setPadding(new Insets(24));
        this.setFillWidth(true);

        // Header Card
        VBox headerCard = new VBox(8);
        headerCard.getStyleClass().addAll("card", "card-accent");
        headerCard.setPadding(new Insets(22));

        Label title = new Label("⚙️ Application Settings & Preferences");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC;");
        Label desc = new Label("Manage your learner profile, preferences, and local data persistence.");
        desc.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #94A3B8;");
        headerCard.getChildren().addAll(title, desc);

        // Profile Setting Card
        VBox profileCard = new VBox(14);
        profileCard.getStyleClass().add("card");
        profileCard.setPadding(new Insets(22));

        Label pTitle = new Label("Learner Identity & Display Name");
        pTitle.setStyle("-fx-font-size: 15.5px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        HBox pRow = new HBox(12);
        pRow.setAlignment(Pos.CENTER_LEFT);

        GamificationService.ProfileData p = GamificationService.getProfile();
        usernameField.setText(p.username);
        usernameField.setPrefWidth(220);

        Button saveBtn = new Button("Save Profile");
        saveBtn.getStyleClass().add("btn-primary");
        saveBtn.setOnAction(e -> {
            String newName = usernameField.getText().trim();
            if (!newName.isEmpty()) {
                try (Connection conn = DatabaseManager.getConnection();
                     Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("UPDATE profile SET username = '" + newName.replace("'", "''") + "' WHERE id = 1");
                    statusLabel.setText("✅ Profile updated successfully!");
                } catch (Exception ex) {
                    statusLabel.setText("⚠️ Failed to update profile.");
                }
            }
        });

        pRow.getChildren().addAll(new Label("Display Name:"), usernameField, saveBtn);
        statusLabel.setStyle("-fx-font-weight: 800; -fx-text-fill: #10B981; -fx-font-size: 12.5px;");

        profileCard.getChildren().addAll(pTitle, pRow, statusLabel);

        // Local Storage Information
        VBox storageCard = new VBox(12);
        storageCard.getStyleClass().add("card");
        storageCard.setPadding(new Insets(22));

        Label sTitle = new Label("Local Offline SQLite Database");
        sTitle.setStyle("-fx-font-size: 15.5px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        String dbPath = System.getProperty("user.home") + File.separator + ".dsstudio" + File.separator + "ds_studio.db";
        Label pathLabel = new Label("Database Path: " + dbPath);
        pathLabel.setStyle("-fx-font-family: 'Consolas', monospace; -fx-text-fill: #38BDF8; -fx-background-color: #0F172A; -fx-padding: 8 14; -fx-background-radius: 6; -fx-border-color: #334155; -fx-border-radius: 6;");

        Label offlineNote = new Label("All progress, quiz attempts, and achievements are stored 100% locally on your machine with zero internet dependency.");
        offlineNote.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #94A3B8;");

        storageCard.getChildren().addAll(sTitle, pathLabel, offlineNote);

        // Danger Zone
        VBox dangerCard = new VBox(12);
        dangerCard.getStyleClass().add("card");
        dangerCard.setStyle("-fx-border-color: #EF4444; -fx-border-width: 1 1 1 3;");
        dangerCard.setPadding(new Insets(22));

        Label dTitle = new Label("Reset Data & Progress");
        dTitle.setStyle("-fx-font-size: 15.5px; -fx-font-weight: 800; -fx-text-fill: #EF4444;");

        Label dWarning = new Label("Reset all learning progress, quiz records, and XP back to initial default state. This action cannot be undone.");
        dWarning.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #94A3B8;");

        Button resetBtn = new Button("⚠️ Reset All Learning Progress");
        resetBtn.getStyleClass().add("btn-danger");
        resetBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to reset all progress?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try (Connection conn = DatabaseManager.getConnection();
                         Statement stmt = conn.createStatement()) {
                        stmt.executeUpdate("DELETE FROM quiz_attempts");
                        stmt.executeUpdate("UPDATE profile SET xp = 0, level = 1, streak = 1 WHERE id = 1");
                        statusLabel.setText("✅ Progress reset successfully.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });

        dangerCard.getChildren().addAll(dTitle, dWarning, resetBtn);

        this.getChildren().addAll(headerCard, profileCard, storageCard, dangerCard);
    }
}
