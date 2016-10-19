package skripsi.ratri.carikuliner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ratri on 2/10/2015.
 */
public class AddUser extends Activity {

    private String nama, email, password;
    private static String url = "http://android.solfagaming.com/add_venue.php";
    String method = "GET";

    ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String method = "GET";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);


        Bundle b = getIntent().getExtras();
        nama = b.getString("parseNama");
        email = b.getString("parseEmail");
}}
