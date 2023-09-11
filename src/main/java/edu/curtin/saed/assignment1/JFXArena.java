package edu.curtin.saed.assignment1;

import javafx.scene.canvas.*;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 */
public class JFXArena extends Pane
{
    // Represents an image to draw, retrieved as a project resource.
    // The following values are arbitrary, and you may need to modify them according to the 
    // requirements of your application.
    private final int gridWidth = 9;
    private final int gridHeight = 9;
    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.
    private List<ArenaListener> listeners = null;
    private BlockingQueue<Robot> robots;
    private List<Wall> walls;
    private Coordinate[][] grid;
    private Citadel citadel;
    private boolean continueGame;
    private ScoreManager scoreManager;

    /**
     * Creates a new arena object, loading the robot image and initialising a drawing surface.
     */
    public JFXArena() {
        // Here's how (in JavaFX) you get an Image object from an image file that's part of the 
        // project's "resources". If you need multiple different images, you can modify this code 
        // accordingly.
        
        // (NOTE: _DO NOT_ use ordinary file-reading operations here, and in particular do not try
        // to specify the file's path/location. That will ruin things if you try to create a 
        // distributable version of your code with './gradlew build'. The approach below is how a 
        // project is supposed to read its own internal resources, and should work both for 
        // './gradlew run' and './gradlew build'.)

        continueGame = true;
        robots = new LinkedBlockingQueue<>();
        walls = new ArrayList<>();
        grid = new Coordinate[gridWidth][gridHeight];
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
        for (double i = 0; i < gridWidth; i++) {
            for (double j = 0; j < gridHeight; j++) {
                grid[(int) i][(int) j] = new Coordinate(i,j);
            }
        }
    }
    
    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback 
     * (of type ArenaListener) receives the grid (x,y) coordinates as parameters to the 
     * 'squareClicked()' method.
     */
    public void addListener(ArenaListener newListener)
    {
        if(listeners == null)
        {
            listeners = new LinkedList<>();
            setOnMouseClicked(event ->
            {
                int gridX = (int)(event.getX() / gridSquareSize);
                int gridY = (int)(event.getY() / gridSquareSize);
                
                if(gridX < gridWidth && gridY < gridHeight)
                {
                    for(ArenaListener listener : listeners)
                    {   
                        listener.squareClicked(gridX, gridY);
                    }
                }
            });
        }
        listeners.add(newListener);
    }

    public void setOnSquareClicked(ArenaListener listener) {
        addListener(listener);
    }

    /**
     * This method is called in order to redraw the screen, either because the user is manipulating 
     * the window, OR because you've called 'requestLayout()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void layoutChildren() {
        super.layoutChildren(); 
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        
        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            getWidth() / (double) gridWidth,
            getHeight() / (double) gridHeight);
            
        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;
            
            
        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setStroke(Color.DARKGREY);
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }
        
        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        // Invoke helper methods to draw things at the current location.
        // ** You will need to adapt this to the requirements of your application. **

        for (Robot robot : robots) {
            drawRobot(gfx, robot);
        }

        for (Wall wall: walls) {
            if (wall != null) {
                drawWall(gfx, wall);
            }
        }

        if (citadel != null) {
            drawCitadel(gfx, citadel);
        }
    }

    private void drawCitadel(GraphicsContext gfx, Citadel citadel) {
        double citadelX = citadel.getXPos();
        double citadelY = citadel.getYPos();
        Image citadelImage = citadel.getImage();

        double x = (citadelX) * gridSquareSize;
        double y = (citadelY) * gridSquareSize;

        double fullSizePixelWidth = citadel.getImage().getWidth();
        double fullSizePixelHeight = citadel.getImage().getHeight();

        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as
            // wide as a full grid cell, and the height will be set to preserve the aspect
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        gfx.drawImage(citadelImage,
                x,
                y,
                displayedPixelWidth,
                displayedPixelHeight);
    }

    private void drawRobot(GraphicsContext gfx, Robot robot) {
        double robotX = robot.getXPos();
        double robotY = robot.getYPos();
        Image robotImage = robot.getImage();
        String robotName = robot.getName();
        drawImage(gfx, robotImage, robotX, robotY, robot);
        drawLabel(gfx, robotName, robotX, robotY);
    }

    public void addWall(Wall wall) {
        if (walls.size() < 10 ) {
            walls.add(wall);
        }
    }

    private void drawWall(GraphicsContext gfx, Wall wall) {
        double wallx = wall.getXPos();
        double wally = wall.getYPos();
        Image wallImage = wall.getImage();

        double x = (wallx) * gridSquareSize;
        double y = (wally) * gridSquareSize;

        double fullSizePixelWidth = wall.getImage().getWidth();
        double fullSizePixelHeight = wall.getImage().getHeight();

        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as
            // wide as a full grid cell, and the height will be set to preserve the aspect
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        gfx.drawImage(wallImage,
                x,
                y,
                displayedPixelWidth,
                displayedPixelHeight);
    }

    /** 
     * Draw an image in a specific grid location. *Only* call this from within layoutChildren(). 
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image 
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *     
     * You shouldn't need to modify this method.
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY, Robot robot) {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = robot.getImage().getWidth();
        double fullSizePixelHeight = robot.getImage().getHeight();
        
        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to 
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(image,
            x - displayedPixelWidth / 2.0,  // Top-left pixel coordinates.
            y - displayedPixelHeight / 2.0, 
            displayedPixelWidth,              // Size of displayed image.
            displayedPixelHeight);
    }
    
    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within 
     * layoutChildren(). 
     *     
     * You shouldn't need to modify this method.
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY) {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }

    public void addRobot(Robot robot) throws InterruptedException {
        robots.put(robot);
        layoutChildren(); // Redraw the arena to show the new robot
    }

    public void removeRobot(Robot robot) throws InterruptedException {
        robots.remove(robot);
        scoreManager.incrementScoreOnRobotDestroyed();
        layoutChildren(); // Redraw the arena to remove the robot
        throw new InterruptedException();
    }

    public boolean isCoordinateOccupied(int x, int y) {
        return grid[x][y].isOccupied();
    }

    public boolean isCoordinateOccupiedByWall(int x, int y) {
        return grid[x][y].isOccupiedByWall();
    }

    public boolean isCoordinateOccupiedByCitadel(int x, int y) {
        return grid[x][y].isOccupiedByCitadel();
    }

    public void coordinateOccupied(GameObject coordinateObject) {
        int x = (int) coordinateObject.getXPos();
        int y = (int) coordinateObject.getYPos();
        grid[x][y].setCoordinateObject(coordinateObject);
    }

    public GameObject coordinateOccupiedBy(int x, int y) {
        return grid[x][y].getCoordinateObject();
    }

    public void clearCoordinate(int x, int y) {
        this.grid[x][y].clear();
    }

    public double getGridSquareSize() {
        return this.gridSquareSize;
    }

    public Wall getWallAt(int x, int y) {
        // Iterate through the list of walls to find the one at the specified position
        Wall wall = null;
        for (Wall w : walls) {
            if (w.getXPos() == x && w.getYPos() == y) {
                wall = w; // Return the wall at the specified position
            }
        }

        return wall;
    }

    public void removeWall(Wall wall) {
        if (walls.contains(wall)) {
            walls.remove(wall);
        }
    }

    public void addCitadel(Citadel citadel) {
        this.citadel = citadel;
        int citadelX = (int) citadel.getXPos();
        int citadelY = (int) citadel.getYPos();
        grid[citadelX][citadelY].setCoordinateObject(citadel);
    }

    public void removeCitadel() {
        int citadelX = (int) citadel.getXPos();
        int citadelY = (int) citadel.getYPos();
        grid[citadelX][citadelY].clear();
        citadel = null;
    }

    public boolean continueGame() {
        return continueGame;
    }

    public void finishGame(Robot robot) throws InterruptedException {
        this.continueGame = false;
        robot.dead();
        removeCitadel();
        removeRobot(robot);
        System.out.println("FINAL SCORE: " + getScore());
    }

    public void addScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public int getScore() {
        return scoreManager.getScore();
    }

    public int numberOfWalls() {
        return walls.size();
    }
}
