package edu.curtin.saed.assignment1;

import java.awt.*;
import java.util.Random;

public class Robot
{
    private int id;
    private int delay;
    private double startX;
    private double startY;
    public RobotState robotState;
    private Image image;


    public Robot (int id)
    {
        this.id = id;
        delay = delay();
        robotState = new Alive(this);
    }

    public int delay()
    {
        Random random = new Random();

        // Define the range (2000 - 500 + 1)
        int minRange = 500;
        int maxRange = 2000;
        int range = maxRange - minRange + 1;

        // Generate a random number within the specified range
        return random.nextInt(range) + minRange;
    }

    public void setPosition(double startX, double startY)
    {
        this.startX = startX;
        this.startY = startY;
    }

    public void setState(RobotState newState)
    {
        this.robotState = newState;
    }

    public void dead()
    {
        robotState.dead();
    }

    public void addImage(Image image)
    {
        this.image = image;
    }
}
