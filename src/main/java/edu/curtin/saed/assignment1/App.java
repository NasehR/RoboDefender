package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application
{
    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("RoboDefender");
        ExecutorService threadPool = Executors.newFixedThreadPool(15);
        JFXArena arena = new JFXArena();
        TextArea logger = new TextArea();
        ToolBar toolbar = new ToolBar();
        Citadel citadel = new Citadel();
        ScoreManager scoreManager = new ScoreManager();
        RobotManager manager = new RobotManager(arena, threadPool, logger);
        WallBuilder builder = new WallBuilder(arena, threadPool, logger);
        Label label = new Label("Score: " + scoreManager.getScore());
        ScoreUpdateRunnable scoreUpdateRunnable = new ScoreUpdateRunnable(scoreManager, label);

        arena.setOnSquareClicked((x, y) -> {
            try {
                // Create a wall at the clicked square and add it to the WallBuilder
                if (!(x == 4 && y == 4)) {
                    Wall wall = new Wall(x, y); // You need to provide the appropriate constructor
                    builder.addWallToQueue(wall);
                }
            }
            catch (InterruptedException e) {
            }
        });

        logger.appendText("Welcome to RoboDefender\n");
        toolbar.getItems().addAll(label);
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);
        arena.addCitadel(citadel);
        arena.addScoreManager(scoreManager);
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();

        manager.run();
        builder.run();
        threadPool.submit(scoreUpdateRunnable);

        stage.setOnCloseRequest(event -> {
            threadPool.shutdownNow();
            Platform.exit();
        });
    }
}
