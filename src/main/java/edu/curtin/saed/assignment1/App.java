package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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
        JFXArena arena = new JFXArena();
        TextArea logger = new TextArea();
        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("My Button 1");
        Button btn2 = new Button("My Button 2");
        Label label = new Label("Score: 999");
        RobotManager manager = new RobotManager(arena);
        WallBuilder builder = new WallBuilder(arena);

        arena.addListener((x, y) ->
//                System.out.println("Arena click at (" + x + "," + y + ")")
                logger.appendText("Arena click at (" + x + "," + y + ")\n")
        );

        arena.setOnSquareClicked((x, y) -> {
            try {
                // Create a wall at the clicked square and add it to the WallBuilder
                Wall wall = new Wall(x, y); // You need to provide the appropriate constructor
                builder.addWallToQueue(wall);
                logger.appendText("Add wall to the queue. \n");
            }
            catch (InterruptedException e) {
            }
        });

        toolbar.getItems().addAll(btn1, btn2, label);

        btn1.setOnAction((event) ->
        {
            System.out.println("Button 1 pressed");
            logger.appendText("Button 1 pressed\n");
        });

        btn2.setOnAction((event) ->
        {
            System.out.println("Button 2 pressed");
            logger.appendText("Button 2 pressed\n");
        });

        logger.appendText("Welcome to RoboDefender\n");

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);


        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();


        manager.run();
        builder.run();

        stage.setOnCloseRequest(event -> {
            manager.shutdown();
            builder.stop();
            Platform.exit();
        });
    }
}
