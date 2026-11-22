package com.dsstudio.ui.views;

import com.dsstudio.model.Question;
import com.dsstudio.repository.QuestionRepository;
import com.dsstudio.service.GamificationService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class PracticeCenterView extends VBox {
    private final ComboBox<String> topicFilter = new ComboBox<>();
    private final VBox questionsContainer = new VBox(16);

    public PracticeCenterView() {
        this.setSpacing(16);
        this.setPadding(new Insets(20));
        this.setFillWidth(true);

        VBox headerCard = new VBox(10);
        headerCard.getStyleClass().addAll("card", "card-accent");

        HBox topRow = new HBox(14);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("📝 Practice Centre");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC;");

        topicFilter.getItems().addAll("ALL", "Arrays", "Stack", "Queue", "Singly Linked List", "Algorithms");
        topicFilter.setValue("ALL");
        topicFilter.setOnAction(e -> refreshQuestions());

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        topRow.getChildren().addAll(title, sp, new Label("Filter Topic:"), topicFilter);
        headerCard.getChildren().add(topRow);

        ScrollPane scrollPane = new ScrollPane(questionsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.getChildren().addAll(headerCard, scrollPane);
        refreshQuestions();
    }

    private void refreshQuestions() {
        questionsContainer.getChildren().clear();
        String sel = topicFilter.getValue();
        List<Question> list = QuestionRepository.getByTopic(sel);

        for (Question q : list) {
            VBox card = new VBox(12);
            card.getStyleClass().add("card");
            card.setPadding(new Insets(18));

            HBox meta = new HBox(8);
            meta.setAlignment(Pos.CENTER_LEFT);
            Label t = new Label(q.getTopic());
            t.getStyleClass().add("badge-cyan");
            Label d = new Label(q.getDifficulty());
            d.getStyleClass().add("badge-indigo");
            meta.getChildren().addAll(t, d);

            Label txt = new Label(q.getQuestionText());
            txt.setStyle("-fx-font-weight: 700; -fx-font-size: 14.5px; -fx-text-fill: #F8FAFC; -fx-line-spacing: 2px;");
            txt.setWrapText(true);

            VBox expBox = new VBox(8);
            expBox.setStyle("-fx-background-color: #064E3B; -fx-border-color: #10B981; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12 16;");
            expBox.setVisible(false);
            expBox.setManaged(false);

            Label ansTitle = new Label("💡 Verified Answer: " + q.getOptions().get(q.getCorrectIndex()));
            ansTitle.setStyle("-fx-font-weight: 800; -fx-text-fill: #34D399; -fx-font-size: 13.5px;");

            Label expDesc = new Label(q.getExplanation());
            expDesc.setStyle("-fx-text-fill: #D1FAE5; -fx-font-size: 13px; -fx-line-spacing: 2px;");
            expDesc.setWrapText(true);

            expBox.getChildren().addAll(ansTitle, expDesc);

            Button revealBtn = new Button("👁 Show Solution & Logic (+5 XP)");
            revealBtn.getStyleClass().add("btn-secondary");
            revealBtn.setOnAction(e -> {
                expBox.setVisible(true);
                expBox.setManaged(true);
                revealBtn.setDisable(true);
                revealBtn.setText("✅ Solution Revealed");
                GamificationService.addXp(5);
            });

            card.getChildren().addAll(meta, txt, revealBtn, expBox);
            questionsContainer.getChildren().add(card);
        }
    }
}
