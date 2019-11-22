package org.firstinspires.ftc.teamcode.Actions;

public class RobotAction {
    public robotActions action;
    public double paramter;

    public RobotAction(String a){
        a.toLowerCase();
        String[] b = a.split(":");
        paramter = Double.parseDouble(b[1]);
        switch (b[0]) {
            case "wait":
                action = robotActions.wait;
                break;
            case "park":
                action = robotActions.park;
                break;
            case "foundation":
                action = robotActions.foundation;
                break;
            default:
                action = robotActions.wait;
                break;
        }
    }
}
