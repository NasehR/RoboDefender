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
    private volatile boolean stopScoreUpdateThread = false;

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
        Button btn1 = new Button("My Button 1");
        Button btn2 = new Button("My Button 2");
        Citadel citadel = new Citadel();
        ScoreManager scoreManager = new ScoreManager();
        RobotManager manager = new RobotManager(arena, threadPool, logger);
        WallBuilder builder = new WallBuilder(arena, threadPool, logger);
        Label label = new Label("Score: " + scoreManager.getScore());

        arena.setOnSquareClicked((x, y) -> {
            try {
                // Create a wall at the clicked square and add it to the WallBuilder
                Wall wall = new Wall(x, y); // You need to provide the appropriate constructor
                builder.addWallToQueue(wall);
            }
            catch (InterruptedException e) {
            }
        });

        Thread scoreUpdateThread = new Thread(() -> {
            while (!stopScoreUpdateThread) {
                try {
                    // Sleep for 1000 milliseconds (1 second)
                    Thread.sleep(1000);
                    scoreManager.incrementScore(10);
                    // Update the score label concurrently
                    Platform.runLater(() -> label.setText("Score: " + scoreManager.getScore()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start the score update thread
        scoreUpdateThread.setDaemon(true); // Set it as a daemon thread so it doesn't block the application shutdown
        scoreUpdateThread.start();


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

        stage.setOnCloseRequest(event -> {
            stopScoreUpdateThread = true;
            threadPool.shutdownNow();
            Platform.exit();
        });
    }
}
