package com.example.ecarxspeedometer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.ecarxspeedometer.databinding.ActivityMainBinding
import com.example.ecarxspeedometer.service.ISpeedometerJavaDataSource
import com.example.ecarxspeedometer.service.SpeedometerJavaDataService
import com.example.ecarxspeedometer.viewmodel.MainViewModel
import com.example.ecarxspeedometer.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity(), ISpeedometerJavaDataSource {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mService: SpeedometerJavaDataService

    private val viewModel by viewModels<MainViewModel> { MainViewModelFactory() }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as SpeedometerJavaDataService.LocalBinder
            mService = binder.getService()
            viewModel.mBound = true
            mService.assignCallback(this@MainActivity)
            mService.emitContinuousData()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            viewModel.mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService.
        if (viewModel.getDataFromJavaService){
            Intent(this, SpeedometerJavaDataService::class.java).also { intent ->
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
        } else {
            //get data from C service
        }
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.getDataFromJavaService){
            unbindService(serviceConnection)
            viewModel.mBound = false
        } else {
            // stop the service running in C
        }
    }

    override fun onNewData(rpm: Int) {
        Log.d("newDataFromJava", "$rpm")
        binding.speedometer.speedTo(viewModel.getLinearVelocity(rpm), moveDuration = 1000)
        binding.tachometer.speedTo(viewModel.getGearRatio(rpm).toFloat(), moveDuration = 1000)
        binding.rpmInfo.text = "${viewModel.getGearRatio(rpm)} * ${viewModel.getWheelRPM(rpm)} RPM"
    }

    /**
     * A native method that is implemented by the 'ecarxspeedometer' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'ecarxspeedometer' library on application startup.
        init {
            System.loadLibrary("ecarxspeedometer")
        }
    }
}