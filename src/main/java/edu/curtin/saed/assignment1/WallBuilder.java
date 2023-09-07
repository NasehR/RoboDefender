package edu.curtin.saed.assignment1;

import javafx.application.Platform;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WallBuilder {
    private final int MAXWALLS = 10;
    private final long BUILDDELAY = 2000; // 2000 milliseconds
    private final JFXArena arena;
    private final BlockingQueue<Wall> buildQueue;
    private Thread addingWallThread = null;
    private Thread displayingWallThread = null;
    private final Object mutex;

    public WallBuilder(JFXArena arena) {
        this.arena = arena;
        buildQueue = new ArrayBlockingQueue<>(MAXWALLS);
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
                            System.out.println("Plot wall.\n");
                            arena.layoutChildren();
                        });
                    }
                    Thread.sleep(BUILDDELAY);
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
        if (buildQueue.size() < MAXWALLS) {
            buildQueue.put(wall); // Add the wall to the queue
        }
    }
}
