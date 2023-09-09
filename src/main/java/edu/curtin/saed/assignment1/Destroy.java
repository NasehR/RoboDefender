package edu.curtin.saed.assignment1;

public class Destroy extends WallState{

    public Wall wall;

    public Destroy(Wall wall){
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
        // remove the wall from the arena
    }

    @Override
    public String toString() {
        return "destroy";
    }
}
