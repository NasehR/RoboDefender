package edu.curtin.saed.assignment1;

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
                while (robot.getXPos() != 4.0 && robot.getYPos() != 4.0) {
                    Thread.sleep(robot.getDelay());
                    int xDiff, yDiff;
                    int newX, newY;
                    double moveStep = arena.getGridSquareSize()/400;
                    /*
                    get the available squares to move into.
                    randomly choose a square move into that square.
                     */
                    xDiff = (8 - (int) robot.getXPos());
                    yDiff = (8 - (int) robot.getYPos());

                    /*
                    if (xDiff < yDiff) {
                        // loop while the new pos isn't reached
                        newX = (int) robot.getXPos();
                        newY = (int) robot.getYPos() + yDiff;
                        for (int i = 0; i < 400; i++) {
                            synchronized (lock) {
                                if ((!arena.isCoordinateOccupied(newX, newY)) || arena.isCoordinateOccupiedByWall(newX, newY)){
                                    robot.setYPosition(robot.getYPos() + moveStep);
                                }
                                System.out.println(robot.getName() + ": MOVE y WARDS");
                            }
                        }
                         Thread.sleep(40);
                    }
                    else if (xDiff >= yDiff){
                        newX = (int) robot.getXPos() + xDiff;
                        newY = (int) robot.getYPos();
                        // loop while the new pos isn't reached
                        for (int i = 0; i < 400; i++) {
                            synchronized (lock) {
                                if ((!arena.isCoordinateOccupied(newX, newY)) || arena.isCoordinateOccupiedByWall(newX, newY)) {
                                    robot.setXPosition(robot.getXPos() + moveStep);
                                }
                                System.out.println(robot.getName() + ": MOVE x WARDS");
                            }
                            Thread.sleep(40);
                        }
                    }

                     */
                }

            } catch (InterruptedException e) { }
        };

        threadPool.submit(moveRobotTask);
    }
}
