package edu.curtin.saed.assignment1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WallBuilder {
    private final int MAX_WALLS = 10;
    private final long BUILD_DELAY = 2000; // 2000 milliseconds
    private final JFXArena arena;
    private final BlockingQueue<Wall> buildQueue;
    private Thread addingWallThread = null;
    private Thread displayingWallThread = null;
    private final Object mutex;

    public WallBuilder(JFXArena arena) {
        this.arena = arena;
        buildQueue = new ArrayBlockingQueue<>(MAX_WALLS);
        mutex = new Object();
    }

    public void run () {
        Runnable addWallTask = () -> {
            try {
                do {
                    synchronized (mutex) {
                        Thread.sleep(BUILD_DELAY);
                        Wall wall = buildQueue.take();
                        // plot the wall on the arena.
                        System.out.println("Plot wall.\n");
                    }
                } while (true);
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
