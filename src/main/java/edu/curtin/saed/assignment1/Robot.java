package edu.curtin.saed.assignment1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javafx.scene.image.Image;

public class Robot extends GameObject{
    private final String IMAGEFILE = "rg1024-robot-carrying-things-4.png";
    private String id;
    private int d;
    public RobotState robotState;

    public Robot (String id)
    {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(IMAGEFILE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + IMAGEFILE);
            }
            image = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + IMAGEFILE, e);
        }
        this.id = id;
        this.d = delay();
        robotState = new Alive(this);
    }

    public void setPosition(double xPos, double yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setXPosition(double xPos)
    {
        this.xPos = xPos;
    }

    public void setYPosition(double yPos) {
        this.yPos = yPos;
    }

    public void setState(RobotState newState)
    {
        this.robotState = newState;
    }

    public String getName()
    {
        return this.id;
    }

    public int getDelay()
    {
        return this.d;
    }

    public static int delay()
    {
        Random random = new Random();

        // Define the range (2000 - 500 + 1)
        int minRange = 500;
        int maxRange = 2000;
        int range = maxRange - minRange + 1;

        // Generate a random number within the specified range
        return random.nextInt(range) + minRange;
    }

    public void dead()
    {
        robotState.dead();
    }
}
