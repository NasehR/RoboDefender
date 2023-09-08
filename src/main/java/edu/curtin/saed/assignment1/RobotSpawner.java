package edu.curtin.saed.assignment1;

import java.util.Random;

public class RobotSpawner {

    private final int TOP_LEFT = 0;
    private final int TOP_RIGHT = 1;
    private final int BOTTOM_LEFT = 2;
    private final int BOTTOM_RIGHT = 3;
    private JFXArena arena;
    private int robotCount;

    public RobotSpawner(JFXArena arena) {
        this.arena = arena;
        robotCount = 0;
    }

    public void spawnRobots() throws InterruptedException {
        for (int i = 0; i < 5; i++)
        {
            Random random = new Random();
            int corner = random.nextInt(4); // Randomly choose a corner (0 to 3)
            double xPos, yPos;

    //        System.out.println(corner);
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
                        arena.coordinateOccupied(0, 0, newRobot);;
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
                        arena.coordinateOccupied(8, 0, newRobot);
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
                        arena.coordinateOccupied(0, 8, newRobot);
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
                        arena.coordinateOccupied(8, 8, newRobot);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + corner);
            }
            Thread.sleep(1500);
        }
    }

    private void moveRobot(Robot robot) throws InterruptedException {

    }
}
