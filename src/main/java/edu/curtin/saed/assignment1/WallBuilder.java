package edu.curtin.saed.assignment1;

import javafx.application.Platform;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WallBuilder {
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final int MAX_WALLS = 10;
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final long BUILD_DELAY = 2000; // 2000 milliseconds
    private final JFXArena arena;
    private final BlockingQueue<Wall> buildQueue;
    private Thread addingWallThread = null;
    private final Object mutex;

    public WallBuilder(JFXArena arena) {
        this.arena = arena;
        buildQueue = new ArrayBlockingQueue<>(MAX_WALLS);
        mutex = new Object();
    }

    public void run () {
        Runnable addWallTask = () -> {
            try {
                while (true) {
                    synchronized (mutex) {
                        Wall wall = buildQueue.take();
                        Platform.runLater(() -> {
                            arena.addWall(wall);
                            arena.coordinateOccupied(wall);
                            System.out.println("Plot wall.\n");
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

        addingWallThread = new Thread(addWallTask, "Adding Wall");
        addingWallThread.start();
    }

    public void stop() {
        if (addingWallThread != null) {
            addingWallThread.interrupt();
        }
    }

    public void addWallToQueue(Wall wall) throws InterruptedException {
        if (buildQueue.size() < MAX_WALLS) {
            buildQueue.put(wall); // Add the wall to the queue
        }
    }
}
