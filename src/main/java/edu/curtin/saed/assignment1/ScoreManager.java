package edu.curtin.saed.assignment1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScoreManager {
    private int score;
    private ExecutorService scoreIncrementExecutor;

    public ScoreManager(ExecutorService threadPool) {
        this.score = 0;
        this.scoreIncrementExecutor = threadPool;
    }

    public void startScoreIncrementTask() {
        // Schedule a task to increment the score by 10 points every second
        scoreIncrementExecutor.execute(() -> {
            try {
                incrementScore(10);
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {}
        });
    }

    public void incrementScore(int points) {
        // Increment the score
        score += points;
        System.out.println("Score: " + score); // You can replace this with your desired action
    }

    public void incrementScoreOnRobotDestroyed() {
        // Increment the score by 100 points when a robot is destroyed
        incrementScore(100);
    }

    public int getScore() {
        return score;
    }
}

