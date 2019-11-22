package meike.com.ftc_unitsapp.Actions;

import org.json.JSONException;
import org.json.JSONObject;

public class RobotAction {
    public robotActions action;
    public double paramater;

    public RobotAction(String a){
        if (a == null || a.length() < 6){
            return;
        }
        a.toLowerCase();
        if (a.contains(":")) {
            String[] b = a.split(":");

            if (b != null && b.length > 1) {
                paramater = (double) Double.parseDouble(b[1]);
            }

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

    public JSONObject toJson () {
        JSONObject a = new JSONObject();
        try {
            a.put("action", action);
            a.put("parameter", paramater);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return a;
    }
}
