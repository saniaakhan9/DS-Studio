package com.dsstudio.ui.views;

import com.dsstudio.model.Question;
import com.dsstudio.repository.QuestionRepository;
import com.dsstudio.service.GamificationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class DSQuestView extends VBox {
    private final ComboBox<String> categoryCombo = new ComboBox<>();
    private final ComboBox<String> topicCombo = new ComboBox<>();
    private final Label questTitleLabel = new Label("🎮 DS Quest — Mission Control");
    private final Label scoreFeedbackLabel = new Label("");
    private final VBox questionCard = new VBox(16);
    private final VBox explanationCard = new VBox(12);

    private List<Question> activeQuestions;
    private int currentIndex = 0;
    private boolean answered = false;

    public DSQuestView() {
        this.setSpacing(16);
        this.setPadding(new Insets(20));
        this.setFillWidth(true);

        setupHeader();
        setupQuestionArea();
        loadQuestions();
    }

    private void setupHeader() {
        VBox headerCard = new VBox(12);
        headerCard.getStyleClass().addAll("card", "card-accent");

        HBox topRow = new HBox(14);
        topRow.setAlignment(Pos.CENTER_LEFT);

        questTitleLabel.setStyle("-fx-font-size: 19px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        categoryCombo.getItems().addAll(
                "All Quests",
                "Concept Quest",
                "Output Quest",
                "Debug Quest",
                "Dry Run Quest",
                "Complexity Quest"
        );
        categoryCombo.setValue("All Quests");
        categoryCombo.setOnAction(e -> loadQuestions());

        topicCombo.getItems().addAll("ALL", "Arrays", "Stack", "Queue", "Singly Linked List", "Algorithms");
        topicCombo.setValue("ALL");
        topicCombo.setOnAction(e -> loadQuestions());

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        scoreFeedbackLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: #10B981;");

        topRow.getChildren().addAll(questTitleLabel, sp, new Label("Category:"), categoryCombo, new Label("Topic:"), topicCombo, scoreFeedbackLabel);

        Label sub = new Label("Solve curated computer science challenges, debug Java code, and level up your data structure mastery!");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #94A3B8;");

        headerCard.getChildren().addAll(topRow, sub);
        this.getChildren().add(headerCard);
    }

    private void setupQuestionArea() {
        questionCard.getStyleClass().add("card");
        questionCard.setPadding(new Insets(22));

        explanationCard.getStyleClass().add("card");
        explanationCard.setPadding(new Insets(18));
        explanationCard.setVisible(false);
        explanationCard.setManaged(false);

        this.getChildren().addAll(questionCard, explanationCard);
    }

    private void loadQuestions() {
        String topic = topicCombo.getValue();
        String cat = categoryCombo.getValue();

        List<Question> list = QuestionRepository.getByTopic(topic);
        if (!cat.equals("All Quests")) {
            String filterCat = cat.replace(" Quest", "").toUpperCase().replace(" ", "_");
            list = list.stream().filter(q -> q.getCategory().equalsIgnoreCase(filterCat)).toList();
        }

        if (list.isEmpty()) {
            list = QuestionRepository.getAll();
        }

        activeQuestions = list;
        currentIndex = 0;
        displayQuestion();
    }

    private void displayQuestion() {
        questionCard.getChildren().clear();
        explanationCard.setVisible(false);
        explanationCard.setManaged(false);
        answered = false;
        scoreFeedbackLabel.setText("");

        if (activeQuestions == null || activeQuestions.isEmpty() || currentIndex >= activeQuestions.size()) {
            Label done = new Label("🎉 All quests in this section completed!");
            done.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #38BDF8;");
            questionCard.getChildren().add(done);
            return;
        }

        Question q = activeQuestions.get(currentIndex);

        HBox metaRow = new HBox(10);
        metaRow.setAlignment(Pos.CENTER_LEFT);

        Label qNum = new Label("Quest " + (currentIndex + 1) + " of " + activeQuestions.size());
        qNum.setStyle("-fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-font-size: 13px;");

        Label diffBadge = new Label(q.getDifficulty());
        diffBadge.getStyleClass().add("badge-cyan");

        Label topicBadge = new Label(q.getTopic());
        topicBadge.getStyleClass().add("badge-indigo");

        Label xpBadge = new Label("+" + q.getXpReward() + " XP");
        xpBadge.getStyleClass().add("badge-emerald");

        metaRow.getChildren().addAll(qNum, diffBadge, topicBadge, xpBadge);

        Label qText = new Label(q.getQuestionText());
        qText.setStyle("-fx-font-size: 15.5px; -fx-font-weight: 700; -fx-text-fill: #F8FAFC; -fx-line-spacing: 2px;");
        qText.setWrapText(true);

        questionCard.getChildren().addAll(metaRow, qText);

        if (q.getCodeSnippet() != null && !q.getCodeSnippet().isEmpty()) {
            TextArea codeSnippet = new TextArea(q.getCodeSnippet());
            codeSnippet.setEditable(false);
            codeSnippet.setPrefRowCount(Math.min(6, q.getCodeSnippet().split("\n").length));
            codeSnippet.getStyleClass().add("code-panel");
            questionCard.getChildren().add(codeSnippet);
        }

        // Options Box with Card-like Radio Items
        VBox optionsBox = new VBox(10);
        ToggleGroup group = new ToggleGroup();

        for (int i = 0; i < q.getOptions().size(); i++) {
            RadioButton rb = new RadioButton(q.getOptions().get(i));
            rb.setToggleGroup(group);
            rb.setUserData(i);
            rb.setMaxWidth(Double.MAX_VALUE);
            rb.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 13.5px; -fx-padding: 10 14; -fx-background-color: #0F172A; -fx-border-color: #334155; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            
            // Highlight container when selected
            final RadioButton currentRb = rb;
            currentRb.selectedProperty().addListener((obs, oldV, newV) -> {
                if (newV) {
                    currentRb.setStyle("-fx-text-fill: #A5B4FC; -fx-font-size: 13.5px; -fx-font-weight: 700; -fx-padding: 10 14; -fx-background-color: #1E1B4B; -fx-border-color: #6366F1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
                } else {
                    currentRb.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 13.5px; -fx-padding: 10 14; -fx-background-color: #0F172A; -fx-border-color: #334155; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
                }
            });

            optionsBox.getChildren().add(rb);
        }

        HBox btnRow = new HBox();
        btnRow.setAlignment(Pos.CENTER_LEFT);

        Button submitBtn = new Button("Submit Answer");
        submitBtn.getStyleClass().add("btn-primary");
        submitBtn.setStyle("-fx-font-size: 13.5px; -fx-padding: 9 22;");
        submitBtn.setOnAction(e -> {
            if (answered) {
                // Next question
                currentIndex++;
                displayQuestion();
                return;
            }

            Toggle selected = group.getSelectedToggle();
            if (selected == null) {
                scoreFeedbackLabel.setText("⚠️ Please select an answer first.");
                return;
            }

            int selectedIndex = (int) selected.getUserData();
            boolean correct = (selectedIndex == q.getCorrectIndex());
            answered = true;

            if (correct) {
                scoreFeedbackLabel.setText("🎯 Correct! +" + q.getXpReward() + " XP Awarded!");
                scoreFeedbackLabel.setStyle("-fx-text-fill: #10B981; -fx-font-size: 13px; -fx-font-weight: 800;");
                GamificationService.addXp(q.getXpReward());
            } else {
                scoreFeedbackLabel.setText("❌ Incorrect. Study the explanation below.");
                scoreFeedbackLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 13px; -fx-font-weight: 800;");
            }

            // Show explanation
            showExplanation(q, correct);
            submitBtn.setText("Next Quest ➜");
        });

        btnRow.getChildren().add(submitBtn);
        questionCard.getChildren().addAll(optionsBox, btnRow);
    }

    private void showExplanation(Question q, boolean correct) {
        explanationCard.getChildren().clear();
        explanationCard.setVisible(true);
        explanationCard.setManaged(true);

        if (correct) {
            explanationCard.setStyle("-fx-background-color: #064E3B; -fx-border-color: #10B981; -fx-border-radius: 12; -fx-background-radius: 12;");
        } else {
            explanationCard.setStyle("-fx-background-color: #1E293B; -fx-border-color: #F59E0B; -fx-border-radius: 12; -fx-background-radius: 12;");
        }

        Label status = new Label(correct ? "✅ Great job! Concept Verified" : "💡 Solution & Academic Explanation");
        status.setStyle("-fx-font-size: 14.5px; -fx-font-weight: 800; -fx-text-fill: " + (correct ? "#34D399" : "#FBBF24") + ";");

        Label correctAns = new Label("Correct Answer: " + q.getOptions().get(q.getCorrectIndex()));
        correctAns.setStyle("-fx-font-weight: 800; -fx-text-fill: #38BDF8; -fx-font-size: 13px;");

        Label expText = new Label(q.getExplanation());
        expText.setStyle("-fx-text-fill: #E2E8F0; -fx-font-size: 13px; -fx-line-spacing: 2px;");
        expText.setWrapText(true);

        explanationCard.getChildren().addAll(status, correctAns, expText);
    }
}
