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
        for (int i = 0; i < 3; i++)
        {
            Thread.sleep(1500);
            Random random = new Random();

            int corner = random.nextInt(4); // Randomly choose a corner (0 to 3)
            double xPos, yPos;

    //        System.out.println(corner);

            switch (corner) {
                case TOP_LEFT: // Top-left corner
                    xPos = 0.0;
                    yPos = 0.0;
                    break;
                case TOP_RIGHT: // Top-right corner
                    xPos = 8.0;
                    yPos = 0.0;
                    break;
                case BOTTOM_LEFT: // Bottom-left corner
                    xPos = 0.0;
                    yPos = 8.0;
                    break;
                case BOTTOM_RIGHT: // Bottom-right corner
                    xPos = 8.0;
                    yPos = 8.0;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + corner);
            }

            Robot newRobot = new Robot(Integer.toString(++robotCount));
            newRobot.setPosition(xPos, yPos);

            try {
                arena.addRobot(newRobot);
                moveRobot(newRobot);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void moveRobot(Robot robot) throws InterruptedException {

    }
}
