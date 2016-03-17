package cisco.demo;

import android.app.ListActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.rtp.AudioStream;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

/**
 * Created by irygiels on 29.10.15.
 */
public class StartRanging {


    BeaconManager beaconManager;
    Region ourRegion;
    private final Double time = 0.05;
    private final long MINUTE = 60*1000;
    private volatile boolean threadsShouldBeRunning = true; //czy wszystko ok - boolean
    private Map<String, Timestamp> currentBeacons;
    private Map<String, List<RowBean>> macBeacons;
    int n;
    Context cont;
    private Map<String, String> distanceBeacons;



    public StartRanging(Context context) {
        cont=context;
        currentBeacons = new HashMap<String, Timestamp>();
        macBeacons = new HashMap<String, List<RowBean>>();
        distanceBeacons = new HashMap<String, String>();

        beaconManager = new BeaconManager(cont);
        beaconManager.setForegroundScanPeriod(1000, 2700);
        ourRegion = new Region("region", null, null, null);
        startRangingBeacons(cont);
        startSendingKnownBeaconsToServer();
        cleanOldBeaconsAfter(time);
        //connectBeacons();
    }

    public void startRangingBeacons(final Context context) {
        // Log.i("BEACON", "HALO");
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                for (Beacon beacon : beacons) {
                    String key = makeKey(beacon); //identyfikator beacona

                    Date date = new Date();
                    currentBeacons.put(key, new Timestamp(date.getTime())); //dodaje do listy wraz z timestampem
                    macBeacons.put(key, new ArrayList<RowBean>());
                    String distance = getDistance(beacon);
                    distanceBeacons.remove(key);
                    distanceBeacons.put(key, distance);
                }
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ourRegion);
                } catch (Throwable exc) {
                    Log.i("BEACON", "startRanging in region doesnt work");
                    exc.printStackTrace();

                }
            }
        });

    }


    public void connectBeacons() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ourRegion);
                } catch (Throwable exc) {
                    Log.i("BEACON", "startRanging in region doesnt work");
                    exc.printStackTrace();

                }
            }
        });
    }

    private String getDistance(Beacon beacon) {
        double distance = Math.min(Utils.computeAccuracy(beacon), 10.0); //do 10 m
        String wyn = String.format("%.2f", distance);
        return wyn;
    }
    public com.estimote.sdk.Utils.Proximity getProximity(Beacon beacon) {
        return Utils.proximityFromAccuracy(Double.valueOf(getDistance(beacon)));
    }

    //tutaj na podstawie wlasnosci beacona (major+minor) tworze jego identyfikator
    private String makeKey(Beacon beacon) {
        //
        //final String key = Integer.toString(beacon.getMajor()) + Integer.toString(beacon.getMinor());
        final String key = String.valueOf(beacon.getMacAddress());
        return key;
    }


    private void startSendingKnownBeaconsToServer() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadsShouldBeRunning) {
                    startRadar(cont);
                    send();
                    //addUser();
                    //co sie dzieje w tle
                    try {
                        sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    private void cleanOldBeaconsAfter(final Double timeInHours) {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (threadsShouldBeRunning) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Date date = new Date();
                    Timestamp twoHoursAgo = new Timestamp(date.getTime() - (long) (time * MINUTE));
                    try {
                        for (Map.Entry<String, Timestamp> entry : currentBeacons.entrySet()) { //jesli przez ostatnie 2 h nie pojawil sie beacon z danym identyfikatorem - usuwam go z listy
                            String key = entry.getKey();
                            Timestamp timestamp = entry.getValue();
                            if (timestamp.before(twoHoursAgo)) {
                                currentBeacons.remove(key); //usuwam
                                macBeacons.remove(key);
                            }
                        }
                    }catch (Exception e){e.printStackTrace(); }

                }
            }
        };

        t.start();
    }

    private void send(){

            List<RowBean> allBeaconsInRange = new ArrayList<RowBean>();

            for(String mac: macBeacons.keySet()){
                //mam wszystkie dostepne mac_add
                if(!allBeaconsInRange.contains(new RowBean(mac))) {
                    allBeaconsInRange.add(new RowBean(mac)); //jesli nie bylo takiego wczesniej - dodaje do listy
                }
            }
         n=allBeaconsInRange.size();
         Log.d("n value", String.valueOf(n));

    }

    public int getDataLength() {
        return n;
    }

    public void startRadar(Context context){
        int k = getDataLength();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("NUMBER_I_NEED", k); //InputString: from the EditText

        List<RowBean> allBeaconsInRange = new ArrayList<RowBean>();
        Set<String> set = new HashSet<String>();

        for(String mac: macBeacons.keySet()) {
            //mam wszystkie dostepne mac_add
            if (!allBeaconsInRange.contains(new RowBean(mac))) {
                allBeaconsInRange.add(new RowBean(String.valueOf(distanceBeacons.get(mac)))); //jesli nie bylo takiego wczesniej - dodaje do listy
                set.add(String.valueOf(distanceBeacons.get(mac)));
            }
        }

        editor.putStringSet("SET", set);
        editor.putInt("SIZE", allBeaconsInRange.size());
        editor.apply();
        Log.d("data value", String.valueOf(set));
        //context.startActivity(startRadarActivity);


    }


}
