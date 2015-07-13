package com.thoughtworks.rdlian.footprint.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.thoughtworks.rdlian.footprint.R;
import com.thoughtworks.rdlian.footprint.services.LocationService;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements LocationSource {

    public static String LOCATION_CHANGED_ACTION = "com.thoughtworks.rdlian.footprint.LOCATION_CHANGED_ACTION";

    @InjectView(R.id.map)
    MapView mapView;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private View.OnClickListener navigationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private AMap aMap;
    private UiSettings uiSettings;

    private LocationBroadCastReceiver locationBroadCastReceiver;
    private OnLocationChangedListener mListener;

    private Intent startLocationIntent;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
        startLocationService();
        registerBroadcastReceiver();
    }

    private void initView(Bundle savedInstanceState) {
        ButterKnife.inject(this);
        initToolBar();
        initAmapComponent(savedInstanceState);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.footprint);
        toolbar.setNavigationOnClickListener(navigationOnClickListener);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initAmapComponent(Bundle savedInstanceState) {
        if (aMap == null) {
            mapView.onCreate(savedInstanceState);
            aMap = mapView.getMap();
            uiSettings = aMap.getUiSettings();
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
            myLocationStyle.strokeColor(Color.BLACK);
            myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 255));
            myLocationStyle.strokeWidth(2);
            uiSettings.setCompassEnabled(true);//show compass
            uiSettings.setScaleControlsEnabled(true);//comparing rule
            uiSettings.setMyLocationButtonEnabled(true);// myLocation button
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));//zoom to size 15
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setLocationSource(this);//start location listener
            aMap.setMyLocationEnabled(true);// enable location and show my location
            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);// set location type
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        startLocationService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        stopLocationService();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        stopLocationService();
        unRegisterBroadcastReceiver();
    }

    private void startLocationService() {
        startLocationIntent =new Intent(this, LocationService.class);
        startService(startLocationIntent);
    }

    private void stopLocationService() {
        startLocationIntent =new Intent(this, LocationService.class);
        stopService(startLocationIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_main:
                Intent i = new Intent(getApplicationContext(), SearchableActivity.class);
                startActivity(i);
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    private void registerBroadcastReceiver(){
        locationBroadCastReceiver = new LocationBroadCastReceiver();
        IntentFilter filter = new IntentFilter(LOCATION_CHANGED_ACTION);
        registerReceiver(locationBroadCastReceiver, filter);
    }

    private void unRegisterBroadcastReceiver() {
        unregisterReceiver(locationBroadCastReceiver);
    }

    private class LocationBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MainActivity.LOCATION_CHANGED_ACTION)){
                Bundle bundle = intent.getExtras();
                Location newLocation = bundle.getParcelable("LOCATION");
                mListener.onLocationChanged(newLocation);
                if (null != currentLocation && null != newLocation) {
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                            new LatLng(newLocation.getLatitude(), newLocation.getLongitude()));
                    polylineOptions.color(Color.BLUE);
                    MainActivity.this.aMap.addPolyline(polylineOptions);
                }
                currentLocation = newLocation;
                Log.v("accuracy", String.valueOf(newLocation.getAccuracy()));
                Log.v("provider", String.valueOf(newLocation.getProvider()));
            }
        }
    }
}
