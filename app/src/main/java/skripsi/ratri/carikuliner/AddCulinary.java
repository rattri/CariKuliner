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
public class AddCulinary extends ListActivity {
    private  String urlSearch = "";
    public double getLatitude;
    public double getLongitude;
    public String getLocation;
    public String strLongitude;
    public String strLatitude, idUserLogin;
    public String strSearch;
    public String useSearch="";
    ProgressDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        final String method = "GET";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_add);


        Bundle b = getIntent().getExtras();

        getLatitude = b.getDouble("parseLatitude");
        getLongitude = b.getDouble("parseLongitude");
        idUserLogin = b.getString("idUser");

        Bundle s = getIntent().getExtras();
        useSearch = s.getString("search");


        strLongitude = Double.toString(getLongitude);
        strLatitude = Double.toString(getLatitude);
        urlSearch = "https://api.foursquare.com/v2/venues/search?client_id=VOWLDU5UW1MU4TK50V4GKK1315AQL5TO3LUKE2NS1ZI1HOGG&client_secret=Z0ONVIMP2TL5U2AA3O1RAFKCK2C5VYNYLMWW45R5UGLTIP2N&v=20130815&radius=400&ll=" + strLongitude + "," + strLatitude;
        new BackgroundTask(urlSearch, method, null).execute();

    }

    final class BackgroundTask extends AsyncTask<String, Void, String> {


        ArrayList<HashMap<String, String>> venuesList = new ArrayList<HashMap<String, String>>();

        private static final String TAG_VENUES = "venues";
        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "name";
        private static final String TAG_CONTACT = "contact";
        private static final String TAG_LOCATION = "location";
        private static final String TAG_ADDRESS = "address";
        private static final String TAG_CROSS_STREET = "crossStreet";
        private static final String TAG_CITY = "city";
        private static final String TAG_STATE = "state";
        private static final String TAG_POSTAL_CODE = "postalCode";
        private static final String TAG_COUNTRY = "country";
        private static final String TAG_LAT = "lat";
        private static final String TAG_LNG = "lng";
        private static final String TAG_DISTANCE = "distance";



        public InputStream is = null;
        public JSONObject jObj;
        public String json = "";



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
            pDialog = new ProgressDialog(AddCulinary.this);
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

                JSONArray venues = jObj.getJSONObject("response").getJSONArray("venues");
                for(int i = 0; i < venues.length(); i++){
                    // parcours du tableau de venues
                    JSONObject ven = venues.getJSONObject(i);

                    // stocker chaque item du JSONObject venue dans une variable de type String
                    String id = ven.getString(TAG_ID);
                    String name = ven.getString(TAG_NAME);


                    // récupérer le JSONObject phone qui contient deux items
                    JSONObject location = null;
                    try {
                        location = ven.getJSONObject(TAG_LOCATION);
                    } catch (JSONException e) {
                        Log.e("ECHO!!", "Gak bisa ambil location: " + e.toString());
                        e.printStackTrace();
                    }
                    //String address = location.getString(TAG_ADDRESS);
                    String address = "";
                    try {
                        JSONArray formattedAddress = location.getJSONArray("formattedAddress");
                        for(int n = 0; n < formattedAddress.length(); n++){
                            if(n==0){
                                address += formattedAddress.getString(n);
                            }else {
                                address += ", "+formattedAddress.getString(n);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("ECHO!!", "Gak bisa ambil formattedAddress: " + e.toString());
                        e.printStackTrace();
                    }

                    //String crossStreet= location.getString(TAG_CROSS_STREET);
                    //String city= location.getString(TAG_CITY);
                    //String state= location.getString(TAG_STATE);
                    //String postalCode= location.getString(TAG_POSTAL_CODE);
                    //String country= location.getString(TAG_COUNTRY);
                    String lat= location.getString(TAG_LAT);
                    //String lat = "-7.00";
                    String lng= location.getString(TAG_LNG);
                    //String lng = "100.00";
                    //String distance= location.getString(TAG_DISTANCE);



                /*if(jsonObjLoc.has("myAddress")) { // name of field to look for

                       myTextAddress = jsonObjLoc.getString("address");
            }*/

                    // créer une HashMap pour ajouter les informations dans chaque item
                    HashMap<String, String> map = new HashMap<String, String>();

                    // insérer dans la HashMap les données que l’on veut afficher sous la forme de clé/valeur
                    map.put(TAG_ID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_ADDRESS, address);
                    //map.put(TAG_CITY, city);
                    map.put(TAG_LAT, lat);
                    map.put(TAG_LNG, lng);
                    //map.put(TAG_DISTANCE, distance);

                    venuesList.add(map);
                }

                ListAdapter adapter =
                        new SimpleAdapter(AddCulinary.this, venuesList,R.layout.add_culinary,
                                new String[] { TAG_ID, TAG_NAME, TAG_ADDRESS, TAG_LAT, TAG_LNG },
                                new int[] {R.id.text_id, R.id.text_name, R.id.text_address, R.id.text_lat, R.id.text_lng });



                setListAdapter(adapter);
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        //Mengambil nilai dari ListView yang di Click
                        String id_fsq = ((TextView) view.findViewById(R.id.text_id)).getText().toString();
                        String venue_fsq = ((TextView) view.findViewById(R.id.text_name)).getText().toString();
                        String address_fsq = ((TextView) view.findViewById(R.id.text_address)).getText().toString();
                        String lat_fsq = ((TextView) view.findViewById(R.id.text_lat)).getText().toString();
                        String long_fsq = ((TextView) view.findViewById(R.id.text_lng)).getText().toString();
                        //Membuat intent untuk menampilkan activity Detail
                        //Selain itu Intent ini juga digunakan untuk mengirimkan suatu data



                        Intent in = new Intent(AddCulinary.this, AddVenue.class);


                        Bundle b = new Bundle();

                        b.putString("fsqId", id_fsq);
                        b.putString("fsqVenue", venue_fsq);
                        b.putString("fsqAddress", address_fsq);
                        b.putString("fsqLat", lat_fsq);
                        b.putString("fsqLong", long_fsq);
                        b.putString("idUser", idUserLogin);

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

    public void search(View view){
        final EditText keySearch = (EditText) findViewById(R.id.TextSearch);
        strSearch = keySearch.getText().toString();


        Intent sr=new Intent(AddCulinary.this,AddCulinarySearch.class);
        Bundle s = new Bundle();
        s.putString("search", strSearch);
        sr.putExtras(s);
        startActivity(sr);
        finish();
    }
}
