package skripsi.ratri.carikuliner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import skripsi.ratri.service.DBLib;


public class MainActivity extends Activity {
    GpsService  gps;
    private int count;
    private DBLib db = new DBLib(this);
    private String namaLogin,emailLogin, idLogin;
    public double latitude;
    public double longitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        gps = new GpsService(MainActivity.this);
        if (gps.canGetLocation()) {
            // ambil latitude dan longitude
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            // jika GPS tidak aktif
            gps.showSettingAlert();
        }

        final EditText keySearch = (EditText) findViewById(R.id.editSearch);
        /*final EditText latitude = (EditText) findViewById(R.id.editLatitude);
        final EditText longitude = (EditText) findViewById(R.id.editLongitude);*/
        ImageButton btnSearch = (ImageButton) findViewById(R.id.buttonSearch);
        TextView btnAdd = (TextView) findViewById(R.id.buttonAdd);
        TextView creditText = (TextView) findViewById(R.id.credit);
        TextView venueText = (TextView) findViewById(R.id.venue);
        ImageButton creditBtn = (ImageButton)findViewById(R.id.btn_credit);
        ImageButton tempatBtn = (ImageButton)findViewById(R.id.btn_tempat);
        //ImageButton addBtn = (ImageButton) findViewById(R.id.addbtn);
        //TextView credit = (TextView) findViewById(R.id.text_credit);
        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);
        Cursor cr = db.getUser();
        count = cr.getCount();
        if (count == 0) {
            login.setText("LOGIN");
            register.setText("REGISTER");
        } else {
            if (cr.moveToFirst()) {
                namaLogin = cr.getString(cr.getColumnIndex("name"));
                emailLogin = cr.getString(cr.getColumnIndex("email"));
                /*idLogin = cr.getString(cr.getColumnIndex("id"));*/
            }
            //Toast.makeText(getApplicationContext(),"Nama : "+ cr.getString(cr.getColumnIndex("name")),Toast.LENGTH_LONG).show();
            login.setText("LOGOUT");//hilangkan button login
            register.setText("");
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String strSearch = keySearch.getText().toString();
               /* String strLatitude= latitude.getText().toString();
                String strLongitude= longitude.getText().toString();*/
                // buat class object dari GpsService


                // dicek dulu apakah GPSnya idup



                    Intent i = new Intent(MainActivity.this, SearchActivity.class);
                    Bundle b = new Bundle();

                    b.putString("parseSearch", strSearch);
                    b.putDouble("parseLatitude", longitude);
                    b.putDouble("parseLongitude", latitude);
                    i.putExtras(b);
                    startActivity(i);

            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if (count == 0) {
                    Toast.makeText(getApplicationContext(), "Login dulu broo..",
                            Toast.LENGTH_LONG).show();
                    Intent menu = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(menu);
                    finish();
                } else {


                        Intent i = new Intent(MainActivity.this, AddCulinary.class);
                        Bundle b = new Bundle();
                        b.putString("idUser", idLogin);

                        b.putDouble("parseLatitude", longitude);
                        b.putDouble("parseLongitude", latitude);
                        i.putExtras(b);
                        startActivity(i);

                }
            }
        });



     creditBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent crd=new Intent(MainActivity.this,CreditActivity.class);
             startActivity(crd);
             finish();
         }
     });
     creditText.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent crd=new Intent(MainActivity.this,CreditActivity.class);
             startActivity(crd);
             finish();

         }
     });
        venueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ven=new Intent(MainActivity.this,VenueCulinary.class);
                Bundle ve = new Bundle();

                ve.putDouble("parseLatitude", longitude);
                ve.putDouble("parseLongitude", latitude);
                ven.putExtras(ve);
                startActivity(ven);
                finish();

            }
        });

     tempatBtn.setOnClickListener(new View.OnClickListener()
       {
          @Override
            public void onClick(View v) {

                 Intent ven=new Intent(MainActivity.this,VenueCulinary.class);
                                          Bundle ve = new Bundle();

                                          ve.putDouble("parseLatitude", longitude);
                                          ve.putDouble("parseLongitude", latitude);
                                          ven.putExtras(ve);
                                          startActivity(ven);
                                          finish();

                                      }
                                  }
     );
    }



    public void login(View view)
    {
        if(count==0){
            Intent menu=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(menu);
            finish();
        }else{
            db = new DBLib(getApplicationContext());
            db.clearUser();
            db.close();
            if(Build.VERSION.SDK_INT >=11){
                recreate();
            }else{
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(), "Logout success..",
                        Toast.LENGTH_LONG).show();
                startActivity(intent);
                overridePendingTransition(0,0);
            }

        }
    }
   /* public void credit(View view){
        Intent crd=new Intent(MainActivity.this,CreditActivity.class);
        startActivity(crd);
        finish();
    }*/

    public void register(View view) {

        Intent reg=new Intent(MainActivity.this,Register.class);
        startActivity(reg);
        finish();
    }

    }








