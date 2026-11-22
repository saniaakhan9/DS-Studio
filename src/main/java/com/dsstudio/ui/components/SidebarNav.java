package com.dsstudio.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SidebarNav extends VBox {
    private final Consumer<String> onNavigate;
    private final Map<String, Button> navButtons = new LinkedHashMap<>();
    private String activeKey = "Dashboard";

    public SidebarNav(Consumer<String> onNavigate) {
        this.onNavigate = onNavigate;
        this.getStyleClass().add("sidebar");
        this.setPrefWidth(240);
        this.setMinWidth(220);
        this.setMaxWidth(260);
        this.setSpacing(6);

        // Logo / Title Header
        VBox logoBox = new VBox(4);
        logoBox.setPadding(new Insets(10, 12, 16, 12));
        Label logoTitle = new Label("⚡ DS STUDIO");
        logoTitle.setStyle("-fx-font-size: 19px; -fx-font-weight: 900; -fx-text-fill: #38BDF8; -fx-letter-spacing: 0.8px;");
        Label logoSub = new Label("Visual Lab & Gamified Practice");
        logoSub.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748B; -fx-font-weight: 700;");
        logoBox.getChildren().addAll(logoTitle, logoSub);

        VBox menuContainer = new VBox(3);
        menuContainer.setFillWidth(true);

        // Core Learning
        addSectionHeader(menuContainer, "LEARNING & VISUALS");
        addNavItem(menuContainer, "Dashboard", "📊  Dashboard");
        addNavItem(menuContainer, "Learn", "📖  Learn Curriculum");
        addNavItem(menuContainer, "Visualizer Lab", "🔬  Visualizer Lab");
        addNavItem(menuContainer, "Java Code Lab", "💻  Java Code Lab");
        addNavItem(menuContainer, "Algorithms", "⚡  Algorithms");

        // Gamification & Testing
        addSectionHeader(menuContainer, "PRACTICE & QUESTS");
        addNavItem(menuContainer, "DS Quest", "🎮  DS Quest");
        addNavItem(menuContainer, "Practice Centre", "📝  Practice Centre");
        addNavItem(menuContainer, "Quiz Centre", "🏆  Quiz Centre");

        // References & Progress
        addSectionHeader(menuContainer, "ANALYTICS & SPECS");
        addNavItem(menuContainer, "Complexity Guide", "📈  Complexity Guide");
        addNavItem(menuContainer, "Bookmarks", "🔖  Bookmarks");
        addNavItem(menuContainer, "My Progress", "🌟  My Progress");
        addNavItem(menuContainer, "Settings", "⚙️  Settings");
        addNavItem(menuContainer, "About DS Studio", "ℹ️  About DS Studio");

        ScrollPane scrollPane = new ScrollPane(menuContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.getChildren().addAll(logoBox, scrollPane);
        setActive("Dashboard");
    }

    private void addSectionHeader(VBox container, String title) {
        Label header = new Label(title);
        header.getStyleClass().add("nav-section-header");
        container.getChildren().add(header);
    }

    private void addNavItem(VBox container, String key, String labelText) {
        Button btn = new Button(labelText);
        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.BASELINE_LEFT);
        btn.setOnAction(e -> {
            setActive(key);
            if (onNavigate != null) {
                onNavigate.accept(key);
            }
        });
        navButtons.put(key, btn);
        container.getChildren().add(btn);
    }

    public void setActive(String key) {
        this.activeKey = key;
        navButtons.forEach((k, btn) -> {
            btn.getStyleClass().remove("nav-button-active");
            if (k.equals(key)) {
                btn.getStyleClass().add("nav-button-active");
            }
        });
    }

    public String getActiveKey() {
        return activeKey;
    }
}
