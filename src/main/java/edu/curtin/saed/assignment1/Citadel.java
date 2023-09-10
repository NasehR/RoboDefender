package edu.curtin.saed.assignment1;

import javafx.scene.image.Image;
import java.io.IOException;
import java.io.InputStream;

public class Citadel extends GameObject {
    @SuppressWarnings("PMD.FieldNamingConventions")
    private final String IMAGE_FILE = "rg1024-isometric-tower.png";

    public Citadel() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(IMAGE_FILE)) {
            if (is == null) {
                throw new AssertionError("Cannot find image file " + IMAGE_FILE);
            }
            image = new Image(is);
        } catch (IOException e) {
            throw new AssertionError("Cannot load image file " + IMAGE_FILE, e);
        }

        // Set the initial position of the citadel to the center of the arena
        xPos = 4.0;
        yPos = 4.0;
    }
}
