package skripsi.ratri.carikuliner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
//import android.widget.Toast;

/**
 * Created by ratri on 3/9/2015.
 */
public class CreditActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit);


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

}
