package edu.curtin.saed.assignment1;

public class Dead extends RobotState {
    public Dead(Robot robot) {
        super(robot);
    }

    @Override
    public void alive() {
        robot.setState(new Alive(robot));
    }

    @Override
    public void dead() {

    }
}
