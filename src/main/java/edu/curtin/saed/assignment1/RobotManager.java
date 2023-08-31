package edu.curtin.saed.assignment1;

import java.util.concurrent.*;

public class RobotManager implements Runnable {
    private final int NUM_THREADS = 5;

    private JFXArena arena;
    private ExecutorService robotThreadPool;
    private BlockingQueue<RobotSpawner> robots;

    public RobotManager (JFXArena arena) {
        this.arena = arena;
        robots = new ArrayBlockingQueue<>(10);
        robotThreadPool = Executors.newFixedThreadPool(NUM_THREADS);
    }

    @Override
    public void run() {
        // Task to generate new robots
        Runnable robotSpawnTask = () -> {
             RobotSpawner robot = new RobotSpawner(arena);
             robot.spawnRobots();
            System.out.println("Hello");
        };

        Thread robotSpawnThread = new Thread(robotSpawnTask);
        robotThreadPool.submit(robotSpawnThread);
    }

    public void shutdown() {
        robotThreadPool.shutdown();
    }
}
