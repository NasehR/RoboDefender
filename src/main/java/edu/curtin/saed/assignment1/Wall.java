package edu.curtin.saed.assignment1;

import javafx.scene.image.Image;
import java.io.IOException;
import java.io.InputStream;

public class Wall extends GameObject{
    private final String IMAGE_FILE = "181478.png";
    public WallState wallState;

    public Wall(double xPos, double yPos) {
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

        wallState = new Built(this);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setWallState(WallState newState) {
        this.wallState = newState;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void damageWall() {
        wallState.damage();
    }

    public void destroyWall() {
        wallState.destroy();
    }
}
