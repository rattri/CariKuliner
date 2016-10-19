package skripsi.ratri.carikuliner;

import java.util.ArrayList;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
/**
 * Created by ratri on 1/10/2015.
 */
public class ViewMap extends FragmentActivity  {
    GoogleMap mMap;
    MapDirection md;
    GpsService  gps;
    private double getUserLat;
    private double getUserLong;
    private double getPlaceLat;
    private double getPlaceLong;
    private String Venue;

    private double userLat;
    private double userLong;
    private double placeLat;
    private double placeLong;
    private double latitude;
    private double longitude;
    private double dUserLat;
    private double dUserLong;

    LatLng fromPosition;
    LatLng toPosition;

    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_map);

        gps = new GpsService(ViewMap.this);


/*
        userLat = Double.valueOf(getUserLat);
        userLong = Double.valueOf(getUserLong);
        placeLat = Double.parseDouble(getPlaceLat);
        placeLong = Double.parseDouble(getPlaceLong);
*/
        Log.d("start_act_view_map",""+getUserLat+","+getUserLong+","+getPlaceLat+","+getPlaceLong);
        // dicek dulu apakah GPSnya idup

        if (gps.canGetLocation()) {
                        // ambil latitude dan longitude
           latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            }
            else
            {
                // jika GPS tidak aktif
                gps.showSettingAlert();
            };

        Bundle in = getIntent().getExtras();

        getPlaceLat = in.getDouble("placeLat");
        getPlaceLong = in.getDouble("placeLong");
        Venue=in.getString("venue");
        fromPosition = new LatLng(latitude,longitude);
        toPosition = new LatLng(getPlaceLat, getPlaceLong);

        if (android.os.Build.VERSION.SDK_INT > 9) {

        }

        md = new MapDirection();
        mMap = ((SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.viewmap)).getMap();

        LatLng coordinates = new LatLng(latitude,longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16));


        mMap.addMarker(new MarkerOptions().position(fromPosition).title("Start"));
        mMap.addMarker(new MarkerOptions().position(toPosition).title(Venue));

        Document doc = md.getDocument(fromPosition, toPosition, MapDirection.MODE_DRIVING);
        int duration = md.getDurationValue(doc);
        String distance = md.getDistanceText(doc);
        String start_address = md.getStartAddress(doc);
        String copy_right = md.getCopyRights(doc);

        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);

        for(int i = 0 ; i < directionPoint.size() ; i++) {
            rectLine.add(directionPoint.get(i));
        }

        mMap.addPolyline(rectLine);
    }
}
