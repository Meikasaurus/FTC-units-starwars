package meike.com.ftc_unitsapp;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        this.view = view;
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public Vector2 InputCheck (String Input) {
        Input = Input.toLowerCase();
        Vector2 pos;
        switch (Input) {
            case "bt":
                pos = new Vector2(590,0);
                break;
            case "ba":
                pos = new Vector2(1180,0);
                break;
            case "bc":
                pos = new Vector2(2360,0);
                break;
            case "rt":
                pos = new Vector2(590,0);
                break;
            case "ra":
                pos =  new Vector2(1180,0);
                break;
            case "rc":
                pos = new Vector2(2360,0);
                break;
                default:
                    pos = new Vector2(0,0);
                    break;
        }
        return pos;
    }
    public void WriteToFile (Vector2 Input) throws JSONException {
        JSONObject JSONinput;
        JSONinput = new JSONObject();
        JSONinput.put("x", Input.X);
        JSONinput.put("y", Input.Y);
        Files.write(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), JSONinput.toString().getBytes());


    }

    /**gets the public image folder */
    public  static File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(, albumName);
        if (!file.mkdirs()) {

        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // Here are some examples of how you might call this method.
// The first parameter is the MIME type, and the second parameter is the name
// of the file you are creating:
//
// createFile("text/plain", "foobar.txt");
// createFile("image/png", "mypicture.png");

    // Unique request code.
    private static final int WRITE_REQUEST_CODE = 43;
    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

}
