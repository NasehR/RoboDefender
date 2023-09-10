package edu.curtin.saed.assignment1;

public abstract class RobotState {
    /* default */ Robot robot;

    public RobotState(Robot robot) {
        this.robot = robot;
    }

    public abstract void alive();
    public abstract void dead();
}
