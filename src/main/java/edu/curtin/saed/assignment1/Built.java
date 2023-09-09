package edu.curtin.saed.assignment1;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class Built extends WallState {
    public Wall wall;

    public Built(Wall wall) {
        this.wall = wall;
    }

    @Override
    public void built() {
        // do nothing
    }

    @Override
    public void damage() {
        wall.setWallState(new Damaged(wall));

        try(InputStream is = getClass().getClassLoader().getResourceAsStream("181479.png"))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + "181479.png");
            }
            Image image = new Image(is);
            wall.setImage(image);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + "181479.png", e);
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public String toString() {
        return "built";
    }
}
