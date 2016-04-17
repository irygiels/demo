package cisco.demo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class RadarActivity extends AppCompatActivity {
    int n;
    int k;
    String user;
    boolean stan;
    boolean stanBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                user= null;
            } else {
                user= extras.getString("username");
            }
        } else {
            user= (String) savedInstanceState.getSerializable("username");
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        k = prefs.getInt("BEACON_COUNT", 0);
        n = prefs.getInt("NUMBER_I_NEED", 0); //no id: default value

        setContentView(R.layout.activity_radar);
        if(user!=null){
        setTitle("You are currently logged as: "+ user);}
        else{setTitle("You are currently logged as: ADMIN");}

        final RandomTextView randomTextView = (RandomTextView) findViewById(
                R.id.random_textview);

        randomTextView.setOnRippleViewClickListener(
                new RandomTextView.OnRippleViewClickListener() {
                    @Override
                    public void onRippleViewClicked(View view) {
                        RadarActivity.this.startActivity(
                                new Intent(RadarActivity.this, BeaconFinder.class));
                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    randomTextView.removeAllViewsInLayout();
                    if (k != 0) {
                        for (int i = 0; i < k; i++) {
                            randomTextView.addKeyWord("beacon no. " + String.valueOf(i+1));
                            randomTextView.show();
                        }
                    } else {
                        for (int i = 0; i < n; i++) {
                            randomTextView.addKeyWord("beacon no. " + String.valueOf(i+1));
                            randomTextView.show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2 * 1000);

        isInZone();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_radar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void isInZone(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                refreshState();
                            }
                        });
                        Thread.sleep(5000);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    private void refreshState(){
        stanBefore = stan;
        stan = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("INZONE", false);
        if(stan && !stanBefore){
            if(user!=null){
            Toast.makeText(RadarActivity.this, "HI " + user + "!", Toast.LENGTH_SHORT).show();}
            else {
                Toast.makeText(RadarActivity.this, "HI ADMIN!", Toast.LENGTH_SHORT).show();
            }
        }

    }


}