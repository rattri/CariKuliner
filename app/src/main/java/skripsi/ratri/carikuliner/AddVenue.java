package skripsi.ratri.carikuliner;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import skripsi.ratri.service.JSONparser;

/**
 * Created by ratri on 2/1/2015.
 */
public class AddVenue extends ListActivity {
    private String getId;
    private String getVenue;
    private String getLat;
    private String getLong;
    private String getAddress;
    private String urlve="";
    JSONParser jParser = new JSONParser();
    ProgressDialog pDialog;
    private static String url = "http://android.solfagaming.com/add_venue.php";
    String method = "GET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String method = "GET";


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);


        Bundle b = getIntent().getExtras();
        getId = b.getString("fsqId");
        getVenue = b.getString("fsqVenue");
        getAddress = b.getString("fsqAddress");
        getLat = b.getString("fsqLat");
        getLong = b.getString("fsqLong");
        /*idUserLogin = b.getString("idUser");*/

        urlve=url+"?id="+getId+"&venue="+getVenue+"&address="+getAddress+"&long="+getLong+"&lat="+getLat;
        new input().execute();

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
            pDialog = new ProgressDialog(AddVenue.this);
            pDialog.setMessage("LOADING...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", getId));
            params.add(new BasicNameValuePair("venue", getVenue));
            params.add(new BasicNameValuePair("address", getAddress));
            params.add(new BasicNameValuePair("lat", getLat));
            params.add(new BasicNameValuePair("long", getLong));


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

                Intent i = new Intent(AddVenue.this, AddFood.class);
                Bundle b = new Bundle();

                b.putString("parseId", getId);

                i.putExtras(b);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(), message + " Gagal Bro!!! ", Toast.LENGTH_LONG).show();
                Log.d("error_brooo",message + " Gagal Bro!!! ");
            }
        }
    }
}


