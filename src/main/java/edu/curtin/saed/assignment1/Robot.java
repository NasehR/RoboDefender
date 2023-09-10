package edu.curtin.saed.assignment1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.control.TextArea;


public class Robot extends GameObject {
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final String IMAGE_FILE = "rg1024-robot-carrying-things-4.png";
    private String id;
    private int d;
    public RobotState robotState;
    private TextArea logger;
    private Object mutex;

    public Robot (String id, TextArea logger)
    {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(IMAGE_FILE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + IMAGE_FILE);
            }
            image = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + IMAGE_FILE, e);
        }
        this.id = id;
        this.d = delay();
        robotState = new Alive(this);
        mutex = new Object();
        this.logger = logger;
    }

    public void setPosition(double xPos, double yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void logCreation() {
        int robotX = (int) getXPos() + 1;
        int robotY = (int) getYPos() + 1;
        System.out.println("robotX: " + getXPos());
        System.out.println("robotY: " + getYPos());
        String logMessage = "Robot " + id + " created at (" + robotX + ", " + robotY + ")";

        synchronized (mutex) {
            // Run on the JavaFX application thread to update the UI
            Platform.runLater(() -> {
                logger.appendText(logMessage + "\n");
            });
        }
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
