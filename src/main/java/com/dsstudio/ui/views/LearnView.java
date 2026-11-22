package com.dsstudio.ui.views;

import com.dsstudio.service.GamificationService;
import com.dsstudio.service.LessonService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.function.Consumer;

public class LearnView extends BorderPane {
    private final Consumer<String> onNavigate;
    private final ListView<String> topicList = new ListView<>();
    private final VBox lessonContentBox = new VBox(20);

    public LearnView(Consumer<String> onNavigate) {
        this.onNavigate = onNavigate;
        this.setPadding(new Insets(16));

        // Left topic list
        VBox leftNav = new VBox(10);
        leftNav.setPrefWidth(240);
        leftNav.getStyleClass().add("card");
        leftNav.setPadding(new Insets(16));

        Label navTitle = new Label("📚 Curriculum Topics");
        navTitle.setStyle("-fx-font-size: 14.5px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        List<LessonService.LessonData> allLessons = LessonService.getAllLessons();
        for (LessonService.LessonData l : allLessons) {
            topicList.getItems().add(l.title);
        }
        topicList.getStyleClass().add("list-view");
        topicList.getSelectionModel().select(0);
        topicList.getSelectionModel().selectedIndexProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                renderLesson(allLessons.get(newV.intValue()));
            }
        });
        VBox.setVgrow(topicList, Priority.ALWAYS);

        leftNav.getChildren().addAll(navTitle, topicList);
        this.setLeft(leftNav);

        // Main content area
        ScrollPane scrollPane = new ScrollPane(lessonContentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        BorderPane.setMargin(scrollPane, new Insets(0, 0, 0, 16));
        this.setCenter(scrollPane);

        renderLesson(allLessons.get(0));
    }

    private void renderLesson(LessonService.LessonData lesson) {
        lessonContentBox.getChildren().clear();
        lessonContentBox.setPadding(new Insets(0, 10, 30, 0));

        // Header Card
        VBox headerCard = new VBox(12);
        headerCard.getStyleClass().addAll("card", "card-accent");
        headerCard.setPadding(new Insets(22));

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(lesson.title);
        title.setStyle("-fx-font-size: 21px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC; -fx-letter-spacing: -0.3px;");

        Label catBadge = new Label(lesson.category);
        catBadge.getStyleClass().add("badge-cyan");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button openVisualizerBtn = new Button("🔬 Open in Visualizer Lab");
        openVisualizerBtn.getStyleClass().add("btn-primary");
        openVisualizerBtn.setOnAction(e -> onNavigate.accept("Visualizer Lab"));

        Button bookmarkBtn = new Button("🔖 Bookmark");
        bookmarkBtn.getStyleClass().add("btn-secondary");
        bookmarkBtn.setOnAction(e -> {
            bookmarkBtn.setText("✅ Bookmarked");
            GamificationService.addXp(10);
        });

        topRow.getChildren().addAll(title, catBadge, sp, openVisualizerBtn, bookmarkBtn);

        Label overview = new Label(lesson.overview);
        overview.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #CBD5E1; -fx-line-spacing: 2px;");
        overview.setWrapText(true);

        headerCard.getChildren().addAll(topRow, overview);

        // Formal Definition
        VBox defCard = createSectionCard("1. Academic & Formal CS Definition", lesson.formalDefinition);
        VBox memCard = createSectionCard("2. Memory Representation & Cache Model", lesson.memoryModel);
        VBox usageCard = createSectionCard("3. Practical Software Engineering Applications", lesson.realWorldUsage);

        // Complexities Card
        VBox compCard = new VBox(12);
        compCard.getStyleClass().add("card");
        compCard.setPadding(new Insets(20));

        Label compTitle = new Label("4. Time & Space Complexity Analysis");
        compTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        GridPane compGrid = new GridPane();
        compGrid.setHgap(24);
        compGrid.setVgap(10);
        int row = 0;
        for (var entry : lesson.complexities.entrySet()) {
            Label k = new Label(entry.getKey() + ":");
            k.setStyle("-fx-text-fill: #94A3B8; -fx-font-weight: 700; -fx-font-size: 13px;");
            Label v = new Label(entry.getValue());
            v.setStyle("-fx-text-fill: #38BDF8; -fx-font-weight: 800; -fx-font-size: 13px;");
            compGrid.add(k, 0, row);
            compGrid.add(v, 1, row);
            row++;
        }
        compCard.getChildren().addAll(compTitle, compGrid);

        // Real Java Code Panel
        VBox codeCard = new VBox(10);
        codeCard.getStyleClass().add("card");
        codeCard.setPadding(new Insets(20));

        Label codeTitle = new Label("5. Java Implementation Pattern");
        codeTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        TextArea codeArea = new TextArea(lesson.javaCodeSnippet);
        codeArea.setEditable(false);
        codeArea.setPrefRowCount(8);
        codeArea.getStyleClass().add("code-panel");
        codeCard.getChildren().addAll(codeTitle, codeArea);

        // Common Pitfalls
        VBox pitCard = new VBox(10);
        pitCard.getStyleClass().add("card");
        pitCard.setPadding(new Insets(20));

        Label pitTitle = new Label("6. Common Implementation Errors & Edge Cases");
        pitTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
        pitCard.getChildren().add(pitTitle);
        for (String pit : lesson.commonPitfalls) {
            Label pl = new Label("⚠️ " + pit);
            pl.setStyle("-fx-text-fill: #FDA4AF; -fx-font-size: 13px; -fx-line-spacing: 2px; -fx-padding: 3 0;");
            pl.setWrapText(true);
            pitCard.getChildren().add(pl);
        }

        // Viva Questions
        VBox vivaCard = new VBox(10);
        vivaCard.getStyleClass().add("card");
        vivaCard.setPadding(new Insets(20));

        Label vivaTitle = new Label("7. MCA / University Viva Examination Questions");
        vivaTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
        vivaCard.getChildren().add(vivaTitle);
        for (String vq : lesson.vivaQuestions) {
            Label vl = new Label("🎓 " + vq);
            vl.setStyle("-fx-text-fill: #A7F3D0; -fx-font-size: 13px; -fx-padding: 3 0; -fx-line-spacing: 2px;");
            vl.setWrapText(true);
            vivaCard.getChildren().add(vl);
        }

        lessonContentBox.getChildren().addAll(
                headerCard, defCard, memCard, usageCard, compCard, codeCard, pitCard, vivaCard
        );
    }

    private VBox createSectionCard(String title, String body) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(20));
        Label t = new Label(title);
        t.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");
        Label b = new Label(body);
        b.setStyle("-fx-font-size: 13px; -fx-text-fill: #CBD5E1; -fx-line-spacing: 2px;");
        b.setWrapText(true);
        card.getChildren().addAll(t, b);
        return card;
    }
}
