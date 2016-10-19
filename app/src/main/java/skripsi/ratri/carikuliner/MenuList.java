package skripsi.ratri.carikuliner;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;

import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;

import android.view.View;


import skripsi.ratri.service.JSONparser;


public class MenuList extends ListActivity{

    private String TAG_menu="menu";
    private String TAG_nama="nama";
    private String TAG_harga="harga";

    private String url = "http://android.solfagaming.com/search_menu.php?name=";
    private  String urlmenu="";
    private String id_kuliner;

    JSONArray getMenu=null;
    // panggil class parser
    ArrayList<HashMap<String, String>> menuList = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_header);

       Bundle b = getIntent().getExtras();
        id_kuliner = b.getString("parseId");

        urlmenu=url+id_kuliner;

        JSONparser jParser = new JSONparser();

        JSONObject json = jParser.getJSONFromUrl(urlmenu);

        try {
            TAG_menu = "menu";
            getMenu = json.getJSONArray(TAG_menu);

            for(int i = 0; i < getMenu.length(); i++){
                JSONObject ar = getMenu.getJSONObject(i);

                String nama = ar.getString(TAG_nama);
                String harga = ar.getString(TAG_harga);

                HashMap<String, String> map = new HashMap<>();
                map.put(TAG_nama, nama);
                map.put(TAG_harga, harga);
                menuList.add(map);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.adapter_listview();
    }
    public void adapter_listview() {
        // tampilkan ke listadapter
        ListAdapter adapter = new SimpleAdapter(this, menuList,
                R.layout.menu, new String[]{TAG_nama, TAG_harga},
                new int[]{R.id.text_nama, R.id.text_harga});

        setListAdapter(adapter);


    }

}
