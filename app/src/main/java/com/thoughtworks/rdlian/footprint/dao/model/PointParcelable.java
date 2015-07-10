package com.thoughtworks.rdlian.footprint.dao.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rdlian on 7/10/15.
 */
public class PointParcelable implements Parcelable {

    private double latitude;
    private double longitude;

    public PointParcelable() {}

    public PointParcelable(Parcel in) {
        readFromParcel(in);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    private void readFromParcel(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PointParcelable createFromParcel (Parcel in) {
            return new PointParcelable(in);
        }

        public PointParcelable [] newArray(int size) {
            return new PointParcelable[size];
        }
    };

}
