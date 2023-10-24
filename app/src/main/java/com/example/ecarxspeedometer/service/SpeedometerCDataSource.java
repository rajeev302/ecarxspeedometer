package com.example.ecarxspeedometer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SpeedometerCDataSource extends Service
{
    static
    {
        System.loadLibrary("native-lib");
    }

    private IBinder mBinder;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mBinder = createServiceBinder();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    public native IBinder createServiceBinder();
}
