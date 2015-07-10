package com.thoughtworks.rdlian.footprint.dao;

import android.content.Context;

import com.thoughtworks.rdlian.footprint.dao.model.Point;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by rdlian on 7/8/15.
 */
public class PointDao extends AbstractDao {

    public PointDao(Context context) {
        super(context);
    }

    public List<Point> getAllPoints() {
        return realm.where(Point.class).findAll();
    }

    public Point getFirstPoint() {
        return realm.where(Point.class).findFirst();
    }

    public Point getLastPoint() {
        return realm.where(Point.class).findAll().last();
    }

    public void removeAllPoints() {
        realm.beginTransaction();
        RealmResults<Point> results = realm.where(Point.class).findAll();
        results.clear();
        realm.commitTransaction();
    }

    public void insertPoint(double latitude, double longitude) {
        realm.beginTransaction();
        Point point = realm.createObject(Point.class);
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        realm.commitTransaction();
    }

}
