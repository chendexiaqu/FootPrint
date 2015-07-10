package com.thoughtworks.rdlian.footprint.services;

import android.content.Context;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.LatLng;
import com.thoughtworks.rdlian.footprint.dao.PointDao;
import com.thoughtworks.rdlian.footprint.dao.model.Point;

import java.util.List;

/**
 * Created by rdlian on 7/9/15.
 */
public class PointService {

    private PointDao pointDao;

    public PointService(Context context) {
        pointDao = new PointDao(context);
    }

    private void basicCRUD(AMap aMap) {
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
}
