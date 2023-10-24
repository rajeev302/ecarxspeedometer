package com.example.ecarxspeedometer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random

class SpeedometerJavaDataService : Service() {
    // Binder given to clients.
    private val binder = LocalBinder()

    // Random number generator.
    private val mGenerator = Random()

    private var accelerateDecelerateRandom: Int = 0
    private var lastRPMValue: Int = 0
    private val perAccelerationValue: Int = 500

    private lateinit var dataSourceCallback: ISpeedometerJavaDataSource

    /** Method for clients.  */

    fun assignCallback(callback: ISpeedometerJavaDataSource){
        dataSourceCallback = callback
    }

    fun emitContinuousData(){
        CoroutineScope(Dispatchers.IO).launch{
            while (true){
                accelerateDecelerateRandom = mGenerator.nextInt(2)
                when(accelerateDecelerateRandom){
                    1 -> {
                        lastRPMValue += perAccelerationValue
                    }
                    2 -> {
                        lastRPMValue -= perAccelerationValue
                    }
                    else -> {

                    }
                }
                Thread.sleep(2500)
                CoroutineScope(Dispatchers.Main).launch{
                    dataSourceCallback.onNewData(lastRPMValue)
                }
            }
        }
    }
    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): SpeedometerJavaDataService = this@SpeedometerJavaDataService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}