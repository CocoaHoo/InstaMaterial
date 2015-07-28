package io.github.mrcocoa.instamaterial;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by cocoa on 15/7/28.
 * Email:cocoahoo@gmail.com
 **/


public class InstaMaterialApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
