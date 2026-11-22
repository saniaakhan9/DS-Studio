package com.dsstudio.ui.views;

import com.dsstudio.db.DatabaseManager;
import com.dsstudio.model.Question;
import com.dsstudio.repository.QuestionRepository;
import com.dsstudio.service.GamificationService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class QuizCenterView extends VBox {
    private final ComboBox<String> topicCombo = new ComboBox<>();
    private final ComboBox<String> modeCombo = new ComboBox<>();
    private final Button startQuizBtn = new Button("🚀 Start Examination");

    private final VBox quizActiveBox = new VBox(16);
    private final VBox quizSetupBox = new VBox(16);
    private final VBox resultsBox = new VBox(18);

    private List<Question> quizQuestions;
    private int currentQIndex = 0;
    private final Map<Integer, Integer> userAnswers = new HashMap<>();
    private final Set<Integer> markedForReview = new HashSet<>();

    private final Label timerLabel = new Label("⏱ 05:00");
    private final Label questionTitleLabel = new Label();
    private final VBox optionsContainer = new VBox(10);
    private final FlowPane palettePane = new FlowPane(8, 8);
    private final ToggleGroup optionsGroup = new ToggleGroup();

    private Timeline quizTimer;
    private int secondsRemaining = 300;

    public QuizCenterView() {
        this.setSpacing(16);
        this.setPadding(new Insets(20));
        this.setFillWidth(true);

        setupSetupBox();
        setupActiveBox();
        setupResultsBox();

        this.getChildren().addAll(quizSetupBox, quizActiveBox, resultsBox);
        showSetup();
    }

    private void setupSetupBox() {
        quizSetupBox.getStyleClass().addAll("card", "card-accent");
        quizSetupBox.setPadding(new Insets(24));

        Label title = new Label("🏆 Examination Quiz Centre");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC;");

        Label desc = new Label("Simulate university and competitive exam conditions with timed question sets, review palettes, and detailed performance grading.");
        desc.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 13.5px;");

        HBox controls = new HBox(16);
        controls.setAlignment(Pos.CENTER_LEFT);

        topicCombo.getItems().addAll("ALL Linear Structures", "Arrays", "Stack", "Queue", "Singly Linked List", "Algorithms");
        topicCombo.setValue("ALL Linear Structures");

        modeCombo.getItems().addAll("Timed Exam (5 Mins)", "Practice Mode (Untimed)");
        modeCombo.setValue("Timed Exam (5 Mins)");

        startQuizBtn.getStyleClass().add("btn-primary");
        startQuizBtn.setStyle("-fx-font-size: 13.5px; -fx-padding: 9 20;");
        startQuizBtn.setOnAction(e -> startQuiz());

        controls.getChildren().addAll(new Label("Topic:"), topicCombo, new Label("Mode:"), modeCombo, startQuizBtn);
        quizSetupBox.getChildren().addAll(title, desc, controls);
    }

    private void setupActiveBox() {
        quizActiveBox.getStyleClass().add("card");
        quizActiveBox.setPadding(new Insets(22));

        // Top bar
        HBox topBar = new HBox(12);
        topBar.setAlignment(Pos.CENTER_LEFT);

        timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-background-color: #0F172A; -fx-padding: 6 14; -fx-background-radius: 999; -fx-border-color: #334155; -fx-border-radius: 999;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button reviewBtn = new Button("🚩 Mark for Review");
        reviewBtn.getStyleClass().add("btn-secondary");
        reviewBtn.setOnAction(e -> {
            if (markedForReview.contains(currentQIndex)) {
                markedForReview.remove(currentQIndex);
            } else {
                markedForReview.add(currentQIndex);
            }
            refreshPalette();
        });

        Button submitQuizBtn = new Button("Submit Quiz");
        submitQuizBtn.getStyleClass().add("btn-danger");
        submitQuizBtn.setOnAction(e -> finishQuiz());

        topBar.getChildren().addAll(timerLabel, sp, reviewBtn, submitQuizBtn);

        // Question content
        questionTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #F8FAFC; -fx-line-spacing: 2px;");
        questionTitleLabel.setWrapText(true);

        // Navigation bottom row
        HBox navRow = new HBox(12);
        navRow.setAlignment(Pos.CENTER_LEFT);

        Button prevBtn = new Button("◀ Previous");
        prevBtn.getStyleClass().add("btn-secondary");
        prevBtn.setOnAction(e -> {
            if (currentQIndex > 0) {
                currentQIndex--;
                displayQuestion();
            }
        });

        Button nextBtn = new Button("Next ▶");
        nextBtn.getStyleClass().add("btn-primary");
        nextBtn.setOnAction(e -> {
            if (currentQIndex < quizQuestions.size() - 1) {
                currentQIndex++;
                displayQuestion();
            }
        });

        navRow.getChildren().addAll(prevBtn, nextBtn);

        // Palette
        VBox paletteBox = new VBox(8);
        Label pTitle = new Label("Question Palette (Click to jump):");
        pTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #94A3B8;");
        paletteBox.getChildren().addAll(pTitle, palettePane);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #334155;");

        quizActiveBox.getChildren().addAll(topBar, questionTitleLabel, optionsContainer, navRow, sep, paletteBox);
    }

    private void setupResultsBox() {
        resultsBox.getStyleClass().add("card");
        resultsBox.setPadding(new Insets(24));
    }

    private void startQuiz() {
        String topic = topicCombo.getValue();
        if (topic.equals("ALL Linear Structures")) {
            quizQuestions = QuestionRepository.getAll();
        } else {
            quizQuestions = QuestionRepository.getByTopic(topic);
        }

        if (quizQuestions.isEmpty()) {
            quizQuestions = QuestionRepository.getAll();
        }

        userAnswers.clear();
        markedForReview.clear();
        currentQIndex = 0;

        quizSetupBox.setVisible(false);
        quizSetupBox.setManaged(false);
        resultsBox.setVisible(false);
        resultsBox.setManaged(false);

        quizActiveBox.setVisible(true);
        quizActiveBox.setManaged(true);

        // Start timer if timed mode
        if (modeCombo.getValue().startsWith("Timed")) {
            secondsRemaining = 300;
            if (quizTimer != null) quizTimer.stop();
            quizTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                secondsRemaining--;
                int m = secondsRemaining / 60;
                int s = secondsRemaining % 60;
                timerLabel.setText(String.format("⏱ %02d:%02d", m, s));
                
                // Urgent timer coloring in last minute
                if (secondsRemaining <= 60) {
                    timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #EF4444; -fx-background-color: #450A0A; -fx-padding: 6 14; -fx-background-radius: 999; -fx-border-color: #EF4444; -fx-border-radius: 999;");
                } else if (secondsRemaining <= 120) {
                    timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #F59E0B; -fx-background-color: #451A03; -fx-padding: 6 14; -fx-background-radius: 999; -fx-border-color: #F59E0B; -fx-border-radius: 999;");
                }
                
                if (secondsRemaining <= 0) {
                    quizTimer.stop();
                    finishQuiz();
                }
            }));
            quizTimer.setCycleCount(Timeline.INDEFINITE);
            quizTimer.play();
        } else {
            timerLabel.setText("Untimed Mode");
            timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-background-color: #0F172A; -fx-padding: 6 14; -fx-background-radius: 999;");
        }

        displayQuestion();
        refreshPalette();
    }

    private void displayQuestion() {
        if (quizQuestions == null || quizQuestions.isEmpty()) return;
        Question q = quizQuestions.get(currentQIndex);

        questionTitleLabel.setText("Q" + (currentQIndex + 1) + ". " + q.getQuestionText());
        optionsContainer.getChildren().clear();
        optionsGroup.getToggles().clear();

        Integer selectedAnswer = userAnswers.get(currentQIndex);

        for (int i = 0; i < q.getOptions().size(); i++) {
            RadioButton rb = new RadioButton(q.getOptions().get(i));
            rb.setToggleGroup(optionsGroup);
            rb.setUserData(i);
            rb.setMaxWidth(Double.MAX_VALUE);
            
            boolean isSel = (selectedAnswer != null && selectedAnswer == i);
            if (isSel) {
                rb.setSelected(true);
                rb.setStyle("-fx-text-fill: #A5B4FC; -fx-font-size: 13.5px; -fx-font-weight: 700; -fx-padding: 10 14; -fx-background-color: #1E1B4B; -fx-border-color: #6366F1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            } else {
                rb.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 13.5px; -fx-padding: 10 14; -fx-background-color: #0F172A; -fx-border-color: #334155; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            }

            final int optIdx = i;
            rb.setOnAction(e -> {
                userAnswers.put(currentQIndex, optIdx);
                displayQuestion();
                refreshPalette();
            });
            optionsContainer.getChildren().add(rb);
        }
        refreshPalette();
    }

    private void refreshPalette() {
        palettePane.getChildren().clear();
        for (int i = 0; i < quizQuestions.size(); i++) {
            Button pb = new Button(String.valueOf(i + 1));
            pb.setPrefSize(34, 34);
            final int idx = i;
            pb.setOnAction(e -> {
                currentQIndex = idx;
                displayQuestion();
            });

            if (i == currentQIndex) {
                pb.setStyle("-fx-background-color: #6366F1; -fx-text-fill: #FFF; -fx-font-weight: 800; -fx-background-radius: 8; -fx-border-color: #818CF8; -fx-border-width: 2; -fx-border-radius: 8; -fx-cursor: hand;");
            } else if (markedForReview.contains(i)) {
                pb.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: #000; -fx-font-weight: 800; -fx-background-radius: 8; -fx-cursor: hand;");
            } else if (userAnswers.containsKey(i)) {
                pb.setStyle("-fx-background-color: #10B981; -fx-text-fill: #FFF; -fx-font-weight: 800; -fx-background-radius: 8; -fx-cursor: hand;");
            } else {
                pb.setStyle("-fx-background-color: #0F172A; -fx-border-color: #334155; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #CBD5E1; -fx-cursor: hand;");
            }
            palettePane.getChildren().add(pb);
        }
    }

    private void finishQuiz() {
        if (quizTimer != null) quizTimer.stop();

        int correctCount = 0;
        int totalQuestions = quizQuestions.size();

        for (int i = 0; i < totalQuestions; i++) {
            Question q = quizQuestions.get(i);
            Integer ans = userAnswers.get(i);
            if (ans != null && ans == q.getCorrectIndex()) {
                correctCount++;
            }
        }

        double accuracy = totalQuestions > 0 ? (correctCount * 100.0 / totalQuestions) : 0;
        int earnedXp = correctCount * 20;

        // Persist attempt to SQLite
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO quiz_attempts (topic, difficulty, score, total_questions, accuracy, xp_earned) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, topicCombo.getValue());
            ps.setString(2, "Standard");
            ps.setInt(3, correctCount);
            ps.setInt(4, totalQuestions);
            ps.setDouble(5, accuracy);
            ps.setInt(6, earnedXp);
            ps.executeUpdate();
            GamificationService.addXp(earnedXp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Render Results
        resultsBox.getChildren().clear();

        Label resTitle = new Label("📊 Quiz Results & Performance Analysis");
        resTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC;");

        HBox scoreRow = new HBox(20);
        scoreRow.getChildren().addAll(
                createStatBox("FINAL SCORE", correctCount + " / " + totalQuestions, "#10B981"),
                createStatBox("ACCURACY", String.format("%.1f%%", accuracy), "#38BDF8"),
                createStatBox("XP EARNED", "+" + earnedXp + " XP", "#F59E0B")
        );

        Button retakeBtn = new Button("🔄 Take Another Quiz");
        retakeBtn.getStyleClass().add("btn-primary");
        retakeBtn.setStyle("-fx-font-size: 13.5px; -fx-padding: 9 20;");
        retakeBtn.setOnAction(e -> showSetup());

        resultsBox.getChildren().addAll(resTitle, scoreRow, retakeBtn);

        quizActiveBox.setVisible(false);
        quizActiveBox.setManaged(false);
        resultsBox.setVisible(true);
        resultsBox.setManaged(true);
    }

    private void showSetup() {
        quizActiveBox.setVisible(false);
        quizActiveBox.setManaged(false);
        resultsBox.setVisible(false);
        resultsBox.setManaged(false);
        quizSetupBox.setVisible(true);
        quizSetupBox.setManaged(true);
    }

    private VBox createStatBox(String label, String value, String color) {
        VBox b = new VBox(6);
        b.getStyleClass().add("card");
        b.setStyle("-fx-border-color: " + color + "; -fx-border-width: 1 1 1 3;");
        Label l = new Label(label);
        l.getStyleClass().add("stat-title");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + color + ";");
        b.getChildren().addAll(l, v);
        return b;
    }
}
