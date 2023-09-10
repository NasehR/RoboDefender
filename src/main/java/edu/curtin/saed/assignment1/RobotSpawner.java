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
                        arena.coordinateOccupied(newRobot);
                        moveRobot(newRobot);
                    }
                    break;
                case TOP_RIGHT: // Top-right corner
                    if(!arena.isCoordinateOccupied(8, 0)) {
                        xPos = 8.0;
                        yPos = 0.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount));
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                        moveRobot(newRobot);
                    }
                    break;
                case BOTTOM_LEFT: // Bottom-left corner
                    if(!arena.isCoordinateOccupied(0, 8)) {
                        xPos = 0.0;
                        yPos = 8.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount));
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
                        moveRobot(newRobot);
                    }
                    break;
                case BOTTOM_RIGHT: // Bottom-right corner
                    if(!arena.isCoordinateOccupied(8, 8)) {
                        xPos = 8.0;
                        yPos = 8.0;
                        Robot newRobot = new Robot(Integer.toString(++robotCount));
                        newRobot.setPosition(xPos, yPos);
                        arena.addRobot(newRobot);
                        arena.coordinateOccupied(newRobot);
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
                double moveStep = arena.getGridSquareSize() / 10; // Divide the movement into 10 steps
                int targetX = 4; // Target X coordinate (citadel's X coordinate)
                int targetY = 4; // Target Y coordinate (citadel's Y coordinate)

                while (robot.getXPos() != targetX || robot.getYPos() != targetY) {
                    Thread.sleep(robot.getDelay());

                    // Calculate the difference between the current position and the target
                    int xDiff = targetX - (int) robot.getXPos();
                    int yDiff = targetY - (int) robot.getYPos();

                    int newX, newY;

                    if (Math.abs(xDiff) >= Math.abs(yDiff)) {
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

        System.out.println(wall.getStatus());
        // Check if the wall is already impacted (first impact)
        if (wall.getStatus().equals("built")) {
            wall.damageWall();
        }
        else {
            arena.removeWall(wall);
            wall.destroyWall();
        }
        robot.dead();
        arena.removeRobot(robot);
    }

    private boolean isValidMove(int newX, int newY) {
        return (!arena.isCoordinateOccupied(newX, newY) || arena.isCoordinateOccupiedByWall(newX, newY))
                && newX >= 0 && newX < 9 && newY >= 0 && newY < 9;
    }
}
