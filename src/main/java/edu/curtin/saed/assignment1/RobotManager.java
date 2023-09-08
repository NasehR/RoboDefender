package edu.curtin.saed.assignment1;

import java.util.Objects;
import java.util.concurrent.*;

public class RobotManager implements Runnable {
    private final int NUM_THREADS = 5;

    private JFXArena arena;
    private ExecutorService threadPool;
    private BlockingQueue<RobotSpawner> robots;
    private Object mutex;

    public RobotManager (JFXArena arena, ExecutorService threadPool) {
        mutex = new Object();
        this.arena = arena;
        robots = new ArrayBlockingQueue<>(10);
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        // Task to generate new robots
        Runnable robotSpawnTask = spawnRobots();

        Thread robotSpawnThread = new Thread(robotSpawnTask);
        threadPool.submit(robotSpawnThread);
    }

    private Runnable spawnRobots () {
        Runnable robotSpawnTask = () -> {
            try {
                RobotSpawner robot = new RobotSpawner(arena, threadPool);
                robot.spawnRobots();
                System.out.println("Hello");
            }
            catch (InterruptedException e) {

            }
        };
        return robotSpawnTask;
    }
}
