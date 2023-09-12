package edu.curtin.saed.assignment1;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class WallBuilder {
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final int MAX_WALLS = 10;
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final long BUILD_DELAY = 2000; // 2000 milliseconds
    private final JFXArena arena;
    private final BlockingQueue<Wall> buildQueue;
    private final Object mutex;
    private ExecutorService threadPool;
    private TextArea logger;
    private Label wallLabel;

    public WallBuilder(JFXArena arena, ExecutorService threadPool, TextArea logger,  Label wallLabel) {
        this.arena = arena;
        buildQueue = new ArrayBlockingQueue<>(MAX_WALLS);
        mutex = new Object();
        this.threadPool = threadPool;
        this.logger = logger;
        this.wallLabel = wallLabel;
    }

    public void run () {
        Runnable addWallTask = () -> {
            try {
                while (true) {
//                    int wallsBuilt = arena.numberOfWalls();
                    synchronized (mutex) {
                        Wall wall = buildQueue.take();
                        int wallsToBeBuilt = buildQueue.size();
                        Platform.runLater(() -> {

//                            int wallsLeftToBuild = arena.numberOfWalls() - buildQueue.size();
                            wallLabel.setText("\t\t\tWalls in the queue: " + wallsToBeBuilt);
                            arena.addWall(wall);
                            int xPosition = (int) wall.getXPos() + 1;
                            int yPosition = (int) wall.getYPos() + 1;
                            logger.appendText("Wall built at position (" + xPosition + "," + yPosition + ")\n");
                            arena.coordinateOccupied(wall);
                            arena.layoutChildren();
                        });
                    }
                    Thread.sleep(BUILD_DELAY);
                }
            }
            catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        };

        threadPool.submit(addWallTask);
    }

    public void addWallToQueue(Wall wall) throws InterruptedException {
        if (buildQueue.size() < MAX_WALLS) {
            buildQueue.put(wall); // Add the wall to the queue
        }
    }
}
