package com.thoughtworks.rdlian.footprint.dao;

import android.content.Context;

import io.realm.Realm;

/**
 * Created by rdlian on 7/8/15.
 */
public class AbstractDao {

    protected Realm realm;

    public AbstractDao(Context context) {
        realm = Realm.getInstance(context);
    }

    public void dispose() {
        realm.close();
    }

}
