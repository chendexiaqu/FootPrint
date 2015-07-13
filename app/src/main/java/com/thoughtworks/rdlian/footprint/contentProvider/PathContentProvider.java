package com.thoughtworks.rdlian.footprint.contentProvider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.thoughtworks.rdlian.footprint.dao.PointDao;
import com.thoughtworks.rdlian.footprint.dao.model.Point;

import java.util.List;

/**
 * Created by rdlian on 7/12/15.
 */
public class PathContentProvider extends ContentProvider {


    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String query = uri.getLastPathSegment().toLowerCase();
        Log.v("getlocation", query);
            PointDao pointDao = new PointDao(getContext());
            List<Point> points = pointDao.getPointsByName(query);
        if (points.isEmpty()) {
            return null;
        }
            String [] names = new String[] {"_id", SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA};
            MatrixCursor cursor = new MatrixCursor(names);
            String pointString = "";
            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                pointString += "latitude: " + point.getLatitude() + " longitude: " + point.getLongitude() + "\n";
            }
            cursor.addRow(new String[]{String.valueOf(0), "lianrundong", pointString});
            return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
