package edu.curtin.saed.assignment1;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class ScoreUpdateRunnable implements Runnable {
    private ScoreManager scoreManager;
    private Label label;

    public ScoreUpdateRunnable(ScoreManager scoreManager, Label label) {
        this.scoreManager = scoreManager;
        this.label = label;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000); // Sleep for 1 second
                scoreManager.incrementScore(10); // Increase the score by 10
                Platform.runLater(() -> label.setText("Score: " + scoreManager.getScore()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }
        }
    }
}
