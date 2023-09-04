package edu.curtin.saed.assignment1;

public class Damaged extends WallState {
    public Wall wall;

    public Damaged(Wall wall){
        this.wall = wall;
    }

    @Override
    public void built() {

    }

    @Override
    public void damage() {

    }

    @Override
    public void destroy() {
        wall.setWallState(new Destroy(wall));
    }
}
