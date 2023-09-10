package edu.curtin.saed.assignment1;

import javafx.scene.control.TextArea;

import java.util.concurrent.*;

public class RobotManager implements Runnable {
    private JFXArena arena;
    private ExecutorService threadPool;
    private TextArea logger;

    public RobotManager (JFXArena arena, ExecutorService threadPool, TextArea logger) {
        this.arena = arena;
        this.threadPool = threadPool;
        this.logger = logger;
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
                RobotSpawner robot = new RobotSpawner(arena, threadPool, logger);
                robot.spawnRobots();
            }
            catch (InterruptedException e) {

            }
        };
        return robotSpawnTask;
    }
}
