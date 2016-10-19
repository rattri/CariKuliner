package skripsi.ratri.carikuliner;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import skripsi.ratri.service.JSONparser;
/**
 * Created by ratri on 1/28/2015.
 */
public class SplashScreen extends ListActivity {
    private String getSearch;
    private double getLatitude;
    private double getLongitude;
    private String getLocation;

    private static String url = "http://android.solfagaming.com/tempat_kuliner.php?";

    private int page = 1;
    private int limit=10;
    private int offset=0;
    private String urlSearch = "";

    ProgressDialog progress;
    private String strLongitude;
    private String strLatitude;

    private  String id_kul;
    ProgressDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        final String method = "GET";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tempat_header);




        Bundle ve = getIntent().getExtras();

        getLatitude = ve.getDouble("parseLatitude");
        getLongitude = ve.getDouble("parseLongitude");


        strLongitude = Double.toString(getLongitude);
        strLatitude = Double.toString(getLatitude);
        getLocation = "lat=" + strLatitude + "&long=" + strLongitude;
        ImageButton btnSearch = (ImageButton) findViewById (R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {

                Intent search = new Intent(SplashScreen.this, VenueCulinarySearch.class);
                EditText keySearch = (EditText) findViewById(R.id.TextSearch);
                String strSearch = keySearch.getText().toString();

                Bundle sr = new Bundle();

                sr.putString("keyWord", strSearch);
                sr.putDouble("userLat", getLatitude);
                sr.putDouble("userLong", getLongitude);
                search.putExtras(sr);
                startActivity(search);

            }
        });

        urlSearch = url + getLocation;
        new BackgroundTask(urlSearch, method, null).execute();

    }



    final class BackgroundTask extends AsyncTask<String, Void, String> {


        ArrayList<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();

        private String TAG_daftar = "data";
        private String TAG_id= "id";
        private String TAG_nama = "nama";
        private String TAG_alamat = "alamat";

        public InputStream is = null;
        public JSONObject jObj;
        public String json = "";

        JSONArray datasearch = null;
        //panggil class parser
        JSONparser parser = new JSONparser();



        // variables passed in:
        String url;
        String method = "GET";
        List<NameValuePair> params;

        // constructor
        public BackgroundTask(String url, String method,
                              List<NameValuePair> params)
        {
            this.url = url;
            this.method = method;
            this.params = params;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SplashScreen.this);
            pDialog.setMessage("LOADING...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }



        @Override
        public String doInBackground(String... urls) {

            String url = urlSearch;
            try {
                if(method.equalsIgnoreCase("POST")) {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
                            CookiePolicy.BROWSER_COMPATIBILITY);
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(params));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();

                }else if(method.equalsIgnoreCase("GET")){
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    //String paramString = URLEncodedUtils.format(params, "utf-8");
                    //url += "?" + paramString;
                    HttpGet httpGet = new HttpGet(url);

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("fail!", "Error convert stream to String: " + e.toString());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return json;
        }

        protected void onPostExecute(String json) {
            pDialog.dismiss();
            try {

                json = doInBackground(url);
                jObj = new JSONObject(json);

            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            try {
            /*
            * JSONObject response = jObj.getJSONObject("response");
            * JSONArray venues = response.JSONArray("venues");
            * */

                JSONArray datasearch = jObj.getJSONObject("response").getJSONArray("data");

                for (int i = 0; i < datasearch.length(); i++) {
                    JSONObject data = datasearch.getJSONObject(i);
                    String id = data.getString(TAG_id);
                    String nama = data.getString(TAG_nama);
                    String alamat = data.getString(TAG_alamat);


                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_id, id);
                    map.put(TAG_nama, nama);
                    map.put(TAG_alamat, alamat);

                    searchList.add(map);
                }


                ListAdapter adapter =
                        new SimpleAdapter(SplashScreen.this, searchList,R.layout.tempat_kuliner,
                                new String[] {TAG_id, TAG_nama, TAG_alamat},
                                new int[]{ R.id.text_id,R.id.text_name,R.id.text_address});



                setListAdapter(adapter);
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        //Mengambil nilai dari ListView yang di Click
                        id_kul = ((TextView) view.findViewById(R.id.text_id)).getText().toString();
                        //Membuat intent untuk menampilkan activity Detail
                        //Selain itu Intent ini juga digunakan untuk mengirimkan suatu data


                        Intent in = new Intent(SplashScreen.this, CulinaryActivity.class);


                        Bundle b = new Bundle();

                        b.putString("parseId", id_kul);
                        b.putDouble("userLat", getLatitude);
                        b.putDouble("userLong", getLongitude);
                        in.putExtras(b);
                        startActivity(in);

                    }
                });
            }
            catch (JSONException e)
            {
                Log.e("ECHO!!", "Gak bisa: ambil venues" + e.toString() + "URL: " + url);
                e.printStackTrace();
            }
        }

    }


}
