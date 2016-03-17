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

public class RadarActivity extends AppCompatActivity {
    int n;
    int k;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        k = prefs.getInt("BEACON_COUNT", 0);
        n = prefs.getInt("NUMBER_I_NEED", 0); //no id: default value
        setContentView(R.layout.activity_radar);
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



}