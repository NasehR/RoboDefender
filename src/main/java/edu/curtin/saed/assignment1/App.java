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

    private ExecutorService threadPool;
    private JFXArena arena;
    private TextArea logger;
    private Citadel citadel;
    private ScoreManager scoreManager;
    private RobotManager manager;
    private WallBuilder builder;
    private Label scoreLabel;
    private Label wallLabel;
    private ScoreUpdateRunnable scoreUpdateRunnable;
    private ToolBar toolbar;
    private Button restartButton;
    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("RoboDefender");
        threadPool = Executors.newFixedThreadPool(15);
        arena = new JFXArena();
        logger = new TextArea();
        citadel = new Citadel();
        scoreManager = new ScoreManager();
        manager = new RobotManager(arena, threadPool, logger);
        builder = new WallBuilder(arena, threadPool, logger);
        scoreLabel = new Label("Score: " + scoreManager.getScore());
        wallLabel = new Label("\t\t\tWalls in the queue: " + arena.numberOfWalls());
        scoreUpdateRunnable = new ScoreUpdateRunnable(scoreManager, scoreLabel);
        toolbar = new ToolBar();
        restartButton = new Button("Restart Game");

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

        logger.appendText("Welcome to RoboDefender\n\n");
        toolbar.getItems().addAll(scoreLabel, wallLabel);
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
