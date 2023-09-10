package edu.curtin.saed.assignment1;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScoreManager {
    private int score;
    private ScheduledExecutorService scoreIncrementExecutor;;

    public ScoreManager() {
        this.score = 0;
        this.scoreIncrementExecutor = Executors.newScheduledThreadPool(1);
    }

    public void startScoreIncrementTask() {
        // Schedule a task to increment the score by 10 points every second
        scoreIncrementExecutor.scheduleAtFixedRate(() -> incrementScore(10), 1, 1, TimeUnit.SECONDS);
    }

    public void stopScoreIncrementTask() {
        // Shutdown the executor when the game ends
        scoreIncrementExecutor.shutdownNow();
    }

    public void incrementScore(int points) {
        // Increment the score
        score += points;
    }

    public void incrementScoreOnRobotDestroyed() {
        // Increment the score by 100 points when a robot is destroyed
        incrementScore(100);
    }

    public int getScore() {
        return score;
    }
}

