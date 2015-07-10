package com.thoughtworks.rdlian.footprint.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.thoughtworks.rdlian.footprint.MainActivity;
import com.thoughtworks.rdlian.footprint.common.LocationStrategy;
import com.thoughtworks.rdlian.footprint.dao.PointDao;
import com.thoughtworks.rdlian.footprint.dao.model.Point;
import com.thoughtworks.rdlian.footprint.dao.model.PointParcelable;



/**
 * Created by rdlian on 7/9/15.
 */
public class LocationService extends Service implements AMapLocationListener {

    private AMapLocation currentLocation;
    private LocationManagerProxy locationManagerProxy;
    private Context context;
    private PointDao pointDao;
    private Intent locationIntent;
    private Bundle bundle;

    private static int minTime = 20 * 1000;
    private static int minDistance = 10;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationIntent = new Intent();
        bundle = new Bundle();
        context = getApplicationContext();
        pointDao = new PointDao(context);
        locationManagerProxy = LocationManagerProxy.getInstance(context);
        locationManagerProxy.setGpsEnable(false);
        locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, minTime, minDistance, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
        }
        locationManagerProxy = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getAMapException().getErrorCode() == 0) {
                if (LocationStrategy.isBetterLocation(aMapLocation, currentLocation)) {
                    currentLocation = aMapLocation;
                    pointDao.insertPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                    sendLocationChangedBroadcast(pointDao.getFirstPoint());
                }
            }
        }
    }

    private void sendLocationChangedBroadcast(Point point){
        locationIntent.setAction(MainActivity.LOCATION_CHANGED_ACTION);
        PointParcelable pointParcelable = new PointParcelable();
        pointParcelable.setLatitude(point.getLatitude());
        pointParcelable.setLongitude(point.getLongitude());
        locationIntent.putExtra("LOCATION", pointParcelable);
        sendBroadcast(locationIntent);

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
