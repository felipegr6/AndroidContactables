package com.example.android.application;

import android.app.Application;

import com.example.android.database.ExampleDatabase;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ExampleDatabase.init(this);
    }
}
