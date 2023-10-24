package com.example.ecarxspeedometer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {
    var mBound: Boolean = false
    var getDataFromJavaService: Boolean = true

    /**
     * all the below functions to get linear velocity or gear ratio are just for demonstration.
     * These are not exact calculations. These can be replaced with exact calculations as required.*/

    fun getLinearVelocity(rpm: Int): Float {
        return (rpm*0.005).toFloat();
    }

    fun getWheelRPM(rpm: Int): Int {
        when(rpm){
            in 0..4000 -> {
                return rpm
            }
            in 4000..8000 -> {
                return rpm - 4000
            }
            in 8000..12000 -> {
                return rpm - 4000
            }
            in 12000..16000 -> {
                return rpm - 8000
            }
            in 16000..20000 -> {
                return rpm - 16000
            }
            in 20000..24000 -> {
                return rpm - 20000
            }
        }
        return 0
    }

    fun getGearRatio(rpm: Int): Int {
        when(rpm){
            in 0..4000 -> {
                return 1
            }
            in 4000..8000 -> {
                return 2
            }
            in 8000..12000 -> {
                return 3
            }
            in 12000..16000 -> {
                return 4
            }
            in 16000..20000 -> {
                return 5
            }
            in 20000..24000 -> {
                return 6
            }
        }
        return 0
    }
}