package edu.curtin.saed.assignment1;

import javafx.application.Platform;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import static java.lang.Math.*;

public class RobotSpawner {

    private final int TOP_LEFT = 0;
    private final int TOP_RIGHT = 1;
    private final int BOTTOM_LEFT = 2;
    private final int BOTTOM_RIGHT = 3;
    private JFXArena arena;
    private int robotCount;
    private ExecutorService threadPool;
    private final Object lock;

    public RobotSpawner(JFXArena arena, ExecutorService threadPool) {
        this.arena = arena;
        robotCount = 0;
        this.threadPool = threadPool;
        lock = new Object();
    }

    public void spawnRobots() throws InterruptedException {
        for (int i = 0; i < 5; i++)
        {
            Random random = new Random();
            int corner = random.nextInt(4); // Randomly choose a corner (0 to 3)
            double xPos, yPos;

            System.out.println(i + ":" + corner);
            switch (corner) {
                case TOP_LEFT: // Top-left corner
                    if(!arena.isCoordinateOccupied(0, 0)) {
                        xPos = 0.0;
                        yPos = 0.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount));
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        moveRobot(newRobot);
                        arena.coordinateOccupied(newRobot);;
                    }
                    break;
                case TOP_RIGHT: // Top-right corner
                    if(!arena.isCoordinateOccupied(8, 0)) {
                        xPos = 8.0;
                        yPos = 0.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount));
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        moveRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                    }
                    break;
                case BOTTOM_LEFT: // Bottom-left corner
                    if(!arena.isCoordinateOccupied(0, 8)) {
                        xPos = 0.0;
                        yPos = 8.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount));
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        moveRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                    }
                    break;
                case BOTTOM_RIGHT: // Bottom-right corner
                    if(!arena.isCoordinateOccupied(8, 8)) {
                        xPos = 8.0;
                        yPos = 8.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount));
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        moveRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + corner);
            }
            Thread.sleep(1500);
        }
    }

    private void moveRobot(Robot robot) throws InterruptedException {
        Runnable moveRobotTask = () -> {
            try {
                double moveStep = arena.getGridSquareSize() / 500; // Divide the movement into 40 steps
                int targetX = 4; // Target X coordinate (citadel's X coordinate)
                int targetY = 4; // Target Y coordinate (citadel's Y coordinate)

                while (robot.getXPos() != targetX && robot.getYPos() != targetY) {
                    Thread.sleep(robot.getDelay()); // Divide the delay into 40 steps

                    // Calculate the difference between the current position and the target
                    int xDiff = targetX - (int) robot.getXPos();
                    int yDiff = targetY - (int) robot.getYPos();

                    // Determine the direction to move (horizontal or vertical)
                    if (Math.abs(xDiff) >= Math.abs(yDiff)) {
                        // Move horizontally
                        int newX = (int) robot.getXPos() + (xDiff > 0 ? 1 : -1);
                        int newY = (int) robot.getYPos();
                        synchronized (lock) {
                            if (isValidMove(newX, newY)) {
                                robot.setXPosition(robot.getXPos() + (xDiff > 0 ? moveStep : -moveStep));
                            }
                        }
                    } else {
                        // Move vertically
                        int newX = (int) robot.getXPos();
                        int newY = (int) robot.getYPos() + (yDiff > 0 ? 1 : -1);
                        synchronized (lock) {
                            if (isValidMove(newX, newY)) {
                                robot.setYPosition(robot.getYPos() + (yDiff > 0 ? moveStep : -moveStep));
                            }
                        }
                    }

                    // Redraw the arena to show the updated robot position
                    Platform.runLater(() -> arena.layoutChildren());
                }
            }
            catch (InterruptedException e) { }
        };

        threadPool.submit(moveRobotTask);
    }

    private boolean isValidMove(int newX, int newY) {
        return (!arena.isCoordinateOccupied(newX, newY) || arena.isCoordinateOccupiedByWall(newX, newY))
                && newX >= 0 && newX < 9 && newY >= 0 && newY < 9;
    }

}
