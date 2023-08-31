package edu.curtin.saed.assignment1;

public class Alive extends RobotState {
    public Alive(Robot robot) {
        super(robot);
    }

    @Override
    public void alive() {}

    @Override
    public void dead() {
        robot.setState(new Dead(robot));
    }
}
