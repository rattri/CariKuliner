package skripsi.ratri.carikuliner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ratri on 2/10/2015.
 */
public class Register extends Activity {

    private EditText nama, email, password;
    public String strNama, strEmail, strPassword;

    ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url = "http://android.solfagaming.com/add_user.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        nama = (EditText)findViewById(R.id.nama);
        email = (EditText)findViewById(R.id.email);
        password= (EditText)findViewById(R.id.password);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == android.view.KeyEvent.KEYCODE_BACK))
        {
            Intent main=new Intent(this,MainActivity.class);
            startActivity(main);
            //Toast.makeText(getApplicationContext(), "Recreate activity..",
                    //Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onKeyDown(keyCode,event);
    }



    public void register(View view) {

        strNama = nama.getText().toString();
        strEmail = email.getText().toString();
        strPassword = password.getText().toString();

        new input().execute();
        /*Intent reg=new Intent(Register.this,AddUser.class);
        Bundle b = new Bundle();
        b.putString("parseNama", strNama);
        b.putString("parseEmail", strEmail);
        b.putString("parsePassword", strPassword);
        reg.putExtras(b);
        startActivity(reg);
        finish();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public class input extends AsyncTask<String, String, String>
    {
        String success;
        String message;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("LOADING...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama", strNama));
            params.add(new BasicNameValuePair("email", strEmail));
            params.add(new BasicNameValuePair("password", strPassword));


            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            try {
                success = json.getString("success");
                message = json.getString("message");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

            if (success.equals("1"))
            {
                Toast.makeText(getApplicationContext(), "Sukses Ditambah!!! " + message, Toast.LENGTH_LONG).show();
                Intent i = new Intent(Register.this, MainActivity.class);
                Log.d("error_brooo", strEmail + " Gagal Bro!!! ");
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(), message + " Gagal Bro!!! ", Toast.LENGTH_LONG).show();
                Log.d("error_brooo", message + " Gagal Bro!!! ");
            }
}}

}
