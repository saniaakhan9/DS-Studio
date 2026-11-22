package com.dsstudio;

import com.dsstudio.ui.components.HeaderBar;
import com.dsstudio.ui.components.SidebarNav;
import com.dsstudio.ui.views.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class SnapshotGenerator extends Application {

    @Override
    public void start(Stage primaryStage) {
        File dir = new File("d:/ads/screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        BorderPane rootLayout = new BorderPane();
        rootLayout.getStyleClass().add("root");

        HeaderBar headerBar = new HeaderBar(() -> {});
        SidebarNav sidebarNav = new SidebarNav(key -> {});
        StackPane contentArea = new StackPane();

        rootLayout.setTop(headerBar);
        rootLayout.setLeft(sidebarNav);
        rootLayout.setCenter(contentArea);

        Scene scene = new Scene(rootLayout, 1280, 800);
        String cssUrl = getClass().getResource("/css/dark-theme.css") != null ?
                getClass().getResource("/css/dark-theme.css").toExternalForm() : null;
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl);
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("DS Studio Snapshot Generator");
        primaryStage.show();

        Map<String, Node> views = new LinkedHashMap<>();
        views.put("Dashboard", new DashboardView(k -> {}));
        views.put("Visualizer Lab", new VisualizerLabView());
        views.put("Java Code Lab", new CodeLabView());
        views.put("Algorithms", new AlgorithmsView());
        views.put("DS Quest", new DSQuestView());
        views.put("Quiz Centre", new QuizCenterView());
        views.put("My Progress", new ProgressView());
        views.put("Learn Curriculum", new LearnView(k -> {}));
        views.put("Complexity Guide", new ComplexityGuideView());

        new Thread(() -> {
            try {
                Thread.sleep(600); // Allow initial layout pass
                for (Map.Entry<String, Node> entry : views.entrySet()) {
                    String key = entry.getKey();
                    Node view = entry.getValue();

                    Platform.runLater(() -> {
                        sidebarNav.setActive(key);
                        headerBar.setPageTitle(key);
                        contentArea.getChildren().setAll(view);
                    });

                    Thread.sleep(400); // Wait for CSS render

                    Platform.runLater(() -> {
                        try {
                            WritableImage snapshot = scene.snapshot(null);
                            int width = (int) snapshot.getWidth();
                            int height = (int) snapshot.getHeight();
                            BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                            PixelReader reader = snapshot.getPixelReader();
                            for (int y = 0; y < height; y++) {
                                for (int x = 0; x < width; x++) {
                                    bImage.setRGB(x, y, reader.getArgb(x, y));
                                }
                            }

                            String filename = "d:/ads/screenshots/screenshot_" + 
                                    key.toLowerCase().replace(" ", "_") + ".png";
                            ImageIO.write(bImage, "png", new File(filename));
                            System.out.println("Saved: " + filename);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    Thread.sleep(300);
                }

                System.out.println("ALL_SCREENSHOTS_CAPTURED");
                Platform.runLater(primaryStage::close);
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(primaryStage::close);
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
