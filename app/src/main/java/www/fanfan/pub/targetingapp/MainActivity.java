package www.fanfan.pub.targetingapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements SensorEventListener, SeekBar.OnSeekBarChangeListener {


    // define the display assembly compass picture
    private ImageView image;
    // record the compass picture angle turned
    private float currentDegree = 0f;
    // device sensor manager
    private SensorManager mSensorManager;
    private LocationManager myLocationManager;
    String PROVIDER = LocationManager.GPS_PROVIDER;


    TextView tvHeading, distanceText, lat_TextView, lng_TextView;

    private SeekBar distanceBar;
    private int distance;
    public static double la;
    public static double lng;

    private final double M_IN_KM = 6378.1;
    private final double M_IN_MILES = 3959;
    private final double M_IN_FEET = 20903520;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image = (ImageView) findViewById(R.id.pointer);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xAT9ru3zhAMjXlP4tiCS0ENf4BYLgnZr4sgdh8ua", "xZ1MLEcHEUvJQM3PdtQvxGXnFR3sRdFZM8vAUkkq");

        myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        distanceBar = (SeekBar) findViewById(R.id.seekBar1);
        distanceBar.setOnSeekBarChangeListener(this);// set seekbar listener.
        distanceText = (TextView) findViewById(R.id.textViewProgress);
        lat_TextView = (TextView) findViewById(R.id.lat);
        lng_TextView = (TextView) findViewById(R.id.lng);


        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        myLocationManager.requestLocationUpdates(
                PROVIDER,//provider
                0, // minTime
                0, // minDistance
                myLocationListener);
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
        myLocationManager.removeUpdates(myLocationListener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated

        float degree = Math.round(event.values[0]);
        //float degree = Math.round(event.values[1]);
        //float degree = Math.round(event.values[2]);
        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                        -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
        0.5f);


            // how long the animation will take place
            ra.setDuration(210);
            // set the animation after the end of the reservation status
            ra.setFillAfter(true);
            // Start the animation
        image.startAnimation(ra);

        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }


    //Check which seekBar is operating
    private TextView seekBar(SeekBar seekbarText) {
        if (seekbarText == distanceBar) return distanceText;
        else return distanceText;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBar(seekBar).setText("");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        seekBar.setSecondaryProgress(seekBar.getProgress()); // set the shade of the previous value.
        distance = seekBar.getProgress();
    }
    //check seeker value
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        TextView a = seekBar(seekBar);
        a.setText(Integer.toString(progress));
    }


    public boolean isInteger(String s){
        Scanner sc = new Scanner((s.trim()));
        return sc.hasNextInt();
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



    private LocationListener myLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showMyLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    };

    private void showMyLocation(Location l) {
        if (l == null) {
            lng_TextView.setText("No Location!");
        } else {
            la = l.getLatitude();
            lng = l.getLongitude();
            lng_TextView.setText("Latitude: " + lng);
            lat_TextView.setText("Longitude: " + la);
        }
    }

    private Location getLastKnownLocation() {
        myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = myLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

                Location l = myLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
        }
        return bestLocation;
    }

    public void getposition (View view)
    {
        Location location= getLastKnownLocation();
        showMyLocation(location);
        lngCalculation();
    }


    public void pushToParse(){

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ParseObject GpsCoordinate = new ParseObject("GpsCoordinate");
        Double lat = MainActivity.la;
        Double lon = MainActivity.lng;
        GpsCoordinate.put("latitud", lat);
        GpsCoordinate.put("longtitude", lon);
        GpsCoordinate.saveInBackground();
    }

    public double degreeToRadius(double degree){

        return degree*PI/180;
    }
    final double PI = (float)Math.PI;
    public double coordinateToRadius(double degree){

        return degree*(PI*180);
    }

    public void lngCalculation(){

        double brng = degreeToRadius(currentDegree);
        double dis = distance; //distance
        double lat = coordinateToRadius(la);
        double ln = coordinateToRadius(lng);
        double R = M_IN_KM;

        double lat2 = Math.asin( Math.sin(lat)*Math.cos(dis/R) +
                Math.cos(lat)*Math.sin(dis/R)*Math.cos(brng));

        double lng2 = ln + Math.atan2(Math.sin(brng)*Math.sin(dis/R)*Math.cos(lat),
                Math.cos(dis/R)-Math.sin(lat)*Math.sin(lat2));

        System.out.print(lat2);
        System.out.print(lng2);


    }




}
