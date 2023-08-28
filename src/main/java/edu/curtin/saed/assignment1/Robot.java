package edu.curtin.saed.assignment1;

import java.util.Random;

public class Robot {
    private int id;
    private int delay;
    private double startRow;
    private double startColumn;

    public Robot (int id, double startRow, double startColumn) {
        this.id = id;
        this.startRow = startRow;
        this.startColumn = startColumn;
        delay = delay();

    }

    public int delay() {
        Random random = new Random();

        // Define the range (2000 - 500 + 1)
        int minRange = 500;
        int maxRange = 2000;
        int range = maxRange - minRange + 1;

        // Generate a random number within the specified range
        return random.nextInt(range) + minRange;
    }
}
