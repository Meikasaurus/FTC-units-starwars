package meike.com.ftc_unitsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import meike.com.ftc_unitsapp.Actions.RobotAction;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private View view;
    boolean isRed;
    public List<RobotAction> actions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        actions = new ArrayList<RobotAction>();
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
        JSONArray array = new JSONArray();
        if (actions != null) {
            Log.d("status", "actions is not null");
            for (int j = 0; j < actions.size(); j++)
            {
                RobotAction a = actions.get(j);
                JSONObject sa = a.toJson();
                array.put(sa);
                Log.d("status", "found action: " + a.action);
            }
            JSONinput.put("actions", array);
            Log.d("status", "array length" + array.length());
        }
        File file = null;
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            // You are allowed to write external storage:
             file = new File(getPublicAlbumStorageDir("FTCunits"), "startpos.json" );
        }

        if (!file.canWrite()){
            Log.println(1,"Error","File not created properly");
        }
        try {
            FileWriter a = new FileWriter(file);
            String output = JSONinput.toString(2);
            Log.d("status", output);
            a.write(output);
            a.flush();
            a.close();
            Log.d("status", "wrote file to " + file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**gets the public documents foler */
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
                actions.add(a);
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
            Log.d("Permissions", "Requesting permissions");
        }
    }

    public static final int REQUEST_WRITE_STORAGE = 112;


}
