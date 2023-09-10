package edu.curtin.saed.assignment1;

import javafx.scene.image.Image;

@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class GameObject {
    protected double xPos;
    protected double yPos;
    protected Image image;

    public Image getImage()
    {
        return this.image;
    }
    public double getXPos()
    {
        return this.xPos;
    }

    public double getYPos()
    {
        return this.yPos;
    }
}
