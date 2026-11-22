package com.dsstudio.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class BookmarksView extends ScrollPane {
    private final Consumer<String> onNavigate;

    public BookmarksView(Consumer<String> onNavigate) {
        this.onNavigate = onNavigate;
        this.setFitToWidth(true);
        this.getStyleClass().add("scroll-pane");

        VBox root = new VBox(20);
        root.setPadding(new Insets(24));

        VBox headerCard = new VBox(8);
        headerCard.getStyleClass().addAll("card", "card-accent");
        headerCard.setPadding(new Insets(22));

        Label title = new Label("🔖 Saved Bookmarks & Quick Revisions");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #F8FAFC;");

        Label desc = new Label("Quick access to your pinned lessons, challenging concepts, and examination algorithms.");
        desc.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #94A3B8;");
        headerCard.getChildren().addAll(title, desc);

        VBox list = new VBox(12);

        list.getChildren().addAll(
                createBookmarkItem("Arrays & Memory Indexing Formula", "Linear Data Structures", "Learn"),
                createBookmarkItem("Circular Queue Modulo Indexing Mechanics", "Queues", "Visualizer Lab"),
                createBookmarkItem("Singly Linked List Cycle Detection (Floyd)", "Linked Lists", "Learn"),
                createBookmarkItem("Bubble Sort Early-Break Optimisation", "Algorithms", "Algorithms"),
                createBookmarkItem("Stack LIFO & System Call Stack In-depth", "Linear Data Structures", "Visualizer Lab")
        );

        root.getChildren().addAll(headerCard, list);
        this.setContent(root);
    }

    private HBox createBookmarkItem(String name, String tag, String targetView) {
        HBox item = new HBox(14);
        item.getStyleClass().add("card");
        item.setPadding(new Insets(16, 20, 16, 20));
        item.setAlignment(Pos.CENTER_LEFT);

        VBox info = new VBox(5);
        Label n = new Label(name);
        n.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #F8FAFC;");

        Label t = new Label(tag);
        t.getStyleClass().add("badge-cyan");
        info.getChildren().addAll(n, t);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button openBtn = new Button("Open Study View ➜");
        openBtn.getStyleClass().add("btn-primary");
        openBtn.setOnAction(e -> onNavigate.accept(targetView));

        item.getChildren().addAll(info, sp, openBtn);
        return item;
    }
}
