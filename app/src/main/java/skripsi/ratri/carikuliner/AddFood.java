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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ratri on 2/1/2015.
 */
public class AddFood extends Activity{
    JSONParser jParser = new JSONParser();
    ProgressDialog pDialog;
    private static String url = "http://android.solfagaming.com/add_food.php";
    private String method = "GET";
    private String getId;
    private EditText getNama;
    private EditText getHarga;
    private String strMenu, strNama, strHarga, kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String method = "GET";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_food);

        ImageButton quit = (ImageButton) findViewById(R.id.quit);

        getNama = (EditText) findViewById(R.id.edit_nama);
        getHarga = (EditText) findViewById(R.id.edit_harga);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent qt = new Intent(AddFood.this, MainActivity.class);
                startActivity(qt);
            }
        });
    }


    public void klikHasilRadio(View v) {

        RadioGroup rgJenKel = (RadioGroup) findViewById(R.id.RadioGroupMenu);

        //ambil pilihan user
        int idPil = rgJenKel.getCheckedRadioButtonId();
        Toast.makeText(this,"idPil : "+idPil,Toast.LENGTH_LONG).show();
        //isi string

        if (idPil==R.id.radioMakanan) {
            strMenu = "1";
            Toast.makeText(this,"strMenu : 1",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"strMenu : 2",Toast.LENGTH_LONG).show();
        }}

    public void finish (View view)
    {
        Intent in = new Intent(AddFood.this, MainActivity.class);
        startActivity(in);
    }


    public void tambah (View view)
    {
        RadioGroup rg1 = (RadioGroup) findViewById(R.id.RadioGroupMenu);
        String selection = "not selected";
        if(rg1.getCheckedRadioButtonId()!=-1){
            int id= rg1.getCheckedRadioButtonId();
            View radioButton = rg1.findViewById(id);
            int radioId = rg1.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rg1.getChildAt(radioId);
            selection = (String) btn.getText();

            int idPil = rg1.getCheckedRadioButtonId();
            strNama = getNama.getText().toString();
            strHarga = getHarga.getText().toString();
            kategori = selection;
            Bundle b = getIntent().getExtras();
            getId = b.getString("parseId");
            new input().execute();
        }else{
            Toast.makeText(this,"Silakan pilih Makanan atau Minuman terlebih dahulu.",Toast.LENGTH_LONG).show();
        }
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
            pDialog = new ProgressDialog(AddFood.this);
            pDialog.setMessage("LOADING...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_kuliner", getId));
            params.add(new BasicNameValuePair("nama", strNama));
            params.add(new BasicNameValuePair("harga", strHarga));
            params.add(new BasicNameValuePair("id_kategori", kategori));

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
                Toast.makeText(getApplicationContext(), "Sukses Ditambah!!! " + message+ "kat:"+kategori, Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddFood.this, AddFood.class);
                Bundle b = new Bundle();

                b.putString("parseId", getId);

                i.putExtras(b);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), message +"id:"+getId+"nama :"+strNama+"harga:"+strHarga+"kategori:"+kategori+ " Gagal !!! ", Toast.LENGTH_LONG).show();
                Log.d("error_brooo", message + " Gagal !!! ");
            }
        }
    }
}


