package edu.curtin.saed.assignment1;

import java.util.concurrent.*;

public class RobotManager implements Runnable {
    private JFXArena arena;
    private ExecutorService threadPool;

    public RobotManager (JFXArena arena, ExecutorService threadPool) {
        this.arena = arena;
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
            }
            catch (InterruptedException e) {

            }
        };
        return robotSpawnTask;
    }
}
