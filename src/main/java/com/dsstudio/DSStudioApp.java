package com.dsstudio;

import com.dsstudio.db.DatabaseManager;
import com.dsstudio.ui.components.HeaderBar;
import com.dsstudio.ui.components.SidebarNav;
import com.dsstudio.ui.views.*;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class DSStudioApp extends Application {
    private BorderPane rootLayout;
    private SidebarNav sidebarNav;
    private HeaderBar headerBar;
    private StackPane contentArea;

    private final Map<String, Node> viewCache = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        // Initialize SQLite Database schema
        try {
            DatabaseManager.getConnection();
        } catch (Exception e) {
            System.err.println("Database initialization warning: " + e.getMessage());
        }

        rootLayout = new BorderPane();
        rootLayout.getStyleClass().add("root");

        // Top Header
        headerBar = new HeaderBar(() -> navigateTo("DS Quest"));
        rootLayout.setTop(headerBar);

        // Sidebar Navigation
        sidebarNav = new SidebarNav(this::navigateTo);
        rootLayout.setLeft(sidebarNav);

        // Main Center Content Stack
        contentArea = new StackPane();
        rootLayout.setCenter(contentArea);

        // Initial view
        navigateTo("Dashboard");

        Scene scene = new Scene(rootLayout, 1280, 800);
        String cssUrl = getClass().getResource("/css/dark-theme.css") != null ? 
                getClass().getResource("/css/dark-theme.css").toExternalForm() : null;
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl);
        }

        primaryStage.setTitle("DS Studio — Interactive Data Structures Learning & Visualization Platform");
        primaryStage.setMinWidth(1080);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void navigateTo(String viewKey) {
        sidebarNav.setActive(viewKey);
        headerBar.setPageTitle(viewKey);
        headerBar.refresh();

        Node view = viewCache.computeIfAbsent(viewKey, this::createView);
        contentArea.getChildren().setAll(view);
    }

    private Node createView(String key) {
        return switch (key) {
            case "Dashboard" -> new DashboardView(this::navigateTo);
            case "Learn" -> new LearnView(this::navigateTo);
            case "Visualizer Lab" -> new VisualizerLabView();
            case "Java Code Lab" -> new CodeLabView();
            case "DS Quest" -> new DSQuestView();
            case "Practice Centre" -> new PracticeCenterView();
            case "Quiz Centre" -> new QuizCenterView();
            case "Algorithms" -> new AlgorithmsView();
            case "Complexity Guide" -> new ComplexityGuideView();
            case "Bookmarks" -> new BookmarksView(this::navigateTo);
            case "My Progress" -> new ProgressView();
            case "Settings" -> new SettingsView();
            case "About DS Studio" -> new AboutView();
            default -> new DashboardView(this::navigateTo);
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
