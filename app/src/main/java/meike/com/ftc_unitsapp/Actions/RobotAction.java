package meike.com.ftc_unitsapp.Actions;

public class RobotAction {
    public robotActions action;
    public double paramter;

    public RobotAction(String a){
        if (a == null || a.length() < 6){
            return;
        }
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
