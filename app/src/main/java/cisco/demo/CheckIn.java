package cisco.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by irygiels on 16.03.16.
 */
public class CheckIn extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    ArrayList<Set<String>> lista = new ArrayList<>();
    Set<String> set = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        textView = (TextView)findViewById(R.id.txt);
        imageView = (ImageView)findViewById(R.id.image);


        String whereAmI;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                whereAmI= null;
            } else {
                whereAmI= extras.getString("chosen");
            }
        } else {
            whereAmI= (String) savedInstanceState.getSerializable("chosen");
        }

        assert whereAmI != null;
        if(whereAmI.contains("starbucks")){
            imageView.setImageResource(R.drawable.starbucks);}
        else if(whereAmI.contains("kfc")){
            imageView.setImageResource(R.drawable.kfc);}
        else if(whereAmI.contains("burger")){
            imageView.setImageResource(R.drawable.burger);}


    }


    public void checkIn(View view) throws InterruptedException {
        if(textView.getText().toString().equals("CHECK IN! (3)")){
            lista = new ArrayList<>();
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading coordinates...");
            progress.show();

            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {
                    // You don't need anything here
                }

                public void onFinish() {
                    progress.dismiss();
                }
            }.start();
// To dismiss the dialog
            getDistanceSet(0);

            textView.setText("CHECK IN! (2)");}
        else if(textView.getText().toString().equals("CHECK IN! (2)")){
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading coordinates...");
            progress.show();

            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {
                    // You don't need anything here
                }

                public void onFinish() {
                    progress.dismiss();
                }
            }.start();
            getDistanceSet(1);

            textView.setText("CHECK IN! (1)");}
        else if(textView.getText().toString().equals("CHECK IN! (1)")){
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading coordinates...");
            progress.show();

            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {
                    // You don't need anything here
                }

                public void onFinish() {
                    progress.dismiss();
                }
            }.start();
            getDistanceSet(2);

            Toast.makeText(CheckIn.this, "Your zone has been successfully saved", Toast.LENGTH_LONG).show();
            Log.d("LISTA", lista.toString());
            getCoordinates(lista);
            Intent i = new Intent(this, BeaconFinder.class);
            startActivity(i);
        }
    }

    public void getDistanceSet(int i){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        set = prefs.getStringSet("SET", new HashSet<String>());
        TreeSet<String> treeSet = new TreeSet<String>(set);
        try{
            assert !treeSet.isEmpty();
            lista.add(i, treeSet);}
        catch (Exception e){ e.printStackTrace(); }
    }

    public void getCoordinates(ArrayList<Set<String>> lista){
        ArrayList<String> records = new ArrayList<>();
        ArrayList<String> results = new ArrayList<>();
        for(int j = 0; j<lista.size(); j++){
            records.add(Arrays.toString(lista.get(j).toString().split(" ,"))); //biore wers
            String[] str = records.get(j).split(", ");
            for(int k = 0; k<str.length; k++){
                if(!str[k].equals(""))    {
                    String s = str[k].replaceAll("[^,\\w\\s]","");
                    results.add(s);
                Log.d("RESULTS", results.get(results.size() - 1));
                }
            }

        }
        getZone(results);
    }

    private void getZone(ArrayList<String> results){
        ArrayList <String> macDistanceFunction = new ArrayList<>();
        ArrayList <String> minDistances = new ArrayList<>();
        ArrayList <String> maxDistances = new ArrayList<>();
        ArrayList <String> tempMacs = new ArrayList<>();
        for(int i = 0; i<results.size(); i++){
            if(results.get(i).length()!=0) {    //jesli tablica nie jest pusta
                String temp = results.get(i).substring(0, 12);  //sprawdzam pierwsze znaki, czyli macaddress
                if (!tempMacs.contains(temp)) { //jesli nie mam jeszcze takiego macaddress
                    tempMacs.add(temp);  //dodaje go do tablicy z macaddresses
                    //macDistanceFunction.add(results.get(i).substring(0, 17)); //do tablicy z dystansami dodaje macaddress+distance
                    minDistances.add(results.get(i).substring(0, 17).replace(",", "."));
                    maxDistances.add(results.get(i).substring(0, 17).replace(",", "."));   //okej mam tablice z maxdistances i mindistances wraz z macaddress


                } else { //jesli mam juz ten mac address
                    for(int j = 0; j < minDistances.size(); j++) { //to sprawdzam w tablicy rekordy zawierajace dany macaddress
                        if (minDistances.get(j).startsWith(temp) && Double.valueOf(results.get(i).substring(13, 17).replace(",", ".")) < Double.valueOf(minDistances.get(j).substring(13, 17).replace(",", "."))) {
                            //jesli distance jest mniejszy
                            minDistances.add(j, results.get(i).substring(13, 17).replace(",", "."));
                        }
                    }
                    Log.d("DISTANCE MIN", minDistances.get(0));
                    for(int j = 0; j < maxDistances.size(); j++) {
                        if(maxDistances.get(j).startsWith(temp) && Double.valueOf(results.get(i).substring(13,17).replace(",", ".")) > Double.valueOf(maxDistances.get(j).substring(13,17).replace(",", "."))){
                            maxDistances.add(j, results.get(i).substring(13,17).replace(",", "."));
                            //jesli distance jest wiekszy
                        }                                                                                                                                                //macDistanceFunction.add(t, results.get(i).substring(12, 17));
                    }
                    Log.d("DISTANCE MAX", maxDistances.get(0));
                }
            }
        }




    }


}
