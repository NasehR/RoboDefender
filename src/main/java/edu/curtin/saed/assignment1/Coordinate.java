package edu.curtin.saed.assignment1;

public class Coordinate {
    private double x;
    private double y;
    private boolean occupied;
    private GameObject coordinateObject;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
        occupied = false;
        coordinateObject = null;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public boolean isOccupied() {
        return this.occupied;
    }

    public boolean isOccupiedByWall() {
        return (coordinateObject instanceof Wall);
    }

    public void setCoordinateObject(GameObject coordinateObject) {
        this.coordinateObject = coordinateObject;
        this.occupied = true;
    }

    public void clear() {
        this.occupied = false;
        coordinateObject = null;
    }

    public GameObject getCoordinateObject() {
        return this.coordinateObject;
    }

    public boolean isOccupiedByCitadel() {
        return (coordinateObject instanceof Citadel);
    }
}