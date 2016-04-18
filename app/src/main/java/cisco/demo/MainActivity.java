package cisco.demo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//ta klasa odpowiada tylko za ekran glowny - jesli BT nie jest wlaczony - pytam o access
//jest tu po to, zeby mozna bylo ja scustomizowac i zeby byla pozniej ladna

public class MainActivity extends ActionBarActivity {

    private int REQUEST_ENABLE_BT = 1;
    private Button startbutton;
    private Context that;
    private BluetoothAdapter bluetoothAdapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar barHide = getSupportActionBar();

        if (barHide != null) {
            barHide.hide();
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothNotEnabled()) askForBluetoothAccess();

        that=this;
        setContentView(R.layout.activity_main);
        startbutton = (Button)findViewById(R.id.login_button);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.cisco);

        Intent BeaconRangingAndDisplay = new Intent(that, BeaconSimplified.class);
        that.startService(BeaconRangingAndDisplay);

        Intent MQTT = new Intent(that, MQTTService.class);
        that.startService(MQTT);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean bluetoothNotEnabled(){
        return !bluetoothAdapter.isEnabled();
    }

    public void askForBluetoothAccess(){
        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
    }


    public void startRanging(View view){
        Intent startRangingaAndDisplaying = new Intent(that, ChooseMode.class);
        startActivity(startRangingaAndDisplaying);
        //Toast.makeText(this, "dziala", Toast.LENGTH_LONG).show();

    }



}