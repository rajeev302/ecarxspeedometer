package com.example.ecarxspeedometer.service

interface ISpeedometerJavaDataSource {
    fun onNewData(rpm: Int)
}