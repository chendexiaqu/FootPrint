package com.thoughtworks.rdlian.footprint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.thoughtworks.rdlian.footprint.dao.PointDao;
import com.thoughtworks.rdlian.footprint.dao.model.Point;
import com.thoughtworks.rdlian.footprint.dao.model.PointParcelable;
import com.thoughtworks.rdlian.footprint.services.LocationService;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

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


    private PointDao pointDao;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mapView.onCreate(savedInstanceState);
        init();
//        basicCRUD();
        intent =new Intent(this, LocationService.class);
        startService(intent);
        registerBroadcastReceiver();

    }

    private void registerBroadcastReceiver(){
        locationBroadCastReceiver = new LocationBroadCastReceiver();
        IntentFilter filter = new IntentFilter(LOCATION_CHANGED_ACTION);
        registerReceiver(locationBroadCastReceiver, filter);
    }

    private void basicCRUD() {
        pointDao = new PointDao(this);
        pointDao.removeAllPoints();
        pointDao.insertPoint(43.828, 87.621);//first
        pointDao.insertPoint(34.16, 108.54);//second
        pointDao.insertPoint(45.808, 126.55);//third
        List<Point> points = pointDao.getAllPoints();
        LatLng first = new LatLng(points.get(0).getLatitude(), points.get(0).getLongitude());
        LatLng second = new LatLng(points.get(1).getLatitude(), points.get(1).getLongitude());
        LatLng third = new LatLng(points.get(2).getLatitude(), points.get(2).getLongitude());
        // 绘制一个经过北京的弧形
        ArcOptions arcOptions = new ArcOptions();
        arcOptions.point(first, second, third);
        arcOptions.strokeColor(Color.RED);
        aMap.addArc(arcOptions);

    }

    private void init() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.footprint);
        toolbar.setNavigationOnClickListener(navigationOnClickListener);
        MyLocationStyle myLocationStyle = null;
        if (aMap == null) {
            aMap = mapView.getMap();
            //aMap.setMapType(AMap.MAP_TYPE_SATELLITE);//开启卫星视图
            uiSettings = aMap.getUiSettings();
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
            myLocationStyle.strokeColor(Color.BLACK);
            myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 255));
            myLocationStyle.strokeWidth(2);
        }
        uiSettings.setCompassEnabled(true);//是否显示指南针
        uiSettings.setScaleControlsEnabled(true);//显示比例尺
        uiSettings.setMyLocationButtonEnabled(true);// 是否显示定位按钮
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));//zoom to size 15
        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setLocationSource(this);//设置定位监听
//        aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
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
        pointDao.dispose();
        intent =new Intent(this, LocationService.class);
        stopService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

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

    private class LocationBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MainActivity.LOCATION_CHANGED_ACTION)){
                Bundle bundle = intent.getExtras();
//                String stringLocation = intent.getStringExtra("LOCATION");
//                Log.v("happy", stringLocation);
                PointParcelable point = bundle.getParcelable("LOCATION");
                Log.v("happy", String.valueOf(point.getLatitude()));
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.GREEN).width(15);
                polylineOptions.add(new LatLng(43.828, 87.621));
                polylineOptions.add(new LatLng(34.16, 108.54));
                polylineOptions.add(new LatLng(45.808, 126.55));
                MainActivity.this.aMap.addPolyline(polylineOptions);
            }
        }
    }

}
