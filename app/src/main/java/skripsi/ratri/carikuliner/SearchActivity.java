package skripsi.ratri.carikuliner;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import skripsi.ratri.service.JSONparser;


/**
 * Created by ratri on 12/24/2014.
 */
public class SearchActivity extends ListActivity  {
    private String getSearch;
    private double getLatitude;
    private double getLongitude;
    private String getLocation;

    private static String url = "http://android.solfagaming.com/search_place.php?name=";

    private int page = 1;
    private int limit=10;
    private int offset=0;
    private String urlget = "";
    private String TAG_daftar = "data";
    private String TAG_nama = "nama";
    private String TAG_harga = "harga";
    private String TAG_id_kul = "id_kuliner";
    private String TAG_id_user = "id_user";
    private String TAG_tempat = "tempat";
    private String TAG_jarak = "jarak";

    private String strLongitude;
    private String strLatitude;
    ProgressDialog pDialog;

    JSONArray datasearch = null;
    // panggil class parser
    JSONparser parser = new JSONparser();
    ArrayList<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final String method = "GET";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //LinearLayout navi = (LinearLayout) findViewById(R.id.navigasi);
        //navi.setVisibility(LinearLayout.VISIBLE);
        Bundle b = getIntent().getExtras();
        getSearch = b.getString("parseSearch");
        getLatitude = b.getDouble("parseLatitude");
        getLongitude = b.getDouble("parseLongitude");

        page =b.getInt("pagin",1);
        if(page>1){
            offset = (limit*(page-1))+1;
        }
        strLongitude = Double.toString(getLongitude);
        strLatitude = Double.toString(getLatitude);

        //strLatitude="20";
        //strLongitude="11";


        getLocation = "&lat=" + strLatitude + "&long=" + strLongitude;

        urlget = url + getSearch + getLocation;

        urlget = urlget + "&limit="+limit+"&offset="+offset;
        urlget = urlget.replace(" ","%20");

        new BackgroundTask(urlget, method, null).execute();
        /*JSONObject json = parser.getJSONFromUrl(urlget);
        try {
            datasearch = json.getJSONArray(TAG_daftar);

            for (int i = 0; i < datasearch.length(); i++) {
                JSONObject data = datasearch.getJSONObject(i);

                String nama = data.getString(TAG_nama);
                String harga = data.getString(TAG_harga);
                String id_user = data.getString(TAG_id_user);
                String id_kul = data.getString(TAG_id_kul);
                String tempat = data.getString(TAG_tempat);
                String jarak= data.getString(TAG_jarak);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_nama, nama);
                map.put(TAG_harga, harga);
                map.put(TAG_id_user, id_user);
                map.put(TAG_id_kul, id_kul);
                map.put(TAG_tempat, tempat);
                map.put(TAG_jarak, jarak);
                searchList.add(map);


            }
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        this.adapter_listview();
        */
    }

    final class BackgroundTask extends AsyncTask<String, Void, String> {
        ArrayList<HashMap<String, String>> venuesList = new ArrayList<HashMap<String, String>>();

        public InputStream is = null;
        public JSONObject jObj;
        public String json = "";

        // variables passed in:
        String url;
        String method = "GET";
        List<NameValuePair> params;

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
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("LOADING...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        public String doInBackground(String... urls) {
            String url = urlget;
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
                datasearch = jObj.getJSONArray(TAG_daftar);

                for (int i = 0; i < datasearch.length(); i++) {
                    JSONObject data = datasearch.getJSONObject(i);

                    String nama = data.getString(TAG_nama);
                    String harga = data.getString(TAG_harga);
                    String id_user = data.getString(TAG_id_user);
                    String id_kul = data.getString(TAG_id_kul);
                    String tempat = data.getString(TAG_tempat);
                    String jarak= data.getString(TAG_jarak);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_nama, nama);
                    map.put(TAG_harga, harga);
                    map.put(TAG_id_user, id_user);
                    map.put(TAG_id_kul, id_kul);
                    map.put(TAG_tempat, tempat);
                    map.put(TAG_jarak, jarak);
                    searchList.add(map);
                }
            }
            catch (JSONException e)
            {
                Log.e("ECHO!!", "Gak bisa: ambil venues" + e.toString() + "URL: " + url);
                e.printStackTrace();
            }
            SearchActivity.this.adapter_listview();
        }
    }

    public void adapter_listview() {

        // tampilkan ke listadapter
        ListAdapter adapter = new SimpleAdapter(this, searchList,
                R.layout.search_activity, new String[]{TAG_tempat,TAG_nama, TAG_harga,  TAG_jarak, TAG_id_kul},
                new int[]{ R.id.text_tempat,R.id.text_nama,R.id.text_harga, R.id.text_jarak, R.id.text_id_kul});

        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //Mengambil nilai dari ListView yang di Click
                String id_kul = ((TextView) view.findViewById(R.id.text_id_kul)).getText().toString();
                //Membuat intent untuk menampilkan activity Detail
                //Selain itu Intent ini juga digunakan untuk mengirimkan suatu data
                Intent in = new Intent(SearchActivity.this, CulinaryActivity.class);
                Bundle b = new Bundle();

                b.putString("parseId", id_kul);
                //b.putDouble("userLat", getLatitude);
                //b.putDouble("userLong", getLongitude);
                in.putExtras(b);
                startActivity(in);

            }
        });
    }}