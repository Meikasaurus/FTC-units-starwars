package meike.com.ftc_unitsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import meike.com.ftc_unitsapp.Actions.RobotAction;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private View view;
    boolean isRed;
    public RobotAction[] actions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
    }
    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        this.view = view;
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //startpos
        EditText startPos = (EditText) findViewById(R.id.StartPos);
        String message = startPos.getText().toString();
        Vector2 a = InputCheckPos(message);

        //actions
        //action 1
        EditText action1 = (EditText) findViewById(R.id.Action1);
        String action1s = action1.getText().toString();
        CreateAction(action1s);
        //action 2
        EditText action2 = (EditText) findViewById(R.id.Action2);
        String action2s = action2.getText().toString();
        CreateAction(action2s);
        //action 3
        EditText action3 = (EditText) findViewById(R.id.Action3);
        String action3s = action3.getText().toString();
        CreateAction(action3s);
        //action 4
        EditText action4 = (EditText) findViewById(R.id.Action4);
        String action4s = action4.getText().toString();
        CreateAction(action4s);


        try {
            WriteToFile(a, !isRed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra(EXTRA_MESSAGE, message + " saved to the filesystem");
        startActivity(intent);
    }

    public Vector2 InputCheckPos(String Input) {
        Input = Input.toLowerCase();
        Vector2 pos;
        switch (Input) {
            case "bt":
                pos = new Vector2(590,0);
                isRed = false;
                break;
            case "ba":
                pos = new Vector2(1180,0);
                isRed = false;
                break;
            case "bc":
                pos = new Vector2(2360,0);
                isRed = false;
                break;
            case "rt":
                pos = new Vector2(590,0);
                isRed = true;
                break;
            case "ra":
                pos =  new Vector2(1180,0);
                isRed = true;
                break;
            case "rc":
                pos = new Vector2(2360,0);
                isRed = true;
                break;
                default:

                    pos = new Vector2(0,0);
                    isRed = false;
                    break;
        }
        return pos;
    }

    public void WriteToFile (Vector2 Input, boolean isBlue) throws JSONException {
        JSONObject JSONinput;
        JSONinput = new JSONObject();
        JSONinput.put("blueAlliance", isBlue);
        JSONinput.put("x", Input.X);
        JSONinput.put("y", Input.Y);
        JSONinput.put("actions", actions);

        File file = new File(getPublicAlbumStorageDir("FTCunits"), "startpos.json" );
        if (!file.canWrite()){
            Log.println(1,"Error","File not created properly");
        }
        try {
            FileWriter a = new FileWriter(file);
            a.write(JSONinput.toString());
            a.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**gets the public image folder */
    public  static File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), albumName);
        if (!file.mkdirs()) {
            Log.println(1, "Status","Failed to create directory");
        }
        return file;
    }

    public void CreateAction (String s){
        if (s != null && s.length() > 5){
            RobotAction a = new RobotAction(s);
            if (a.action != null){
                actions[actions.length] = a;
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////               import van stackoverflow
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
