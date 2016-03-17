package cisco.demo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by irygiels on 15.03.16.
 */
public class BeaconSimplified extends Service {


    public BeaconSimplified() {
    }

    String BroadcastActionName;


    //Tutaj wywolywana jest usluga
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Beacon service starting", Toast.LENGTH_SHORT).show();
        //Nazwa akcji dla Android Manifest
        BroadcastActionName = "BEACON";
        new StartRanging(getApplicationContext());
        //startSendingKnownBeaconsToServer(); //tu bedzie mozna wywolac metode, na razie nie robi nic :)
        //cleanOldBeaconsAfter(time); //wyczysc liste beaconow po jakims czasie (do ustawienia wyzej)

        //new StartRangingBeacons(getApplicationContext());

        //Log.i("BEACON", "started ranging");

        //wykorzystujac obiekt klasy UDPSocketListiner moge szybko otwierac sockety UDP w kazdej z klas.
        //Motoda "startListenForUDPBroadcast" tworzy thread i otwiera socket. Port nasłuchuje broadcast (255.255.255.255), argument metody "PORT" specyfikuje port nasluchiwania.

        //IntentFilter przyjmuje argument, ktory jest nazwa akcji. //Powiazane z mMessageReceiver
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}