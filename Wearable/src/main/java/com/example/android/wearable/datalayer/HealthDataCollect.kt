/*
package com.example.android.wearable.datalayer

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import com.example.android.wearable.datalayer.constants.*

abstract class HealthDataCollect : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private lateinit var mOrientation: Sensor
    private lateinit var linearSensor: Sensor

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START_FOREGROUND" -> {
                startForegroundService()
            }
            "STOP_FOREGROUND" -> {
                stopForegroundService()
            }
        }
        return START_STICKY
    }

    private fun startForegroundService() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, linearSensor, SensorManager.SENSOR_DELAY_UI)
        //val notification = HealthDataNotification.createNotification(this)
        val notification = HealthDataNotification.createNotification(this)
        startForeground(20, notification)
    }

    private fun stopForegroundService() {
        sensorManager.unregisterListener(this)
        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)!!
        mOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        linearSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)!!

        sensorManager.registerListener(this, sensor, 3)
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_HEART_RATE) {
            mHeartRate = event.values[0].toInt()
        } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            mGravAccelerateX = event.values[0].toDouble()
            mGravAccelerateY = event.values[1].toDouble()
            mGravAccelerateZ = event.values[2].toDouble()
        } else if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            mLinAccelerateX = event.values[0].toDouble()
            mLinAccelerateY = event.values[1].toDouble()
            mLinAccelerateZ = event.values[2].toDouble()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}
*/
