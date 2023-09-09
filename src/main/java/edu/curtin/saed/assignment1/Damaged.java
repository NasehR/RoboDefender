package edu.curtin.saed.assignment1;

public class Damaged extends WallState {
    public Wall wall;

    public Damaged(Wall wall){
        this.wall = wall;
    }

    @Override
    public void built() {
        // do nothing
    }

    @Override
    public void damage() {
        // do nothing
    }

    @Override
    public void destroy() {
        wall.setState(new Destroy(wall));
    }

    @Override
    public String toString() {
        return "damaged";
    }
}
