package com.dsstudio.ui.views;

import com.dsstudio.db.DatabaseManager;
import com.dsstudio.service.GamificationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ProgressView extends ScrollPane {

    public ProgressView() {
        this.setFitToWidth(true);
        this.getStyleClass().add("scroll-pane");

        VBox root = new VBox(22);
        root.setPadding(new Insets(24));

        GamificationService.ProfileData profile = GamificationService.getProfile();

        // Header Card
        VBox headerCard = new VBox(14);
        headerCard.getStyleClass().addAll("card", "card-accent");
        headerCard.setPadding(new Insets(22));

        Label title = new Label("🌟 Student Mastery & Learning Analytics");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC;");

        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
                createStat("CURRENT LEVEL", "Level " + profile.level, profile.levelTitle, "#6366F1"),
                createStat("LIFETIME XP", String.valueOf(profile.xp), "Total points accumulated", "#10B981"),
                createStat("ACTIVE STREAK", profile.streak + " Days", "Consistent daily study", "#F59E0B")
        );

        headerCard.getChildren().addAll(title, statsRow);

        // Achievements Showcase
        VBox achCard = new VBox(14);
        achCard.getStyleClass().add("card");
        achCard.setPadding(new Insets(22));

        Label achTitle = new Label("🏆 Earned Badges & Mastery Milestones");
        achTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
        achCard.getChildren().add(achTitle);

        FlowPane badgesPane = new FlowPane(14, 14);
        List<GamificationService.AchievementItem> achievements = GamificationService.getAchievements();

        for (var a : achievements) {
            VBox badgeBox = new VBox(6);
            badgeBox.setPrefWidth(240);
            badgeBox.setPadding(new Insets(14));
            if (a.unlocked) {
                badgeBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #1E1B4B, #0F172A); -fx-border-color: #818CF8; -fx-border-width: 1.5; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(99, 102, 241, 0.25), 8, 0, 0, 3);");
            } else {
                badgeBox.setStyle("-fx-background-color: #0F172A; -fx-border-color: #334155; -fx-border-radius: 10; -fx-background-radius: 10;");
            }

            Label bTitle = new Label((a.unlocked ? "🏅 " : "🔒 ") + a.title);
            bTitle.setStyle("-fx-font-weight: 800; -fx-font-size: 13.5px; -fx-text-fill: " + (a.unlocked ? "#F8FAFC" : "#64748B") + ";");

            Label bDesc = new Label(a.description);
            bDesc.setStyle("-fx-font-size: 11.5px; -fx-text-fill: " + (a.unlocked ? "#CBD5E1" : "#475569") + "; -fx-line-spacing: 1.5px;");
            bDesc.setWrapText(true);

            badgeBox.getChildren().addAll(bTitle, bDesc);
            badgesPane.getChildren().add(badgeBox);
        }
        achCard.getChildren().add(badgesPane);

        // Recent Quiz Attempts History from SQLite
        VBox quizHistCard = new VBox(14);
        quizHistCard.getStyleClass().add("card");
        quizHistCard.setPadding(new Insets(22));

        Label qhTitle = new Label("📝 Recent Exam Quiz History (Local SQLite DB)");
        qhTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
        quizHistCard.getChildren().add(qhTitle);

        VBox attemptsList = new VBox(8);
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT topic, score, total_questions, accuracy, xp_earned, attempt_timestamp " +
                     "FROM quiz_attempts ORDER BY id DESC LIMIT 5")) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                HBox row = new HBox(16);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle("-fx-background-color: #0F172A; -fx-padding: 12 16; -fx-background-radius: 8; -fx-border-color: #334155; -fx-border-radius: 8;");

                Label topLbl = new Label(rs.getString("topic"));
                topLbl.setStyle("-fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-pref-width: 180; -fx-font-size: 13px;");

                Label scLbl = new Label("Score: " + rs.getInt("score") + " / " + rs.getInt("total_questions"));
                scLbl.setStyle("-fx-text-fill: #F8FAFC; -fx-pref-width: 110; -fx-font-size: 13px; -fx-font-weight: 600;");

                Label accLbl = new Label(String.format("%.1f%%", rs.getDouble("accuracy")));
                accLbl.setStyle("-fx-font-weight: 800; -fx-text-fill: #10B981; -fx-pref-width: 80; -fx-font-size: 13px;");

                Label xpLbl = new Label("+" + rs.getInt("xp_earned") + " XP");
                xpLbl.getStyleClass().add("badge-cyan");

                Region sp = new Region();
                HBox.setHgrow(sp, Priority.ALWAYS);

                Label dateLbl = new Label(rs.getString("attempt_timestamp"));
                dateLbl.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11.5px; -fx-font-weight: 600;");

                row.getChildren().addAll(topLbl, scLbl, accLbl, xpLbl, sp, dateLbl);
                attemptsList.getChildren().add(row);
            }

            if (!found) {
                Label noQ = new Label("No quiz attempts recorded yet. Launch the Quiz Centre to record your first exam score!");
                noQ.setStyle("-fx-text-fill: #64748B; -fx-font-style: italic; -fx-padding: 8 0;");
                attemptsList.getChildren().add(noQ);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        quizHistCard.getChildren().add(attemptsList);

        root.getChildren().addAll(headerCard, achCard, quizHistCard);
        this.setContent(root);
    }

    private VBox createStat(String label, String val, String sub, String color) {
        VBox b = new VBox(6);
        b.getStyleClass().add("card");
        b.setStyle("-fx-border-color: " + color + "; -fx-border-width: 1 1 1 3;");
        b.setPrefWidth(240);
        Label l = new Label(label);
        l.getStyleClass().add("stat-title");
        Label v = new Label(val);
        v.setStyle("-fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + color + ";");
        Label s = new Label(sub);
        s.setStyle("-fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: #64748B;");
        b.getChildren().addAll(l, v, s);
        return b;
    }
}
