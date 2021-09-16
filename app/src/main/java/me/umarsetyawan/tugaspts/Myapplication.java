package me.umarsetyawan.tugaspts;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Myapplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("mahasiswa.db")
                .schemaVersion(0)
                .allowQueriesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
