package me.ztiany.capturer;

import android.app.Application;

import timber.log.Timber;

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
