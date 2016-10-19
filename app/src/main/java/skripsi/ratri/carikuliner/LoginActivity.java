package skripsi.ratri.carikuliner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import skripsi.ratri.service.DBLib;

/**
 * Created by ratri on 2/4/2015.
 */
public class LoginActivity extends Activity {
    private DBLib db = null;
    private EditText email, password;
    Button login;
    ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url = "http://android.solfagaming.com/login_user.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = (EditText)findViewById(R.id.edit_email);
        password= (EditText)findViewById(R.id.edit_password);


        login = (Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new login().execute();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent main=new Intent(this,MainActivity.class);
            startActivity(main);
            Toast.makeText(getApplicationContext(), "Recreate activity..",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onKeyDown(keyCode,event);
    }

    public class login extends AsyncTask<String, String, String>
    {
        String success;
        String message;
        JSONObject json;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Login in process...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email.getText().toString()));
            params.add(new BasicNameValuePair("password", password.getText().toString()));

            json = jParser.makeHttpRequest(url, "POST", params);

            try {
                success = json.getString("success");
                message = json.getString("message");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();
                Log.e("error parsing","Parsing success error broo" + e.toString());
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (success.equals("1"))
            {
                String nama = "";
                String email = "";
                JSONArray data;
                try {
                    data = json.getJSONArray("data");
                    nama = data.getString(1);
                    email = data.getString(2);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error",
                            Toast.LENGTH_LONG).show();
                    Log.e("error parsing", "Parsing nama error broo" + e.toString());
                }
                Toast.makeText(getApplicationContext(), "Login Sukses!!! namanya :" + nama + ", email:" + email, Toast.LENGTH_LONG).show();
                db = new DBLib(getApplicationContext());
                db.insertUser(nama, email);
                db.close();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), message + " Gagal Bro!!! ", Toast.LENGTH_LONG).show();
                Log.d("error_brooo", message + " Gagal Bro!!! ");
            }
        }
    }
}

