package com.example.rohitsingla.distancecalculator;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MyActivity extends Activity implements ConnectionCallbacks,OnConnectionFailedListener, LocationListener{

    // LogCat tag
    private static final String TAG = MyActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    //boolean flag to check if it location updating was just started : to handle this scenario:-
    /*if a user starts getting updates from p1 and goes to p2 and stops getting updates there. Now
    if user goes to point p3(far from p2) without taking location updates and starts getting updates from p3,
        then distance between p2 and p3 should not get added to distance starting from p3.*/
    private boolean mLocationUpdatesJustStarted = true;

    //boolean to check if this activity is in foreground to avoid unnecessary toasts
    private boolean isCurrentActivityInForeground = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    //to save distance travelled
    private static double DISTANCE_TRAVELLED = 0;
    private static double DISTANCE_TRAVELLED_LOCATION_API = 0;
    private static double DISTANCE_TRAVELLED_RADIUS = 0;

    // UI elements
    private TextView lblLocation,distTravelled;
    private Button btnShowLocation, btnStartLocationUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        lblLocation = (TextView) findViewById(R.id.lblLocation);
        distTravelled = (TextView) findViewById(R.id.distanceTravelled);
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnStartLocationUpdates = (Button) findViewById(R.id.btnLocationUpdates);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        // Show location button click listener
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                displayLocation();
            }
        });

        // Toggling the periodic location updates
        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                togglePeriodicLocationUpdates();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Inside onResume()");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //setting foreground flag to true
        isCurrentActivityInForeground = true;

        checkPlayServices();
        Log.d(TAG, "Inside onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Inside onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Inside onPause()");

        //as another activity comes into foreground, making the foreground flag as false
        isCurrentActivityInForeground = false;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "Inside onDestroy()");
        //stopping location updates
        stopLocationUpdates();
        //disconnecting google API client
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            //getting previous location from text view if location updates are not just started
            Log.d(TAG,"mLocationUpdatesJustStarted = "+mLocationUpdatesJustStarted);
            if(!mLocationUpdatesJustStarted){
                String previousLocation = lblLocation.getText().toString();
                double previousLatitude, previousLongitude;
                if(previousLocation!=null && !previousLocation.isEmpty()){
                    int index = previousLocation.indexOf(',');
                    if(index!=-1){      //In the error printed in below else block there is no comma, so parsing error will not occur
                        previousLatitude = Double.parseDouble(previousLocation.substring(0,index));
                        previousLongitude = Double.parseDouble(previousLocation.substring(index+2));    //one space is there
                        Log.d(TAG, "Calculating distance between (" + previousLatitude + ", " + previousLongitude + ") and (" + latitude + "," + longitude + ") in metres.");
                        //updating distance travelled
                        double distanceFormula, distanceLoc, distanceRad;
                        distanceFormula = distance(previousLatitude,previousLongitude,latitude,longitude,"m");
                        distanceLoc = distanceLocationAPI(previousLatitude,previousLongitude,latitude,longitude,"m");
                        distanceRad = distanceUsingRadius(previousLatitude,previousLongitude,latitude,longitude,"m");
                        Log.d(TAG,"The three distances are : "+distanceFormula+", "+distanceLoc+", "+distanceRad);
                        DISTANCE_TRAVELLED += distanceFormula;
                        DISTANCE_TRAVELLED_LOCATION_API += distanceLoc;
                        DISTANCE_TRAVELLED_RADIUS += distanceRad;
                    }
                }
            }

            //update location updates just started flag
            if(mLocationUpdatesJustStarted)
                mLocationUpdatesJustStarted = false;

            lblLocation.setText(latitude + ", " + longitude);
            distTravelled.setText("Distance Travelled (as per geolocation) = "+DISTANCE_TRAVELLED +" mtrs"+
                    "\nDistance Travelled (as per Location) = "+DISTANCE_TRAVELLED_LOCATION_API +" mtrs"+
                    "\nDistance Travelled (using earth's radius) = "+DISTANCE_TRAVELLED_RADIUS +" mtrs");

        } else {

            lblLocation
                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
            btnStartLocationUpdates
                    .setText(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            //resetting distance travelled to 0 and updates just started flag to true
            DISTANCE_TRAVELLED = 0;
            DISTANCE_TRAVELLED_LOCATION_API = 0;
            DISTANCE_TRAVELLED_RADIUS = 0;
            mLocationUpdatesJustStarted = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
            btnStartLocationUpdates
                    .setText(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
        Log.d(TAG,"Starting location updates");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        Log.d(TAG,"Stopping location updates");
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        if(isCurrentActivityInForeground)
            Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

    /**
     * This function returns the distance(as per geodatasource.com definitions) between to geo locations in the given units as input
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @param unit unit in which distance is to be calculated
     * @return distance between two input points
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;          //this is in miles
        if (unit == "K") {                  //kilometres
            dist = dist * 1.609344;
        }else if (unit == "m"){             //metres
            dist = dist * 1.609344 * 1000;
        }

        return (dist);
    }

    /**
     * This function converts decimal degrees to radians
     * @param deg
     * @return angle in radians
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * This function converts radians to decimal degrees
     * @param rad
     * @return angle in decimal degrees
     */
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    /**
     * This function returns the distance(as per android.location.Location.distanceTo API definitions) between to geo locations in the given units as input
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @param unit unit in which distance is to be calculated
     * @return distance between two input points
     */
    private static double distanceLocationAPI(double lat1, double lon1, double lat2, double lon2, String unit){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        double dist = loc1.distanceTo(loc2); //in meters

        if (unit == "K") {                  //kilometres
            dist = dist/1000;
        }else if (unit == "M"){             //miles
            dist = (dist * 0.621371)/1000 ;
        }

        return dist;
    }

    private static double distanceUsingRadius(double lat1, double lon1, double lat2, double lon2, String unit){
        double Radius = 6371.00;// radius of earth in Km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double dist = Radius * c;           //in kilometres

        if (unit == "m") {                  //metres
            dist = dist * 1000;
        }else if (unit == "M"){             //miles
            dist = dist * 0.621371 ;
        }

        return dist;
    }

}
