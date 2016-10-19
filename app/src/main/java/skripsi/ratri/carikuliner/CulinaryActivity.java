package skripsi.ratri.carikuliner;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListAdapter;

import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;

import android.view.View;
import android.widget.TextView;
import android.util.Log;

import skripsi.ratri.service.JSONparser;


public class CulinaryActivity extends ListActivity{

    private String TAG_nama="nama";
    private String TAG_id="id";
    private String TAG_longitude="longitude";
    private String TAG_latitude="latitude";
    private String TAG_alamat="alamat";
    private String url = "http://android.solfagaming.com/search.php?name=";
    private String urlculinary="";
    private double getUserLat;
    private double getUserLong;
    private String PlaceLat;
    private String PlaceLong;
    private String getIdKul;
    private String tempat;

    JSONArray getCulinary=null;
    // panggil class parser
    ArrayList<HashMap<String, String>> culinaryList = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
       setContentView(R.layout.main_detail);

        Bundle in = getIntent().getExtras();

        getIdKul = in.getString("parseId");
         getUserLat = in.getDouble("userLat");
        getUserLong = in.getDouble("userLong");

        urlculinary=url+getIdKul;

        JSONparser jParser = new JSONparser();

        JSONObject json = jParser.getJSONFromUrl(urlculinary);

        try {
            String TAG_tempat = "tempat";
            getCulinary = json.getJSONArray(TAG_tempat);

            for(int i = 0; i < getCulinary.length(); i++){
                JSONObject ar = getCulinary.getJSONObject(i);

                String nama = ar.getString(TAG_nama);
                String id = ar.getString(TAG_id);
                String longitude = ar.getString(TAG_longitude);
                String latitude = ar.getString(TAG_latitude);
                String alamat = ar.getString(TAG_alamat);

                PlaceLong = longitude;
                PlaceLat = latitude;
                tempat = nama;

                HashMap<String, String> map = new HashMap<>();
                map.put(TAG_nama, nama);
                map.put(TAG_id, id);
                map.put(TAG_longitude, longitude);
                map.put(TAG_latitude, latitude);
                map.put(TAG_alamat, alamat);
                culinaryList.add(map);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.adapter_listview();
    }
    public void adapter_listview() {
        // tampilkan ke listadapter
        ListAdapter adapter = new SimpleAdapter(this, culinaryList,
                R.layout.culinary_activity, new String[]{TAG_nama, TAG_id, TAG_longitude, TAG_latitude, TAG_alamat},
                new int[]{R.id.text_nama, R.id.text_id, R.id.text_longitude, R.id.text_latitude,R.id.address});

        setListAdapter(adapter);


    }
    public void proses(View view)
    {


        Intent menu=new Intent(CulinaryActivity.this,MenuList.class);
        Bundle b = new Bundle();
        b.putString("parseId", getIdKul);
        menu.putExtras(b);
        startActivity(menu);
    }
    public void map(View view)
    {
        try {
            double dLat = Double.parseDouble(PlaceLat);
            double dLong = Double.parseDouble(PlaceLong);

            Intent map=new Intent(CulinaryActivity.this,ViewMap.class);
            Bundle b = new Bundle();

            b.putDouble("userLong", getUserLong);
            b.putDouble("userLat", getUserLat);
            b.putDouble("placeLat", dLat);
            b.putDouble("placeLong", dLong);
            b.putString("venue",tempat);
            map.putExtras(b);
            Log.d("SUKSESKONVERT","STARTING ACT : "+dLat+", "+dLong+" USERLAT:"+getUserLat+", USERLONG:"+getUserLong);
            startActivity(map);
        } catch (NumberFormatException e) {
            // p did not contain a valid double
            Log.d("eror_konvert","gagal konvert double Userlat: "+TAG_latitude+" , UserLong: "+TAG_longitude);
        }


    }
}
