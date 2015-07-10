package com.thoughtworks.rdlian.footprint.dao.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by rdlian on 7/8/15.
 */
public class Path extends RealmObject {

    private String userId;

    private RealmList<Point> points;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RealmList<Point> getPoints() {
        return points;
    }

    public void setPoints(RealmList<Point> points) {
        this.points = points;
    }

}
