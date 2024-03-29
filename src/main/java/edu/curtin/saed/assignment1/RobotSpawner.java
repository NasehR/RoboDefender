package edu.curtin.saed.assignment1;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import static java.lang.Math.*;

public class RobotSpawner {

    @SuppressWarnings("PMD.FieldNamingConventions")
    private final int TOP_LEFT = 0;
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final int TOP_RIGHT = 1;
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final int BOTTOM_LEFT = 2;
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final int BOTTOM_RIGHT = 3;
    private JFXArena arena;
    private int robotCount;
    private ExecutorService threadPool;
    private final Object lock;
    private final Object mutex;
    private final Object mutex2;
    private TextArea logger;

    public RobotSpawner(JFXArena arena, ExecutorService threadPool, TextArea logger) {
        this.arena = arena;
        robotCount = 0;
        this.threadPool = threadPool;
        lock = new Object();
        mutex = new Object();
        mutex2 = new Object();
        this.logger = logger;
    }

    public void spawnRobots() throws InterruptedException {
        for (int i = 0; i < 150; i++)
        {
            Random random = new Random();
            int corner = random.nextInt(4); // Randomly choose a corner (0 to 3)
            double xPos, yPos;

            switch (corner) {
                case TOP_LEFT: // Top-left corner
                    if(!arena.isCoordinateOccupied(0, 0)) {
                        xPos = 0.0;
                        yPos = 0.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount), logger);
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                        newRobot.logCreation();
                        moveRobot(newRobot);
                    }
                    break;
                case TOP_RIGHT: // Top-right corner
                    if(!arena.isCoordinateOccupied(8, 0)) {
                        xPos = 8.0;
                        yPos = 0.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount), logger);
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                        newRobot.logCreation();
                        moveRobot(newRobot);
                    }
                    break;
                case BOTTOM_LEFT: // Bottom-left corner
                    if(!arena.isCoordinateOccupied(0, 8)) {
                        xPos = 0.0;
                        yPos = 8.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount), logger);
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                        newRobot.logCreation();
                        moveRobot(newRobot);
                    }
                    break;
                case BOTTOM_RIGHT: // Bottom-right corner
                    if(!arena.isCoordinateOccupied(8, 8)) {
                        xPos = 8.0;
                        yPos = 8.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount), logger);
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                        newRobot.logCreation();
                        moveRobot(newRobot);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + corner);
            }
            Thread.sleep(1500);
        }
    }

    private void moveRobot(Robot robot) {
        Runnable moveRobotTask = () -> {
            try {
                int targetX = 4; // Target X coordinate (citadel's X coordinate)
                int targetY = 4; // Target Y coordinate (citadel's Y coordinate)

                while (arena.continueGame()) {
                    Thread.sleep(robot.getDelay());

                    // Calculate the difference between the current position and the target
                    int xDiff = targetX - (int) robot.getXPos();
                    int yDiff = targetY - (int) robot.getYPos();

                    int newX, newY;

                    if (abs(xDiff) >= abs(yDiff)) {
                        // Move horizontally
                        newX = (int) robot.getXPos() + (xDiff > 0 ? 1 : -1);
                        newY = (int) robot.getYPos();
                    } else {
                        // Move vertically
                        newX = (int) robot.getXPos();
                        newY = (int) robot.getYPos() + (yDiff > 0 ? 1 : -1);
                    }

                    // Check if the new position is valid
                    if (isValidMove(newX, newY)) {
                        double currentX = robot.getXPos();
                        double currentY = robot.getYPos();

                        // Animate the robot's movement in 10 steps
                        for (int step = 0; step < 10; step++) {
                            synchronized (lock) {
                                Thread.sleep(40); // 40 milliseconds (25 frames per second)
                                double fraction = (double) step / 10.0;
                                double intermediateX = currentX + fraction * (newX - currentX);
                                double intermediateY = currentY + fraction * (newY - currentY);
                                robot.setPosition(intermediateX, intermediateY);

                                // Redraw the arena to show the updated robot position
                                Platform.runLater(() -> {
                                    arena.layoutChildren();
                                });
                            }
                        }

                        if (arena.isCoordinateOccupiedByWall(newX, newY)) {
                            robot.setPosition(newX, newY);
                            arena.clearCoordinate((int) currentX, (int) currentY);
                            handleCollisions(robot);
                        }
                        else if (arena.isCoordinateOccupiedByCitadel(newX, newY)) {
                            robot.setPosition(newX, newY);
                            arena.clearCoordinate((int) currentX, (int) currentY);
                            handleCollisions(robot);
                        }
                        else {
                            // Update the robot's position to the new coordinates
                            robot.setPosition(newX, newY);
                            // Mark the new position as occupied
                            arena.coordinateOccupied(robot);
                            arena.clearCoordinate((int) currentX, (int) currentY);
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        threadPool.submit(moveRobotTask);
    }

    private void handleCollisions(Robot robot) throws InterruptedException {
        int robotX = (int) robot.getXPos();
        int robotY = (int) robot.getYPos();

        // Get the wall at the robot's position
        Wall wall = arena.getWallAt(robotX, robotY);

        GameObject coordinateObject = arena.coordinateOccupiedBy(robotX, robotY);

        if (coordinateObject instanceof Wall) {
            // Check if the wall is already impacted (first impact)
            if (wall.getStatus().equals("built")) {
                wall.damageWall();
            }
            else {
                arena.removeWall(wall);
                wall.destroyWall();
            }
            logCollision(robot, wall);
            robot.dead();
            arena.removeRobot(robot);
        }
        else if (coordinateObject instanceof Citadel) {
            logEndGame();
            arena.finishGame(robot);
        }
    }

    private void logCollision(Robot robot, Wall wall) {
        int robotX = (int) robot.getXPos() + 1;
        int robotY = (int) robot.getYPos() + 1;
        int wallX = (int) wall.getXPos() + 1;
        int wallY = (int) wall.getYPos() + 1;

        String logWallMessage = "Wall at (" + wallX + ", " + wallY + ") is " + wall.getStatus().toString();
        String logRobotMessage = "Robot " + robot.getName() + " died at (" + robotX + ", " + robotY + ")";

        synchronized (mutex) {
            // Run on the JavaFX application thread to update the UI
            Platform.runLater(() -> {
                logger.appendText(logWallMessage + "\n");
                logger.appendText(logRobotMessage + "\n");
            });
        }
    }

    private void logEndGame() {
        synchronized (mutex2) {
            // Run on the JavaFX application thread to update the UI
            Platform.runLater(() -> {
                logger.appendText("FINAL SCORE: " + arena.getScore());
            });
        }
    }

    private boolean isValidMove(int newX, int newY) {
        return (!arena.isCoordinateOccupied(newX, newY) || arena.isCoordinateOccupiedByWall(newX, newY) || arena.isCoordinateOccupiedByCitadel(newX, newY) )
                && newX >= 0 && newX < 9 && newY >= 0 && newY < 9;
    }
}
