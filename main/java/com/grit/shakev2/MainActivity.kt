package com.grit.shakev2

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView
    private lateinit var squareRotation: TextView
    private lateinit var data: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        square = findViewById(R.id.square_mid)
        squareRotation = findViewById(R.id.square_rotate)
        data = findViewById(R.id.data)

        setupSensor();

    }

    private fun setupSensor() {

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
            // Crashes at FASTEST?
        )

    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {


            val sides = event.values[0]

            val upDown = event.values[1]

            val depth = event.values[2]

            squareRotation.apply {
                scaleX = (depth * 0.2).toFloat()
                scaleY = (depth * 0.2).toFloat()

            }

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }


            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN
            else Color.RED

            square.setBackgroundColor(color)

            square.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"
            squareRotation.text = "Tilt ${depth.toInt()}"
            data.text = "X: ${upDown.toInt()} Y: ${sides.toInt()} Z: ${depth.toInt()}"


        } else square.text = "Cant read sensor"

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}